package ua.kapitonenko.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.CashboxDAO;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.records.Cashbox;
import ua.kapitonenko.app.domain.records.ZReport;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
	
	private ReportService reportService;
	@Mock
	private DAOFactory daoFactoryMock;
	@Mock
	private ConnectionWrapper wrapperMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private ZReportDAO zReportDAOMock;
	@Mock
	private Report reportMock;
	@Mock
	private ReceiptService receiptServiceMock;
	@Mock
	private ServiceFactory serviceFactoryMock;
	@Mock
	private SettingsService settingsServiceMock;
	@Mock
	private ZReport inMemoryZReportMock;
	@Mock
	private ZReport persistentZReportMock;
	@Mock
	private CashboxDAO cashboxDAOMock;
	
	
	@Before
	public void setUp() throws Exception {
		reportService = Application.getServiceFactory().getReportService();
		reportService.setDaoFactory(daoFactoryMock);
		reportService.setServiceFactory(serviceFactoryMock);
		when(daoFactoryMock.getConnection()).thenReturn(wrapperMock);
		when(daoFactoryMock.getCashboxDao(connectionMock)).thenReturn(cashboxDAOMock);
		when(wrapperMock.open()).thenReturn(connectionMock);
		when(daoFactoryMock.getZReportDAO(connectionMock)).thenReturn(zReportDAOMock);
		when(serviceFactoryMock.getSettingsService()).thenReturn(settingsServiceMock);
		when(serviceFactoryMock.getReceiptService()).thenReturn(receiptServiceMock);
	}
	
	@Test
	public void createZReportShouldReturnFalseIfInsertionFailed() throws Exception {
		when(zReportDAOMock.insert(any(ZReport.class))).thenReturn(false);
		when(reportMock.getCashbox()).thenReturn(mock(Cashbox.class));
		when(reportMock.getUserId()).thenReturn(null);
		when(reportMock.getCashBalance()).thenReturn(null);
		
		boolean actual = reportService.createZReport(reportMock);
		assertThat(actual, is(false));
	}
	
	@Test
	public void getCountShouldReturnNumberOfRecords() {
		long expected = 3333333337777779977L;
		when(zReportDAOMock.getCount()).thenReturn(expected);
		
		assertThat(reportService.getCount(), is(equalTo(expected)));
		verify(wrapperMock).close();
	}
	
	@Test
	public void getReportListShouldReturnReportsWithAllDependencies() throws Exception {
		int offset = 20;
		int limit = 2;
		
		when(zReportDAOMock.findAllByQuery(anyString(), any())).thenReturn(Arrays.asList(persistentZReportMock, persistentZReportMock));
		
		List<Report> result = reportService.getReportList(offset, limit);
		verifyListRefsSet(limit, result);
	}
	
	@Test
	public void getReportListShouldReturnEmptyListWhenNoRecordsFound() throws Exception {
		int offset = 20;
		int limit = 2;
		
		when(zReportDAOMock.findAllByQuery(anyString(), any())).thenReturn(Collections.emptyList());
		
		List<Report> result = reportService.getReportList(offset, limit);
		verifyListRefsSet(0, result);
	}
	
	private void verifyListRefsSet(int listSize, List<Report> result) {
		verify(settingsServiceMock, times(listSize)).getTaxCatList();
		verify(settingsServiceMock, times(listSize)).getPaymentTypes();
		verify(receiptServiceMock, times(listSize)).getReceiptList(anyLong(), anyLong());
		verify(persistentZReportMock, times(listSize)).getCreatedBy();
		assertThat(result.size(), is(equalTo(listSize)));
	}
	
}