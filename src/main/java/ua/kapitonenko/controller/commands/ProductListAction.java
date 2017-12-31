package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.Product;
import ua.kapitonenko.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ProductListAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductListAction.class);
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		List<Product> list = productService.getProductsList();
		request.setAttribute(Keys.PRODUCTS, list);
		request.setAttribute(Keys.ACTION, Routes.PRODUCTS);
		return request.forward(Pages.PRODUCT_LIST);
	}
}
