package ua.kapitonenko.app.controller.commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.*;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class ReceiptCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReceiptCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		// TODO split into separate classes
		
		Receipt receipt = getCalculator(request);
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		
		if (request.isPost()) {
			String command = getCommand(request);
			
			setPayment(request, receipt);
			
			String route = Actions.RECEIPT_CREATE;
			
			switch (command) {
				case Keys.ADD:
					add(request, receipt, validator);
					break;
				case Keys.UPDATE:
					update(request, receipt, validator);
					break;
				case Keys.DELETE:
					delete(request, receipt);
					break;
				case Keys.SAVE:
					if (save(request, receipt, validator)) {
						route = Actions.RECEIPTS;
					}
					break;
				case Keys.CANCEL:
					if (cancel(request, receipt)) {
						route = Actions.RECEIPTS;
					}
					break;
			}
			if (!validator.isValid()) {
				request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
			}
			return request.redirect(route);
		}
		
		return request.forward(Pages.RECEIPT_FORM, Actions.RECEIPT_CREATE);
	}
	
	private void setPayment(RequestWrapper request, Receipt receipt) {
		String payment = request.getParameter(Keys.PAYMENT);
		Long paymentId = ValidationBuilder.parseId(payment);
		
		if (paymentId != null) {
			receipt.getRecord().setPaymentTypeId(paymentId);
		}
	}
	
	private String getCommand(RequestWrapper request) {
		String[] buttons = request.getParams().get(Keys.BUTTON);
		String command = Stream.of(buttons).filter(StringUtils::isNotEmpty).findFirst().orElse("");
		LOGGER.debug("command: " + command);
		return command;
	}
	
	private void add(RequestWrapper request, Receipt receipt, ValidationBuilder validator) {
		String quantity = request.getParameter(Keys.NEW_PRODUCT_QUANTITY);
		String product = request.getParameter(Keys.NEW_PRODUCT_ID);
		String name = request.getParameter(Keys.NEW_PRODUCT_NAME);
		
		Long productId = ValidationBuilder.parseId(product);
		BigDecimal quantityValue = validator.parseDecimal(quantity, 3, Keys.PRODUCT_QUANTITY);
		
		validator
				.required(quantityValue, Keys.PRODUCT_QUANTITY)
				.requiredOne(productId, Keys.PRODUCT_ID, name, Keys.PRODUCT_NAME);
		
		if (validator.isValid()) {
			List<Product> foundList = productService.findByIdOrName(request.getSession().getLocaleId(), productId, name.trim());
			
			validator.listSize(1, foundList, Keys.ERROR_SEARCH_FAIL, Keys.GUIDE_SPECIFY_REQUEST);
			
			if (validator.isValid()) {
				Product found = foundList.get(0);
				LOGGER.debug(quantityValue + " " + found.getQuantity());
				validator.notGreater(quantityValue, found.getQuantity(), Keys.ERROR_NOT_ENOUGH);
				
				if (validator.isValid()) {
					found.setQuantity(quantityValue);
					receipt.addProduct(found);
				}
			}
		}
	}
	
	private boolean cancel(RequestWrapper request, Receipt receipt) {
		
		if (receiptService.update(receipt)) {
			request.getSession().remove(Keys.RECEIPT);
			request.getSession().remove(Keys.PAYMENT_TYPES);
			return true;
		}
		return false;
	}
	
	private boolean save(RequestWrapper request, Receipt receipt, ValidationBuilder validator) {
		validator
				.idInList(receipt.getRecord().getPaymentTypeId(),
						(List<PaymentType>) request.getSession().get(Keys.PAYMENT_TYPES),
						Keys.PAYMENT)
				.required(receipt.getProducts(), Keys.ERROR_PRODUCT_LIST_NOT_EMPTY);
		
		if (validator.isValid()) {
			receipt.getRecord().setCancelled(false);
			if (receiptService.update(receipt)) {
				request.getSession().remove(Keys.RECEIPT);
				return true;
			}
		}
		return false;
	}
	
	private void delete(RequestWrapper request, Receipt receipt) {
		String toDelete = request.getParameter(Keys.PRODUCT_ID);
		Long deleteId = ValidationBuilder.parseId(toDelete);
		if (deleteId != null) {
			receipt.remove(deleteId);
		}
	}
	
	private void update(RequestWrapper request, Receipt receipt, ValidationBuilder validator) {
		String[] updatedQuantities = request.getParams().get(Keys.PRODUCT_QUANTITY);
		for (int i = 0; i < updatedQuantities.length; i++) {
			BigDecimal updated = validator.parseDecimal(updatedQuantities[i], 3, Keys.PRODUCT_QUANTITY);
			validator.requiredAll(updated, Keys.PRODUCT_QUANTITY);
			if (updated != null) {
				receipt.getProducts().get(i).setQuantity(updated);
			}
		}
	}
	
	private Receipt getCalculator(RequestWrapper request) {
		
		Long localeId = request.getSession().getLocaleId();
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		
		if (receipt == null) {
			LOGGER.debug("new receipt created");
			Cashbox cashbox = (Cashbox) request.getSession().get(Keys.CASHBOX);
			User user = request.getSession().getUser();
			ReceiptRecord record = new ReceiptRecord(null,
					                                        cashbox.getId(),
					                                        Application.getId(Application.PAYMENT_TYPE_UNDEFINED),
					                                        Application.getId(Application.RECEIPT_TYPE_FISCAL),
					                                        true, user.getId());
			
			receipt = new Receipt(record);
			receipt.setCategories(settingsService.getTaxCatList());
			
			receiptService.create(receipt);
			request.getSession().set(Keys.RECEIPT, receipt);
			request.getSession().set(Keys.PAYMENT_TYPES, settingsService.getPaymentTypes());
		}
		
		receipt.setLocalId(localeId);
		return receipt;
	}
}
