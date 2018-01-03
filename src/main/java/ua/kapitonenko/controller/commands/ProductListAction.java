package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.PaginationHelper;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.domain.entities.Product;
import ua.kapitonenko.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ProductListAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(ProductListAction.class);
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		int noOfRecords = productService.getProductsCount();
		PaginationHelper pager = new PaginationHelper(request, noOfRecords);
		
		List<Product> list = productService.getProductsList(pager.getOffset(), pager.getRecordsPerPage());
		request.setAttribute(Keys.PRODUCTS, list);
		
		return request.forward(Pages.PRODUCT_LIST, Routes.PRODUCTS);
	}
}
