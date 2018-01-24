package ua.kapitonenko.app.controller.commands.receipt;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Basic implementation of {@code ActionCommand} used by commands, which process receipt form.
 * Uses TemplateMethod pattern.
 */
public abstract class ReceiptEditAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/**
	 * Can only process POST requests.
	 * Throws {@link MethodNotAllowedException} if request is not POST.
	 * Saves alert messages in request to session in order to transfer them between requests.
	 * Redirects to URI returned by method process() or back to previous action on null.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		Long localeId = request.getSession().getLocaleId();
		receipt.setLocalId(localeId);
		
		ValidationBuilder validator = request.getValidator();
		
		String action = process(receipt, request, validator);
		
		if (!validator.isValid()) {
			request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
			logger.warn("Receipt edit validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		
		return (action == null) ? request.goBack() : request.redirect(action);
	}
	
	/**
	 * TemplateMethod for providing concrete implementation of command.
	 * @param receipt Receipt instance
	 * @param request RequestWrapper
	 * @param validator ValidationBuilder instance
	 * @return String URI of next action
	 */
	protected abstract String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator);
}
