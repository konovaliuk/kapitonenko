package ua.kapitonenko.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ReceiptProduct;
import ua.kapitonenko.app.domain.records.ReceiptRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptServiceTest {
	
	private ReceiptService receiptService;
	
	@Mock
	private Receipt receipt;
	@Mock
	private ReceiptRecord record;
	@Mock
	private ReceiptRecord inserted;
	@Mock
	private DAOFactory daoFactory;
	@Mock
	private ConnectionWrapper wrapper;
	@Mock
	private Connection connection;
	@Mock
	private ReceiptDAO receiptDAO;
	@Mock
	private CashboxDAO cashboxDAO;
	@Mock
	private PaymentTypeDAO paymentTypeDAO;
	@Mock
	private ReceiptTypeDAO receiptTypeDAO;
	@Mock
	private ReceiptProductDAO receiptProductDAO;
	@Mock
	private UserDAO userDAO;
	@Mock
	private ProductService productService;
	@Mock
	private SettingsService settingsService;
	@Mock
	private ServiceFactory serviceFactory;
	@Mock
	private ProductDAO productDAO;
	
	@Before
	public void setUp() throws Exception {
		receiptService = Application.getServiceFactory().getReceiptService();
		receiptService.setDaoFactory(daoFactory);
		receiptService.setServiceFactory(serviceFactory);
		when(serviceFactory.getProductService()).thenReturn(productService);
		when(serviceFactory.getSettingsService()).thenReturn(settingsService);
		when(daoFactory.getConnection()).thenReturn(wrapper);
		when(wrapper.open()).thenReturn(connection);
		when(daoFactory.getReceiptDAO(connection)).thenReturn(receiptDAO);
		when(daoFactory.getCashboxDao(connection)).thenReturn(cashboxDAO);
		when(daoFactory.getUserDAO(connection)).thenReturn(userDAO);
		when(daoFactory.getReceiptProductDAO(connection)).thenReturn(receiptProductDAO);
		when(daoFactory.getPaymentTypeDAO(connection)).thenReturn(paymentTypeDAO);
		when(daoFactory.getReceiptTypeDAO(connection)).thenReturn(receiptTypeDAO);
		when(daoFactory.getProductDAO(connection)).thenReturn(productDAO);
		when(receipt.getRecord()).thenReturn(record);
		
	}
	
	@Test
	public void createShouldInsertReceiptRecordAndUpdateAllRefferences() throws Exception {
		final Long ID = 13L;
		when(receiptDAO.insert(record)).thenReturn(true);
		when(receiptDAO.findOne(anyLong())).thenReturn(inserted);
		when(record.getId()).thenReturn(ID);
		
		boolean result = receiptService.create(receipt);
		
		verify(receiptDAO).insert(record);
		verify(receiptDAO).findOne(ID);
		verifyReferencesSet(inserted);
		verify(receipt).setRecord(inserted);
		verify(wrapper).close();
		
		assertThat(result, is(true));
	}
	
	@Test
	public void createShouldReturnFalseIfInsertionFailed() throws Exception {
		when(receiptDAO.insert(record)).thenReturn(false);
		
		boolean result = receiptService.create(receipt);
		
		verify(receiptDAO).insert(record);
		verify(receiptDAO, never()).findOne(anyLong());
		verify(wrapper).close();
		
		assertThat(result, is(false));
	}
	
	@Test
	public void getCountShouldReturnNumberOfRecords() {
		long expected = 477L;
		when(receiptDAO.getCount()).thenReturn(expected);
		
		assertThat(receiptService.getCount(), is(equalTo(expected)));
	}
	
	@Test
	public void findOneByRecordIdShouldReturnReceiptWithAllDependencies() {
		Long id = 963L;
		when(receiptDAO.findOne(id)).thenReturn(record);
		when(record.getId()).thenReturn(id);
		
		Receipt receipt = receiptService.findOne(id);
		verifyReferencesSet(record);
		verify(productService).findAllByReceiptId(id);
		verify(settingsService).getTaxCatList();
		
		assertThat(receipt.getRecord().getId(), is(equalTo(id)));
	}
	
	@Test
	public void getReceiptsListLimitShouldReturnListOfReceiptsWithAllDependencies() {
		int offset = 20;
		int limit = 2;
		List<ReceiptRecord> records = Arrays.asList(new ReceiptRecord(), new ReceiptRecord());
		when(receiptDAO.findAllByQuery(anyString(), any())).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(offset, limit);
		verifyListRefsSet(limit, result);
		
	}
	
	@Test
	public void getReceiptsListLimitShouldReturnEmptyListWhenNoRecordsFound() {
		int offset = 20;
		int limit = 2;
		List<ReceiptRecord> records = Collections.emptyList();
		when(receiptDAO.findAllByQuery(anyString(), any())).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(offset, limit);
		
		verifyListRefsSet(0, result);
	}
	
	@Test
	public void getReceiptsListByCashbosShouldReturnListOfReceiptsWithAllDependencies() {
		Long cashboxId = 8L;
		int listSize = 2;
		List<ReceiptRecord> records = Arrays.asList(new ReceiptRecord(), new ReceiptRecord());
		when(receiptDAO.findAllByQuery(anyString(), any())).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(cashboxId);
		
		verifyListRefsSet(listSize, result);
	}
	
	@Test
	public void getReceiptsListByCashboxShouldReturnEmptyListWhenNoRecordsFound() {
		Long cashboxId = 8L;
		int listSize = 0;
		List<ReceiptRecord> records = Collections.emptyList();
		when(receiptDAO.findAllByQuery(anyString(), any())).thenReturn(records);
		
		List<Receipt> result = receiptService.getReceiptList(cashboxId);
		
		verifyListRefsSet(listSize, result);
	}
	
	@Test
	public void cancelShouldUpdateRecordStatusAndReturnTrueOnExistingRecord() {
		Long receiptId = 88L;
		when(receiptDAO.findOne(receiptId)).thenReturn(record);
		when(record.getId()).thenReturn(receiptId);
		when(receiptDAO.update(record)).thenReturn(true);
		
		
		boolean result = receiptService.cancel(receiptId);
		verify(record).setCancelled(true);
		verify(receiptDAO).update(record);
		assertThat(result, is(true));
	}
	
	@Test
	public void updateShouldUpdateRecordAndAllRefsAndReturnTrue() {
		Long receiptId = 1000L;
		List<Product> products = Arrays.asList(new Product(), new Product());
		when(receiptDAO.findOne(receiptId)).thenReturn(record);
		when(record.getId()).thenReturn(receiptId);
		when(receiptDAO.update(record)).thenReturn(true);
		when(productDAO.findOne(anyLong())).thenReturn(null);
		when(receipt.getProducts()).thenReturn(products);
		
		boolean result = receiptService.update(receipt);
		verify(receiptDAO).update(record);
		verify(receiptProductDAO, times(2)).insert(any(ReceiptProduct.class));
		assertThat(result, is(true));
	}
	
	@Test
	public void updateShouldUpdateQuantityInStock() {
		Long receiptId = 1L;
		Product product = mock(Product.class);
		List<Product> products = Arrays.asList(product, product);
		when(product.getQuantity()).thenReturn(BigDecimal.ONE);
		when(receiptDAO.findOne(receiptId)).thenReturn(record);
		when(record.getId()).thenReturn(receiptId);
		when(receiptDAO.update(record)).thenReturn(true);
		when(productDAO.findOne(anyLong())).thenReturn(product);
		when(receipt.getProducts()).thenReturn(products);
		
		boolean result = receiptService.update(receipt);
		verify(receiptDAO).update(record);
		verify(productDAO, times(2)).update(product);
		assertThat(result, is(true));
	}
	
	private void verifyListRefsSet(int listSize, List<Receipt> result) {
		verify(productService, times(listSize)).findAllByReceiptId(anyLong());
		verify(settingsService, times(listSize)).getTaxCatList();
		assertThat(result.size(), is(equalTo(listSize)));
	}
	
	private void verifyReferencesSet(ReceiptRecord record) {
		verify(record).setCashbox(any());
		verify(record).setPaymentType(any());
		verify(record).setReceiptType(any());
		verify(record).setUserCreateBy(any());
		verify(record).setProducts(any());
	}
	
}