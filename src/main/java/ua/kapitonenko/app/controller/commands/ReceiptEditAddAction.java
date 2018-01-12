package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

public class ReceiptEditAddAction extends ReceiptEditAction {
	private static final Logger LOGGER = Logger.getLogger(ReceiptEditAddAction.class);
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		String quantity = request.getParameter(Keys.NEW_PRODUCT_QUANTITY);
		String product = request.getParameter(Keys.NEW_PRODUCT_ID);
		String name = request.getParameter(Keys.NEW_PRODUCT_NAME);
		
		Long productId = ValidationBuilder.parseId(product);
		BigDecimal quantityValue = validator.parseDecimal(quantity, 3, Keys.PRODUCT_QUANTITY);
		
		validator
				.required(quantityValue, Keys.PRODUCT_QUANTITY)
				.requiredOne(productId, Keys.PRODUCT_ID, name, Keys.PRODUCT_NAME);
		
		if (validator.isValid()) {
			if (name != null) {
				name = name.trim();
			}
			
			List<Product> foundList = productService.findByIdOrName(request.getSession().getLocaleId(), productId, name);
			
			validator.listSize(1, foundList, Keys.ERROR_SEARCH_FAIL, Keys.GUIDE_SPECIFY_REQUEST);
			
			if (validator.isValid()) {
				Product found = foundList.get(0);
				LOGGER.debug(quantityValue + " " + found.getQuantity());
				
				Long typeId = receipt.getRecord().getReceiptTypeId();
				
				if (typeId.equals(Application.Ids.RECEIPT_TYPE_FISCAL.getValue())) {
					validator.notGreater(quantityValue, found.getQuantity(), Keys.ERROR_NOT_ENOUGH);
				}
				
				if (validator.isValid()) {
					found.setQuantity(quantityValue);
					receipt.addProduct(found);
				}
			}
		}
		return null;
	}
}
