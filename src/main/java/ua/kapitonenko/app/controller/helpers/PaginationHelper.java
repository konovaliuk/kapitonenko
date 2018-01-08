package ua.kapitonenko.app.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.exceptions.NotFoundException;

public class PaginationHelper {
	private static final Logger LOGGER = Logger.getLogger(PaginationHelper.class);
	
	public static final int DEFAULT_RECORDS_PER_PAGE = 5;
	
	private RequestWrapper request;
	private int currentPage = 1;
	
	public PaginationHelper(RequestWrapper request, long noOfRecords) {
		this.request = request;
		init(noOfRecords);
	}
	
	public void init(long noOfRecords) {
		int noOfPages = (int) Math.ceil((double) noOfRecords / getRecordsPerPage());
		
		String page = request.getParameter(Keys.PAGE);
		try {
			if (page != null) {
				currentPage = Integer.parseInt(page);
			}
			
			if (currentPage > 1 && currentPage > noOfPages) {
				currentPage = noOfPages;
			}
			
		} catch (Exception e) {
			LOGGER.debug(e);
			throw new NotFoundException(request.getUri());
		}
		
		request.setAttribute(Keys.CUR_PAGE, currentPage);
		request.setAttribute(Keys.NO_PAGES, noOfPages);
	}
	
	public int getRecordsPerPage() {
		return (Application.recordsPerPage() > 0)
				       ? Application.recordsPerPage()
				       : DEFAULT_RECORDS_PER_PAGE;
	}
	
	public int getOffset() {
		return (currentPage - 1) * getRecordsPerPage();
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	
}
