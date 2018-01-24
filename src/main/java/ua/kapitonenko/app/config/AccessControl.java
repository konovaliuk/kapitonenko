package ua.kapitonenko.app.config;

import ua.kapitonenko.app.config.keys.Actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.kapitonenko.app.config.Application.Ids.*;

/**
 * {@code AccessControl} provides information on user's permissions to perform actions
 * base on their role.
 */
public class AccessControl {
	
	private AccessControl() {
	}
	
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
	
	/**
	 * Performs check if user has permission to perform action associated with provided URI.
	 *
	 * @param roleId Long id of user's {@link ua.kapitonenko.app.persistence.records.UserRole} record id
	 * @param action String URI
	 * @return true if action is allowed, otherwise false
	 */
	public static boolean allowed(Long roleId, String action) {
		return access.get(roleId).contains(action);
	}
	
	/**
	 * Performs check if guest has permission to perform action associated with provided URI.
	 * @param action String URI
	 * @return true if guest has permissions, otherwise false
	 */
	public static boolean guestAllowed(String action) {
		return access.get(ROLE_GUEST.getValue()).contains(action);
	}
}
