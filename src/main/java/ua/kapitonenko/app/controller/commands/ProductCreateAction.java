package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.records.*;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		// TODO split to smaller methods
		
		List<TaxCategory> taxes = settingsService.getTaxCatList();
		List<Unit> units = settingsService.getUnitList();
		
		Product product = new Product();
		List<ProductLocale> names = new ArrayList<>();
		List<LocaleRecord> locales = settingsService.getLocaleList();
		
		if (!request.isPost()) {
			for (int i = 0; i < locales.size(); i++) {
				names.add(new ProductLocale(product, locales.get(i), Keys.PRODUCT_NAME, null));
			}
			product.setNames(names);
		}
		
		if (request.isPost()) {
			String[] lang = request.getParams().get(Keys.PRODUCT_NAME);
			String quantity = request.getParameter(Keys.PRODUCT_QUANTITY);
			String unit = request.getParameter(Keys.PRODUCT_UNIT);
			String price = request.getParameter(Keys.PRODUCT_PRICE);
			String tax = request.getParameter(Keys.PRODUCT_TAX);
			
			
			ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
			
			Long unitId = ValidationBuilder.parseId(unit);
			Long taxId = ValidationBuilder.parseId(tax);
			BigDecimal priceValue = validator.parseDecimal(price, 2, Keys.PRODUCT_PRICE);
			BigDecimal quantityValue = validator.parseDecimal(quantity, 3, Keys.PRODUCT_QUANTITY);
			
			validator
					.required(lang, Keys.PRODUCT_NAME)
					.required(quantityValue, Keys.PRODUCT_QUANTITY)
					.required(priceValue, Keys.PRODUCT_PRICE)
					.idInList(unitId, units, Keys.PRODUCT_UNIT)
					.idInList(taxId, taxes, Keys.PRODUCT_TAX)
					.ifValid()
					.notLess(quantityValue, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY)
					.isValid();
			
			product.setQuantity(quantityValue);
			product.setPrice(priceValue);
			product.setTaxCategoryId(taxId);
			product.setUnitId(unitId);
			
			for (int i = 0; i < locales.size(); i++) {
				names.add(new ProductLocale(product, locales.get(i), Keys.PRODUCT_NAME, lang[i]));
			}
			product.setNames(names);
			product.setCreatedBy(request.getSession().getUser().getId());
			
			if (validator.isValid()) {
				productService.createProduct(product);
				return request.redirect(Actions.PRODUCTS);
			}
		}
		
		request.setAttribute(Keys.PRODUCT, product);
		request.setAttribute(Keys.TAX_CAT_LIST, taxes);
		request.setAttribute(Keys.UNIT_LIST, units);
		
		return request.forward(Pages.PRODUCT_FORM, Actions.PRODUCTS_CREATE);
	}
}
