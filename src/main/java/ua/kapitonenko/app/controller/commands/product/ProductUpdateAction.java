package ua.kapitonenko.app.controller.commands.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

/**
 * Implementation of {@code ActionCommand}.
 * Updates the quantity of products in stock.
 */
public class ProductUpdateAction implements ActionCommand {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	/**
	 * Updates the quantity of products in storage.
	 * Can only handle POST requests.
	 * Throws {@link MethodNotAllowedException} if request is not POST
	 * Throws {@link NotFoundException} if record with the given id was not found.
	 * Redirects back to previous action.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
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
			BigDecimal oldQuantity = product.getQuantity();
			product.setQuantity(quantityValue);
			productService.update(product);
			logger.info("Updated quantity of product id={} from {} to {}", productId, oldQuantity, quantityValue);
			
		} else {
			request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
			logger.warn("Product update validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		
		return request.goBack();
	}
}
