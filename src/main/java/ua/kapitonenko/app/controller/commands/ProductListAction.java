package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.PaginationHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ProductListAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ProductListAction.class);
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		long noOfRecords = productService.getCount();
		PaginationHelper pager = new PaginationHelper(request, noOfRecords);
		
		List<Product> list = productService.getProductsList(pager.getOffset(),
				pager.getRecordsPerPage(), request.getSession().getLocaleId());
		request.setAttribute(Keys.PRODUCTS, list);
		
		return request.forward(Pages.PRODUCT_LIST, Actions.PRODUCTS);
	}
}
