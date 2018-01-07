package ua.kapitonenko.app.taglib;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.records.User;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AccessTag extends TagSupport {
	private static final Logger LOGGER = Logger.getLogger(AccessTag.class);
	
	private String action;
	
	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		User user = (User) session.getAttribute(Keys.USER);
		
		if (user != null && !Application.allowed(user.getUserRoleId(), action)) {
			LOGGER.debug(user.getUserRoleId() + " " + action);
			return SKIP_BODY;
		}
		
		return EVAL_BODY_INCLUDE;
	}
}
