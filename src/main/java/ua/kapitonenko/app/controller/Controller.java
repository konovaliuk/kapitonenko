package ua.kapitonenko.app.controller;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.exceptions.ForbiddenException;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * {@code Controller} is an implementation of {@code HttpServlet} interface.
 * Performs the Controller role in MVC Pattern implementation.
 */
public class Controller extends HttpServlet {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private RequestHelper requestHelper = RequestHelper.getInstance();
	
	/**
	 * Initializes the application.
	 *
	 * @throws ServletException
	 */
	@Override
	public void init() throws ServletException {
		Application.init(getServletContext());
	}
	
	/**
	 * Delegates request processing to processRequest method.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Delegates request processing to processRequest method.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Wraps {@code HttpServletRequest} in {@link RequestWrapper} object.
	 * Uses {@link RequestHelper} object to map request URI to {@link ActionCommand} implementation instance.
	 * Using information from executing command selects request destination
	 * or sends a temporary redirect response to the client.
	 * On exceptions sends an error response to the client.
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		RequestWrapper requestWrapper = new RequestWrapper(request);
		logger.info("Request: {}", requestWrapper);
		
		try {
			ActionCommand command = requestHelper.getCommand(requestWrapper);
			logger.info("Executing the command: {}", command);
			ResponseParams result = command.execute(requestWrapper);
			
			if (result.isRedirect()) {
				logger.info("Redirecting to: {}", result.getUri());
				response.sendRedirect(result.getUri());
			} else {
				logger.info("Forwarding to: {}", result.getUri());
				dispatch(request, response, result.getUri());
			}
			
		} catch (NotFoundException e) {
			logger.error("Not found: {}", requestWrapper.getUri(), e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (MethodNotAllowedException e) {
			logger.error("Method not allowed: {} {}", requestWrapper.getMethod(), requestWrapper.getUri(), e);
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
		} catch (ForbiddenException e) {
			logger.error("Forbidden: {} {} roleId={}",
					requestWrapper.getMethod(), requestWrapper.getUri(), requestWrapper.getSession().getUser().getUserRoleId(), e);
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Keys.ERROR_INTERNAL);
		}
	}
	
	private void dispatch(HttpServletRequest request, HttpServletResponse response, String page)
			throws javax.servlet.ServletException, java.io.IOException {
		RequestDispatcher dispatcher =
				getServletContext().getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}
}
