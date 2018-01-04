package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.exceptions.MethodNotAllowedException;
import ua.kapitonenko.exceptions.NotFoundException;
import ua.kapitonenko.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;

public class ProductDeleteAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductDeleteAction.class);
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		String id = request.getParameter("id");
		Long productId = ValidationBuilder.parseId(id);
		
		if (productId == null || !productService.delete(productId, request.getSession().getUser().getId())) {
			throw new NotFoundException(request.getUri());
		}
		
		return request.goBack();
	}
}
