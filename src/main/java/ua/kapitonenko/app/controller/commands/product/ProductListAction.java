package ua.kapitonenko.app.controller.commands.product;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.PaginationHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.service.ProductService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of {@code ActionCommand}.
 * Gets the list of {@link Product}s from {@link ProductService} and displays them in list view.
 */
public class ProductListAction implements ActionCommand {
	
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	/**
	 * Gets the list of all {@link Product}s from {@link ProductService} and displays them in list view with pagination.
	 * Returns the URI of list view.
	 */
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
