package ua.kapitonenko.app.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.records.ZReport;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.ReportType;
import ua.kapitonenko.app.fixtures.ReportStub;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.fixtures.TestServiceFactory;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
	
	private ReportService reportService;
	
	private ServiceFactory appServiceFactory;
	private ModelFactory appModelFactory;
	
	@Mock
	private DAOFactory daoFactoryMock;
	@Mock
	private ConnectionWrapper wrapperMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private ZReportDAO zReportDAOMock;
	@Mock
	private ZReport zReportMock;
	
	
	@Before
	public void setUp() throws Exception {
		reportService = Application.getServiceFactory().getReportService();
		reportService.setDaoFactory(daoFactoryMock);
		when(daoFactoryMock.getConnection()).thenReturn(wrapperMock);
		when(wrapperMock.open()).thenReturn(connectionMock);
		when(daoFactoryMock.getZReportDAO(connectionMock)).thenReturn(zReportDAOMock);
		
		appServiceFactory = Application.getServiceFactory();
		Application.setServiceFactory(TestServiceFactory.getInstance());
		appModelFactory = Application.getModelFactory();
		Application.setModelFactory(new TestModelFactory());
	}
	
	@After
	public void tearDown() throws Exception {
		Application.setServiceFactory(appServiceFactory);
		Application.setModelFactory(appModelFactory);
	}
	
	@Test
	public void createZReportShouldInsertZReportRecordAndSetItToReport() throws Exception {
		when(zReportDAOMock.insert(any(ZReport.class))).thenReturn(true);
		when(zReportDAOMock.findOne(anyLong())).thenReturn(zReportMock);
		Report report = new ReportStub();
		
		boolean actual = reportService.createZReport(report);
		assertThat(actual, is(true));
		assertThat(report.getRecord(), is(equalTo(zReportMock)));
	}
	
	@Test
	public void createZReportShouldReturnFalseIfInsertionFailed() throws Exception {
		when(zReportDAOMock.insert(any(ZReport.class))).thenReturn(false);
		Report report = new ReportStub();
		
		boolean actual = reportService.createZReport(report);
		assertThat(actual, is(false));
	}
	
	@Test
	public void getCount() throws Exception {
		long expected = 3333333337777779977L;
		when(zReportDAOMock.getCount()).thenReturn(expected);
		
		assertThat(reportService.getCount(), is(equalTo(expected)));
		verify(wrapperMock).close();
	}
	
	@Test
	public void getReportListShouldReturnReportsWithDependencies() throws Exception {
		int size = 14;
		List<ZReport> zReportList = Stream.generate(() -> zReportMock)
				                            .limit(size)
				                            .collect(Collectors.toList());
		when(zReportDAOMock.findAllByQuery(anyString(), any())).thenReturn(zReportList);
		when(zReportMock.getCreatedBy()).thenReturn(1L);
		
		List<Report> reports = reportService.getReportList(size, size);
		assertThat(reports.size(), is(equalTo(size)));
		reports.forEach(this::verifyDependencies);
	}
	
	@Test
	public void getReportListShouldReturnEmptyListWhenRecordsNotFound() throws Exception {
		when(zReportDAOMock.findAllByQuery(anyString(), any())).thenReturn(Collections.emptyList());
		when(zReportMock.getCreatedBy()).thenReturn(1L);
		
		List<Report> reports = reportService.getReportList(20, 5);
		assertThat(reports.isEmpty(), is(true));
		
	}
	
	@Test
	public void createShouldSetReportsDependencies() throws Exception {
		ReportStub report = new ReportStub();
		reportService.create(report);
		
		verifyDependencies(report);
	}
	
	@Test
	public void createShouldInsertZReportOnZReportType() throws Exception {
		ReportStub report = new ReportStub();
		report.setType(ReportType.Z_REPORT);
		reportService.create(report);
		
		verify(zReportDAOMock).insert(any(ZReport.class));
	}
	
	@Test
	public void createShouldNotInsertZReportOnXReportType() throws Exception {
		ReportStub report = new ReportStub();
		report.setType(ReportType.X_REPORT);
		reportService.create(report);
		
		verify(zReportDAOMock, never()).insert(any(ZReport.class));
	}
	
	private void verifyDependencies(Report report) {
		assertThat(report.getCashbox(), is(not(nullValue())));
		assertThat(((ReportStub) report).isSummaryInitialized(), is(true));
	}
	
}