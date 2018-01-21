package ua.kapitonenko.app.controller.helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaginationHelperTest {
	private static final int RECORDS_PER_PAGE = Application.recordsPerPage();
	private static final int NO_OF_RECORDS = 256;
	private static final int EXPECT_NO_OF_PAGES = (int) Math.ceil(1.0 * NO_OF_RECORDS / RECORDS_PER_PAGE);
	private static final int EXPECT_LAST_PAGE_OFFSET = NO_OF_RECORDS - (NO_OF_RECORDS % RECORDS_PER_PAGE);
	
	private PaginationHelper pager;
	
	private ArgumentCaptor<String> attrName;
	private ArgumentCaptor<Object> attrValue;
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute(Keys.LOCALE)).thenReturn("");
		attrName = ArgumentCaptor.forClass(String.class);
		attrValue = ArgumentCaptor.forClass(Object.class);
	}
	
	@Test
	public void constructorShouldSetRequestWrapperAttr() throws Exception {
		Long page = 8L;
		long noOfRecords = 256;
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page.toString());
		
		pager = new PaginationHelper(new RequestWrapper(request), noOfRecords);
		
		verify(request, times(3)).setAttribute(attrName.capture(), attrValue.capture());
		
		assertThat(attrName.getAllValues().contains(Keys.CUR_PAGE), is(true));
		assertThat(attrName.getAllValues().contains(Keys.NO_PAGES), is(true));
		assertThat(attrValue.getAllValues().contains(page.intValue()), is(true));
		assertThat(attrValue.getAllValues().contains(EXPECT_NO_OF_PAGES), is(true));
	}
	
	@Test
	public void constructorShouldSetCurPageToMaxExistingPage() throws Exception {
		Long page = 80L;
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page.toString());
		
		pager = new PaginationHelper(new RequestWrapper(request), NO_OF_RECORDS);
		
		verify(request, times(3)).setAttribute(attrName.capture(), attrValue.capture());
		
		assertThat(attrValue.getAllValues().contains(page.intValue()), is(false));
		assertThat(attrValue.getAllValues().contains(EXPECT_NO_OF_PAGES), is(true));
	}
	
	@Test
	public void constructorShouldSetCurPageToOneOnZeroAndNegativePage() throws Exception {
		Long page = 0L;
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page.toString());
		
		pager = new PaginationHelper(new RequestWrapper(request), NO_OF_RECORDS);
		
		verify(request, times(3)).setAttribute(attrName.capture(), attrValue.capture());
		
		assertThat(attrValue.getAllValues().contains(1), is(true));
		assertThat(attrValue.getAllValues().contains(page), is(false));
		
	}
	
	@Test(expected = NotFoundException.class)
	public void constructorShouldThrowExceptionOnInvalidPageNo() throws Exception {
		String page = "four";
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page);
		
		pager = new PaginationHelper(new RequestWrapper(request), NO_OF_RECORDS);
	}
	
	@Test
	public void getOffsetForFirstPageShouldReturnZero() throws Exception {
		Long page = 1L;
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page.toString());
		
		pager = new PaginationHelper(new RequestWrapper(request), NO_OF_RECORDS);
		
		assertThat(pager.getOffset(), is(equalTo(0)));
	}
	
	@Test
	public void getOffsetForLastPage() throws Exception {
		Long page = 52L;
		
		when(request.getParameter(Keys.PAGE)).thenReturn(page.toString());
		
		pager = new PaginationHelper(new RequestWrapper(request), NO_OF_RECORDS);
		
		assertThat(pager.getOffset(), is(equalTo(EXPECT_LAST_PAGE_OFFSET)));
	}
}