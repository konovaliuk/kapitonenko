package ua.kapitonenko.app.taglib;

import ua.kapitonenko.app.config.AccessControl;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.persistence.records.User;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * {@code AccessTag} performs check user's permissions to perform action.
 * Renders element if action allowed, otherwise skips rendering.
 */
public class AccessTag extends TagSupport {
	
	private String action;
	
	/**
	 * Sets the URI of action to be checked.
	 *
	 * @param action String the URI
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		User user = (User) session.getAttribute(Keys.USER);
		
		if (user != null && !AccessControl.allowed(user.getUserRoleId(), action)) {
			return SKIP_BODY;
		}
		
		return EVAL_BODY_INCLUDE;
	}
}
