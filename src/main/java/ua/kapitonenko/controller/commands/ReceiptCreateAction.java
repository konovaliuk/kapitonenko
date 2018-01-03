package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.entities.*;
import ua.kapitonenko.service.ProductService;
import ua.kapitonenko.service.ReceiptService;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static ua.kapitonenko.controller.keys.Keys.*;

public class ReceiptCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReceiptCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	private ProductService productService = Application.getServiceFactory().getProductService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		LOGGER.debug(request.paramsToString());
		
		ReceiptCalculator calculator = (ReceiptCalculator) request.getSession().get(R_CALCULATOR);
		
		List<PaymentType> paymentTypes = settingsService.getPaymentTypes();
		Long localeId = request.getSession().getLocaleId();
		
		if (calculator == null) {
			LOGGER.debug("new calculator created");
			Cashbox cashbox = (Cashbox) request.getSession().get(CASHBOX);
			User user = request.getSession().getUser();
			Receipt receipt = new Receipt(null,
					                             cashbox.getId(),
					                             Application.getId(Application.PAYMENT_TYPE_UNDEFINED),
					                             Application.getId(Application.RECEIPT_TYPE_FISCAL),
					                             true, user.getId());
			
			calculator = new ReceiptCalculator();
			calculator.setReceipt(receipt);
			calculator.setCategories(settingsService.getTaxCatList());
			calculator.setPaymentTypes(paymentTypes);
			
			receiptService.create(calculator);
			request.getSession().set(R_CALCULATOR, calculator);
		}
		
		calculator.setLocalId(localeId);
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		
		if (request.isPost()) {
			
			String command = request.getParameter(BUTTON);
			String quantity = request.getParameter(NEW_PRODUCT_QUANTITY);
			String product = request.getParameter(NEW_PRODUCT_ID);
			String name = request.getParameter(NEW_PRODUCT_NAME);
			String payment = request.getParameter(PAYMENT);
			Long productId = ValidationBuilder.parseId(product);
			BigDecimal quantityValue = validator.parseDecimal(quantity, 3, PRODUCT_QUANTITY);
			Long paymentId = ValidationBuilder.parseId(payment);
			
			if (paymentId != null) {
				calculator.getReceipt().setPaymentTypeId(paymentId);
			}
			
			if (command.equals(ADD)) {
				
				boolean valid = validator
						                .required(quantityValue, PRODUCT_QUANTITY)
						                .requiredOne(productId, PRODUCT_ID, name, PRODUCT_NAME)
						                //.idInList(paymentId, paymentTypes, Keys.PAYMENT)
						                .isValid();
				
				if (valid) {
					List<Product> foundList = productService.findByIdOrName(localeId, productId, name);
					Product foundProduct = null;
					
					if (foundList == null || foundList.size() == 0) {
						// err not found
					} else if (foundList.size() > 1) {
						// err specify the request
					} else {
						foundProduct = foundList.get(0);
						
						if (foundProduct.getQuantity().compareTo(quantityValue) < 0) {
							// err not enough
						}
					}
					
					if (valid && foundProduct != null) {
						foundProduct.setQuantity(quantityValue);
						calculator.addProduct(foundProduct);
						
					}
				}
				
			} else if (command.equals(Keys.UPDATE)) {
				String[] updatedQuantities = request.getParams().get(PRODUCT_QUANTITY);
				for (int i = 0; i < updatedQuantities.length; i++) {
					BigDecimal updated = validator.parseDecimal(updatedQuantities[i], 3, PRODUCT_QUANTITY);
					validator.requiredAll(updated, PRODUCT_QUANTITY);
					if (updated != null) {
						calculator.getProducts().get(i).setQuantity(updated);
					}
				}
				
			} else if (command.equals(Keys.DELETE)) {
				String toDelete = request.getParameter(PRODUCT_ID);
				Long deleteId = ValidationBuilder.parseId(toDelete);
				if (deleteId != null) {
					calculator.remove(deleteId);
				}
			} else if (command.equals(Keys.SAVE)) {
				LOGGER.debug("befor save validator is valid " + validator.isValid());
				boolean valid = validator
						                .idInList(paymentId, paymentTypes, Keys.PAYMENT)
						                .required(calculator.getProducts(), Keys.ERROR_PRODUCT_LIST_NOT_EMPTY)
						                .isValid();
				if (valid) {
					LOGGER.debug(valid);
					calculator.getReceipt().setCancelled(false);
					if (receiptService.update(calculator)) {
						request.getSession().remove(R_CALCULATOR);
						return request.goHome();
					}
					
				}
			} else if (command.equals(Keys.CANCEL)) {
				if (receiptService.update(calculator)) {
					request.getSession().remove(R_CALCULATOR);
					return request.goHome();
				}
			}
		}
		
		return request.forward(Pages.RECEIPT_FORM, Routes.RECEIPT_CREATE);
	}
}

/*		Long receiptTypeId = Validator.parseId(request.getParameter(Keys.TYPE));
		if (ReceiptType.Supported.valueOf(5L) == null){
			LOGGER.debug("no value");
		}*/