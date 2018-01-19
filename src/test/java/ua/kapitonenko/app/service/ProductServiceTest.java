package ua.kapitonenko.app.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.records.ProductLocale;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.fixtures.ProductMock;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.fixtures.TestServiceFactory;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	
	private ProductService productService;
	
	private Product product;
	
	private ServiceFactory appServiceFactory;
	private ModelFactory appModelFactory;
	
	@Mock
	private DAOFactory daoFactoryMock;
	@Mock
	private ConnectionWrapper wrapperMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private ProductDAO productDAO;
	@Mock
	private ProductLocaleDAO productLocaleDAO;
	@Mock
	private TaxCategoryDAO taxCategoryDAO;
	@Mock
	private UnitDAO unitDAO;
	@Mock
	private LocaleDAO localeDAO;
	@Mock
	private ProductRecord productRecord;
	
	@Before
	public void setUp() throws Exception {
		productService = Application.getServiceFactory().getProductService();
		productService.setDaoFactory(daoFactoryMock);
		when(daoFactoryMock.getConnection()).thenReturn(wrapperMock);
		when(wrapperMock.open()).thenReturn(connectionMock);
		when(daoFactoryMock.getProductDAO(connectionMock)).thenReturn(productDAO);
		when(daoFactoryMock.getProductLocaleDAO(connectionMock)).thenReturn(productLocaleDAO);
		when(daoFactoryMock.getTaxCategoryDAO(connectionMock)).thenReturn(taxCategoryDAO);
		when(daoFactoryMock.getUnitDAO(connectionMock)).thenReturn(unitDAO);
		when(daoFactoryMock.getLocaleDAO(connectionMock)).thenReturn(localeDAO);
		
		appServiceFactory = Application.getServiceFactory();
		Application.setServiceFactory(TestServiceFactory.getInstance());
		appModelFactory = Application.getModelFactory();
		Application.setModelFactory(new TestModelFactory());
		
		product = Application.getModelFactory().createProduct(productRecord);
	}
	
	@After
	public void tearDown() throws Exception {
		Application.setServiceFactory(appServiceFactory);
		Application.setModelFactory(appModelFactory);
	}
	
	@Test
	public void newProductShouldCreateNewProductWithRecordAndSupportedLang() throws Exception {
		Product product = productService.newProduct();
		assertThat(product.getRecord(), is(notNullValue()));
		assertThat(product.getNames().size(), is(greaterThanOrEqualTo(1)));
	}
	
	@Test
	public void createProductShouldInsertProductAndAllProductNames() throws Exception {
		int localesSize = 4;
		product.setNames(ModelUtils.generateProductLocaleList(localesSize));
		
		productService.createProduct(product);
		
		verify(productDAO).insert(productRecord);
		verify(productLocaleDAO, times(localesSize)).insert(any(ProductLocale.class));
		verify(wrapperMock).beginTransaction();
		verify(wrapperMock).commit();
		verify(wrapperMock).close();
	}
	
	@Test
	public void getProductsList() throws Exception {
	}
	
	@Test
	public void getProductsListShouldReturnListOfProductsWithAllDependencies() {
		int offset = 20;
		int limit = 2;
		when(productDAO.findAllByQuery(anyString(), any())).thenReturn(Arrays.asList(productRecord, productRecord));
		
		List<Product> products = productService.getProductsList(offset, limit, ModelUtils.anyId());
		assertThat(products.size(), is(equalTo(limit)));
		products.forEach(p -> ((ProductMock) p).verifyDependencies());
		
	}
	
	@Test
	public void getProductsListShouldReturnEmptyListWhenNoRecordsFound() {
		int offset = 20;
		int limit = 2;
		when(productDAO.findAllByQuery(anyString(), any())).thenReturn(Collections.emptyList());
		
		List<Product> products = productService.getProductsList(offset, limit, ModelUtils.anyId());
		assertThat(products.isEmpty(), is(true));
	}
	
	@Test
	public void findByIdOrNameShouldReturnListOfInitializedProducts() throws Exception {
		when(productDAO.findByIdOrName(anyLong(), anyLong(), anyString())).thenReturn(Arrays.asList(productRecord, productRecord));
		
		List<Product> products = productService.findByIdOrName(ModelUtils.anyId(), ModelUtils.anyId(), "");
		assertThat(products.size(), is(equalTo(2)));
		products.forEach(p -> ((ProductMock) p).verifyDependencies());
	}
	
	@Test
	public void findAllByReceiptIdShouldReturnListOfInitializedProducts() throws Exception {
		when(productDAO.findAllByReceiptId(anyLong())).thenReturn(Arrays.asList(productRecord, productRecord));
		
		List<Product> products = productService.findAllByReceiptId(ModelUtils.anyId());
		assertThat(products.size(), is(equalTo(2)));
		products.forEach(p -> ((ProductMock) p).verifyDependencies());
	}
	
	
	@Test
	public void getCountShouldReturnNoOfRecords() throws Exception {
		long expected = 16L;
		when(productDAO.getCount()).thenReturn(expected);
		assertThat(productService.getCount(), is(equalTo(expected)));
	}
	
	@Test
	public void deleteShouldDeleteRecord() throws Exception {
		Long recordId = 74L;
		when(productDAO.findOne(recordId)).thenReturn(productRecord);
		
		productService.delete(recordId, new Random().nextLong());
	}
	
	@Test
	public void updateShouldUpdateRecord() throws Exception {
		productService.update(product);
		
		verify(productDAO).update(productRecord);
	}
	
	@Test
	public void findOneShouldReturnProductWithAllDependencies() throws Exception {
		Long recordId = 40L;
		when(productDAO.findOne(recordId)).thenReturn(productRecord);
		
		ProductMock product = (ProductMock) productService.findOne(recordId);
		assertThat(product, is(notNullValue()));
		product.verifyDependencies();
	}
}