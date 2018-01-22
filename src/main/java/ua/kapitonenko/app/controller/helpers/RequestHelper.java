package ua.kapitonenko.app.controller.helpers;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.AccessControl;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.commands.HomeAction;
import ua.kapitonenko.app.controller.commands.LanguageAction;
import ua.kapitonenko.app.controller.commands.product.ProductCreateAction;
import ua.kapitonenko.app.controller.commands.product.ProductDeleteAction;
import ua.kapitonenko.app.controller.commands.product.ProductListAction;
import ua.kapitonenko.app.controller.commands.product.ProductUpdateAction;
import ua.kapitonenko.app.controller.commands.receipt.*;
import ua.kapitonenko.app.controller.commands.report.ReportCreateAction;
import ua.kapitonenko.app.controller.commands.report.ReportListAction;
import ua.kapitonenko.app.controller.commands.report.ReportViewAction;
import ua.kapitonenko.app.controller.commands.user.LoginAction;
import ua.kapitonenko.app.controller.commands.user.LogoutAction;
import ua.kapitonenko.app.controller.commands.user.SignUpAction;
import ua.kapitonenko.app.exceptions.ForbiddenException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.persistence.records.User;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;

public class RequestHelper {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static RequestHelper instance = new RequestHelper();
	private HashMap<String, ActionCommand> commands = new HashMap<>();
	
	private RequestHelper() {
		initCommands();
	}
	
	public static RequestHelper getInstance() {
		return instance;
	}
	
	private void initCommands() {
		commands.put(Actions.LOGIN, new LoginAction());
		commands.put(Actions.LOGOUT, new LogoutAction());
		commands.put(Actions.HOME, new HomeAction());
		commands.put(Actions.SIGNUP, new SignUpAction());
		commands.put(Actions.LANGUAGE, new LanguageAction());
		commands.put(Actions.PRODUCTS, new ProductListAction());
		commands.put(Actions.PRODUCTS_CREATE, new ProductCreateAction());
		commands.put(Actions.PRODUCTS_UPDATE, new ProductUpdateAction());
		commands.put(Actions.PRODUCTS_DELETE, new ProductDeleteAction());
		
		commands.put(Actions.RECEIPT_CREATE, new ReceiptCreateAction());
		commands.put(Actions.RECEIPT_RETURN, new ReceiptReturnAction());
		commands.put(Actions.RECEIPT_CANCEL, new ReceiptCancelAction());
		commands.put(Actions.RECEIPTS, new ReceiptListAction());
		
		commands.put(Actions.RECEIPT_EDIT_UPDATE, new ReceiptEditUpdateAction());
		commands.put(Actions.RECEIPT_EDIT_DELETE, new ReceiptEditDeleteAction());
		commands.put(Actions.RECEIPT_EDIT_CANCEL, new ReceiptEditCancelAction());
		commands.put(Actions.RECEIPT_EDIT_SAVE, new ReceiptEditSaveAction());
		commands.put(Actions.RECEIPT_EDIT_ADD, new ReceiptEditAddAction());
		
		commands.put(Actions.REPORT_CREATE, new ReportCreateAction());
		commands.put(Actions.REPORTS, new ReportListAction());
		commands.put(Actions.REPORT_VIEW, new ReportViewAction());
		
	}
	
	public ActionCommand getCommand(RequestWrapper request) throws IOException, ServletException {
		String key = request.getUri();
		
		ActionCommand command = commands.get(key);
		
		if (command == null) {
			throw new NotFoundException();
		}
		User user = request.getSession().getUser();
		
		if (user == null && !AccessControl.guestAllowed(key)) {
			return commands.get(Actions.HOME);
		}
		
		if (user != null && !AccessControl.allowed(user.getUserRoleId(), key)) {
			throw new ForbiddenException();
		}
		
		return command;
	}
	
}
