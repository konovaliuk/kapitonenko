package ua.kapitonenko.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.exceptions.NotFoundException;

import static ua.kapitonenko.config.keys.Keys.CUR_PAGE;
import static ua.kapitonenko.config.keys.Keys.NO_PAGES;

public class PaginationHelper {
	private static final Logger LOGGER = Logger.getLogger(PaginationHelper.class);
	
	public static final int DEFAULT_RECORDS_PER_PAGE = 5;
	
	private RequestWrapper request;
	private int currentPage = 1;
	
	public PaginationHelper(RequestWrapper request, int noOfRecords) {
		this.request = request;
		init(noOfRecords);
	}
	
	public void init(int noOfRecords) {
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
		
		request.setAttribute(CUR_PAGE, currentPage);
		request.setAttribute(NO_PAGES, noOfPages);
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
