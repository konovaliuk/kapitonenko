package ua.kapitonenko.app.config;

import ua.kapitonenko.app.config.keys.Actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.kapitonenko.app.config.Application.Ids.*;

public class AccessControl {
	private static Map<Long, List<String>> access = new HashMap<>();
	
	static {
		access.put(
				ROLE_GUEST.getValue(), Arrays.asList(
						Actions.LOGIN,
						Actions.SIGNUP,
						Actions.LANGUAGE,
						Actions.HOME));
		
		access.put(
				ROLE_CASHIER.getValue(), Arrays.asList(
						Actions.LANGUAGE,
						Actions.PRODUCTS,
						Actions.RECEIPTS,
						Actions.RECEIPT_CREATE,
						Actions.RECEIPT_CANCEL,
						
						Actions.RECEIPT_EDIT_CANCEL,
						Actions.RECEIPT_EDIT_DELETE,
						Actions.RECEIPT_EDIT_SAVE,
						Actions.RECEIPT_EDIT_UPDATE,
						Actions.RECEIPT_EDIT_ADD,
						
						Actions.LOGOUT,
						Actions.HOME));
		
		access.put(
				ROLE_MERCHANDISER.getValue(), Arrays.asList(
						Actions.LANGUAGE,
						Actions.PRODUCTS,
						Actions.PRODUCTS_UPDATE,
						Actions.PRODUCTS_DELETE,
						Actions.PRODUCTS_CREATE,
						Actions.LOGOUT,
						Actions.HOME));
		
		access.put(
				ROLE_SENIOR.getValue(), Arrays.asList(
						Actions.LANGUAGE,
						Actions.REPORTS,
						Actions.REPORT_CREATE,
						Actions.REPORT_VIEW,
						Actions.RECEIPT_CANCEL,
						Actions.RECEIPT_RETURN,
						Actions.RECEIPTS,
						
						Actions.RECEIPT_EDIT_CANCEL,
						Actions.RECEIPT_EDIT_DELETE,
						Actions.RECEIPT_EDIT_SAVE,
						Actions.RECEIPT_EDIT_UPDATE,
						Actions.RECEIPT_EDIT_ADD,
						
						Actions.LOGOUT,
						Actions.HOME));
	}
	
	public static boolean allowed(Long roleId, String action) {
		return access.get(roleId).contains(action);
	}
	
	public static boolean guestAllowed(String action) {
		return access.get(ROLE_GUEST.getValue()).contains(action);
	}
}
