package ua.kapitonenko.app.controller.commands.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Implementation of {@code ActionCommand}.
 * Deletes the product from storage.
 */
public class ProductDeleteAction implements ActionCommand {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	/**
	 * Deletes the product from storage.
	 * Can only handle POST requests.
	 * Throws {@link MethodNotAllowedException} if request is not POST
	 * Throws {@link NotFoundException} if record with the given id was not found.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		
		String id = request.getParameter("id");
		Long productId = ValidationBuilder.parseId(id);
		
		if (productId == null || !productService.delete(productId, request.getSession().getUser().getId())) {
			throw new NotFoundException();
		}
		logger.info("Product deleted: id={}", productId);
		return request.goBack();
	}
}
