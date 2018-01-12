package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;

public class ProductUpdateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductUpdateAction.class);
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		ValidationBuilder validator = request.getValidator();
		
		String id = request.getParameter("id");
		String quantity = request.getParameter(Keys.PRODUCT_QUANTITY);
		
		Long productId = ValidationBuilder.parseId(id);
		BigDecimal quantityValue = validator.parseDecimal(quantity, 3, Keys.PRODUCT_QUANTITY);
		
		validator
				.required(productId, Keys.PRODUCT_ID)
				.required(quantityValue, Keys.PRODUCT_QUANTITY)
				.ifValid()
				.notLess(quantityValue, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		
		if (validator.isValid()) {
			Product product = productService.findOne(productId);
			product.setQuantity(quantityValue);
			productService.update(product);
			
		} else {
			request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
		}
		
		return request.goBack();
	}
}
