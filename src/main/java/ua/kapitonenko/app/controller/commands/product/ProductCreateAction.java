package ua.kapitonenko.app.controller.commands.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.dao.records.Unit;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;

public class ProductCreateAction implements ActionCommand {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		List<TaxCategory> taxes = settingsService.getTaxCatList();
		List<Unit> units = settingsService.getUnitList();
		
		Product product = productService.newProduct();
		
		if (request.isPost()) {
			String[] lang = request.getParams().get(Keys.PRODUCT_NAME);
			String quantity = request.getParameter(Keys.PRODUCT_QUANTITY);
			String unit = request.getParameter(Keys.PRODUCT_UNIT);
			String price = request.getParameter(Keys.PRODUCT_PRICE);
			String tax = request.getParameter(Keys.PRODUCT_TAX);
			
			
			ValidationBuilder validator = request.getValidator();
			
			Long unitId = ValidationBuilder.parseId(unit);
			Long taxId = ValidationBuilder.parseId(tax);
			BigDecimal priceValue = validator.parseDecimal(price, 2, Keys.PRODUCT_PRICE);
			BigDecimal quantityValue = validator.parseDecimal(quantity, 3, Keys.PRODUCT_QUANTITY);
			
			validator
					.requiredAllLang(lang, Keys.PRODUCT_NAME)
					.required(quantityValue, Keys.PRODUCT_QUANTITY)
					.required(priceValue, Keys.PRODUCT_PRICE)
					.idInList(unitId, units, Keys.PRODUCT_UNIT)
					.idInList(taxId, taxes, Keys.PRODUCT_TAX)
					.ifValid()
					.notLess(quantityValue, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
			
			product.setQuantity(quantityValue);
			product.setPrice(priceValue);
			product.setTaxCategoryId(taxId);
			product.setUnitId(unitId);
			product.fillNames(lang);
			product.setCreatedBy(request.getSession().getUser().getId());
			
			if (validator.isValid()) {
				Product created = productService.createProduct(product);
				logger.info("New product created: {}", created);
				return request.redirect(Actions.PRODUCTS);
			}
			logger.warn("Product create validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		
		request.setAttribute(Keys.PRODUCT, product);
		request.setAttribute(Keys.TAX_CAT_LIST, taxes);
		request.setAttribute(Keys.UNIT_LIST, units);
		
		return request.forward(Pages.PRODUCT_FORM, Actions.PRODUCTS_CREATE);
	}
}
