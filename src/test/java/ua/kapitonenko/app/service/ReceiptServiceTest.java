package ua.kapitonenko.app.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.fixtures.ReceiptStub;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.fixtures.TestServiceFactory;
import ua.kapitonenko.app.persistence.connection.ConnectionWrapper;
import ua.kapitonenko.app.persistence.dao.*;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptServiceTest {
	
	private ReceiptService receiptService;
	
	private Receipt receipt;
	private ServiceFactory appServiceFactory;
	private ModelFactory appModelFactory;
	
	@Mock
	private DAOFactory daoFactory;
	@Mock
	private ConnectionWrapper wrapper;
	@Mock
	private Connection connection;
	@Mock
	private ReceiptDAO receiptDAO;
	@Mock
	private ReceiptProductDAO receiptProductDAO;
	@Mock
	private ProductDAO productDAO;
	@Mock
	private CashboxDAO cashboxDAO;
	@Mock
	private PaymentTypeDAO paymentTypeDAO;
	@Mock
	private ReceiptTypeDAO receiptTypeDAO;
	@Mock
	private ReceiptRecord record;
	@Mock
	private ReceiptRecord inserted;
	
	
	@Before
	public void setUp() throws Exception {
		receipt = new ReceiptStub(record);
		
		receiptService = Application.getServiceFactory().getReceiptService();
		receiptService.setDaoFactory(daoFactory);
		when(daoFactory.getConnection()).thenReturn(wrapper);
		when(wrapper.open()).thenReturn(connection);
		
		when(daoFactory.getReceiptDAO(connection)).thenReturn(receiptDAO);
		when(daoFactory.getReceiptProductDAO(connection)).thenReturn(receiptProductDAO);
		when(daoFactory.getProductDAO(connection)).thenReturn(productDAO);
		when(daoFactory.getCashboxDao(connection)).thenReturn(cashboxDAO);
		when(daoFactory.getReceiptTypeDAO(connection)).thenReturn(receiptTypeDAO);
		when(daoFactory.getPaymentTypeDAO(connection)).thenReturn(paymentTypeDAO);
		
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
	public void updateShouldUpdateRecordInsertProductsAndUpdateStock() throws Exception {
		Long recordId = 14L;
		int prodSize = 5;
		when(inserted.getId()).thenReturn(recordId);
		when(record.getId()).thenReturn(recordId);
		when(receiptDAO.update(record)).thenReturn(true);
		when(receiptDAO.findOne(recordId)).thenReturn(inserted);
		when(receiptProductDAO.findAllByReceiptId(recordId)).thenReturn(Collections.emptyList());
		when(productDAO.findOne(anyLong())).thenReturn(mock(ProductRecord.class));
		receipt.setProducts(ModelUtils.generateProductList(prodSize));
		
		receiptService.update(receipt);
		verify(receiptDAO).update(record);
		verify(receiptProductDAO, times(prodSize)).insert(any());
		verify(productDAO, times(prodSize)).update(any());
	}
	
	@Test
	public void createShouldInsertReceiptRecordAndUpdateRecord() throws Exception {
		when(inserted.getId()).thenReturn(87L);
		when(receiptDAO.insert(record)).thenReturn(true);
		when(receiptDAO.findOne(anyLong())).thenReturn(inserted);
		
		boolean result = receiptService.create(receipt);
		assertThat(result, is(true));
		assertThat(receipt.getRecord().getId(), is(not(equalTo(record.getId()))));
	}
	
	@Test
	public void createShouldReturnFalseIfInsertionFailed() throws Exception {
		when(receiptDAO.insert(record)).thenReturn(false);
		
		boolean result = receiptService.create(receipt);
		assertThat(result, is(false));
	}
	
	@Test
	public void createReturnShouldCopyRecordSetReturnTypeAndInsert() throws Exception {
		boolean result = receiptService.createReturn(receipt);
		ReceiptRecord returnRecord = receipt.getRecord();
		assertThat(result, is(false));
		assertThat(returnRecord.getReceiptTypeId(), is(equalTo(Application.Ids.RECEIPT_TYPE_RETURN.getValue())));
		verify(receiptDAO).insert(returnRecord);
	}
	
	@Test
	public void getReceiptsListLimitShouldReturnListOfReceiptsWithAllDependencies() {
		int offset = 20;
		int limit = 2;
		List<ReceiptRecord> records = Arrays.asList(new ReceiptRecord(), new ReceiptRecord());
		when(receiptDAO.findAll(offset, limit)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(offset, limit);
		verifyListRefsSet(limit, result);
		
	}
	
	@Test
	public void getReceiptsListLimitShouldReturnEmptyListWhenNoRecordsFound() {
		int offset = 20;
		int limit = 2;
		List<ReceiptRecord> records = Collections.emptyList();
		when(receiptDAO.findAll(offset, limit)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(offset, limit);
		
		verifyListRefsSet(0, result);
	}
	
	@Test
	public void getReceiptsListByZReportIdShouldReturnListOfReceiptsWithAllDependencies() {
		Long cashboxId = 8L;
		Long reportId = 55L;
		int listSize = 2;
		List<ReceiptRecord> records = Arrays.asList(new ReceiptRecord(), new ReceiptRecord());
		when(receiptDAO.findAllByZReportId(reportId, cashboxId)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(reportId, cashboxId);
		verifyListRefsSet(listSize, result);
		
	}
	
	@Test
	public void getReceiptsListByZReportIdShouldReturnEmptyListWhenNoRecordsFound() {
		Long cashboxId = 8L;
		Long reportId = 55L;
		int listSize = 0;
		List<ReceiptRecord> records = Collections.emptyList();
		when(receiptDAO.findAllByZReportId(reportId, cashboxId)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(reportId, cashboxId);
		
		verifyListRefsSet(listSize, result);
	}
	
	@Test
	public void getReceiptsListByCashbosShouldReturnListOfReceiptsWithAllDependencies() {
		Long cashboxId = 8L;
		int listSize = 2;
		List<ReceiptRecord> records = Arrays.asList(new ReceiptRecord(), new ReceiptRecord());
		when(receiptDAO.findAllByCashboxId(cashboxId)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(cashboxId);
		
		verifyListRefsSet(listSize, result);
	}
	
	@Test
	public void getReceiptsListByCashboxShouldReturnEmptyListWhenNoRecordsFound() {
		Long cashboxId = 8L;
		int listSize = 0;
		List<ReceiptRecord> records = Collections.emptyList();
		when(receiptDAO.findAllByCashboxId(cashboxId)).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(cashboxId);
		
		verifyListRefsSet(listSize, result);
	}
	
	private void verifyListRefsSet(int listSize, List<Receipt> result) {
		verify(cashboxDAO, times(listSize)).findOne(anyLong());
		verify(paymentTypeDAO, times(listSize)).findOne(anyLong());
		verify(receiptTypeDAO, times(listSize)).findOne(anyLong());
		assertThat(result.size(), is(equalTo(listSize)));
	}
	
	@Test
	public void findOneByRecordIdShouldReturnReceiptWithRecore() {
		Long id = 963L;
		when(receiptDAO.findOne(id)).thenReturn(record);
		when(record.getId()).thenReturn(id);
		
		Receipt receipt = receiptService.findOne(id);
		
		assertThat(receipt.getRecord().getId(), is(equalTo(id)));
	}
	
	@Test
	public void getCountShouldReturnNumberOfRecords() {
		long expected = 477L;
		when(receiptDAO.getCount()).thenReturn(expected);
		
		assertThat(receiptService.getCount(), is(equalTo(expected)));
	}
}