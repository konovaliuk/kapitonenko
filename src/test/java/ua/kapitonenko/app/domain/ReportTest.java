package ua.kapitonenko.app.domain;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.fixtures.ReportSummaryMock;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.persistence.records.ZReport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportTest {
	
	private Report report;
	
	private ModelFactory appModelFactory;
	
	@Mock
	private ZReport zReportRecord;
	
	@Before
	public void setUp() throws Exception {
		appModelFactory = Application.getModelFactory();
		report = appModelFactory.createReport(1L);
		Application.setModelFactory(new TestModelFactory());
	}
	
	@After
	public void tearDown() throws Exception {
		Application.setModelFactory(appModelFactory);
	}
	
	@Test
	public void initSummaryShouldGroupReceiptsByReceiptType() throws Exception {
		long expectedSalesNo = 3L;
		long expectedRefundNo = 2L;
		
		report.initSummary(ModelUtils.generateReceiptList(expectedSalesNo, expectedRefundNo), Collections.emptyList(), Collections.emptyList());
		assertThat(report.getSalesFigures().getNoReceipts(), is(equalTo(expectedSalesNo)));
		assertThat(report.getRefundsFigures().getNoReceipts(), is(equalTo(expectedRefundNo)));
	}
	
	@Test
	public void initSummaryShouldInitFields() throws Exception {
		int expectedTaxCatSize = 4;
		int expectedPayTypeSize = 9;
		
		report.initSummary(ModelUtils.generateReceiptList(2L, 3L),
				ModelUtils.generateTaxCatList(expectedTaxCatSize),
				ModelUtils.generatePayTypesList(expectedPayTypeSize));
		
		assertThat(report.getFields().isEmpty(), is(false));
		
		ReportSummaryMock sales = (ReportSummaryMock) report.getSalesFigures();
		ReportSummaryMock refunds = (ReportSummaryMock) report.getRefundsFigures();
		
		sales.verifyTaxCatCalls(expectedTaxCatSize);
		sales.verifyPayTypeCalls(expectedPayTypeSize);
		refunds.verifyTaxCatCalls(expectedTaxCatSize);
		refunds.verifyPayTypeCalls(expectedPayTypeSize);
	}
	
	@Test
	public void getCreatedAtShouldReturnCurDateForXReport() throws Exception {
		report.setType(ReportType.X_REPORT);
		assertThat(DateUtils.isSameDay(report.getCreatedAt(), new Date()), is(true));
	}
	
	@Test
	public void getCreatedAtShouldReturnRecordDateForZReport() throws Exception {
		Date expected = java.sql.Date.valueOf(LocalDate.of(2000, 12, 31));
		when(zReportRecord.getCreatedAt()).thenReturn(expected);
		report.setRecord(zReportRecord);
		report.setType(ReportType.Z_REPORT);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		assertThat(sdf.format(report.getCreatedAt()), is(equalTo(sdf.format(expected))));
		
	}
	
	@Test
	public void getCashBalanceShouldReturnSalesRefundsCashAmountDiff() throws Exception {
		report.initSummary(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
		BigDecimal salesCash = BigDecimal.valueOf(999.33);
		BigDecimal refundsCash = BigDecimal.valueOf(111.11);
		ReportSummaryMock sales = (ReportSummaryMock) report.getSalesFigures();
		ReportSummaryMock refunds = (ReportSummaryMock) report.getRefundsFigures();
		sales.setCashAmount(salesCash);
		refunds.setCashAmount(refundsCash);
		
		assertThat(report.getCashBalance(), is(equalTo(salesCash.subtract(refundsCash))));
	}
	
	@Test
	public void getDocTypeShouldReturnFiscalOnZReport() throws Exception {
		report.setType(ReportType.Z_REPORT);
		
		assertThat(report.getDocType(), is(equalTo(Keys.FISCAL)));
	}
	
	@Test
	public void getDocTypeShouldReturnNullOnXReport() throws Exception {
		report.setType(ReportType.X_REPORT);
		
		assertThat(report.getDocType(), is(nullValue()));
	}
	
}