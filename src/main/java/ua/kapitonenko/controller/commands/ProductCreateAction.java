package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.domain.entities.*;
import ua.kapitonenko.service.ProductService;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ua.kapitonenko.config.keys.Keys.*;

public class ProductCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		LOGGER.debug(request.paramsToString());
		
		List<TaxCategory> taxes = settingsService.getTaxCatList();
		List<Unit> units = settingsService.getUnitList();
		
		Product product = new Product();
		List<ProductLocale> names = new ArrayList<>();
		List<Locale> locales = settingsService.getLocaleList();
		
		if (!request.isPost()) {
			for (int i = 0; i < locales.size(); i++) {
				names.add(new ProductLocale(product, locales.get(i), PRODUCT_NAME, null));
			}
			product.setNames(names);
		}
		
		if (request.isPost()) {
			String[] lang = request.getParams().get(PRODUCT_NAME);
			String quantity = request.getParameter(PRODUCT_QUANTITY);
			String unit = request.getParameter(PRODUCT_UNIT);
			String price = request.getParameter(PRODUCT_PRICE);
			String tax = request.getParameter(PRODUCT_TAX);
			
			
			ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
			
			Long unitId = ValidationBuilder.parseId(unit);
			Long taxId = ValidationBuilder.parseId(tax);
			BigDecimal priceValue = validator.parseDecimal(price, 2, PRODUCT_PRICE);
			BigDecimal quantityValue = validator.parseDecimal(quantity, 3, PRODUCT_QUANTITY);
			
			boolean valid = validator
									.required(lang, PRODUCT_NAME)
					                .required(quantityValue, PRODUCT_QUANTITY)
					                .required(priceValue, PRODUCT_PRICE)
					                .idInList(unitId, units, PRODUCT_UNIT)
									.idInList(taxId, taxes, PRODUCT_TAX)
					                .isValid();

			
			product.setQuantity(quantityValue);
			product.setPrice(priceValue);
			product.setTaxCategoryId(taxId);
			product.setUnitId(unitId);
			
			for (int i = 0; i < locales.size(); i++) {
				names.add(new ProductLocale(product, locales.get(i), PRODUCT_NAME, lang[i]));
			}
			product.setNames(names);
			product.setCreatedBy(request.getSession().getUser().getId());
			
			if (valid) {
				productService.createProduct(product);
				return request.redirect(Routes.PRODUCTS);
			}
		}

		request.setAttribute(PRODUCT, product);
		request.setAttribute(TAX_CAT_LIST, taxes);
		request.setAttribute(UNIT_LIST, units);
		
		return request.forward(Pages.PRODUCT_FORM, Routes.PRODUCTS_CREATE);
	}
}
