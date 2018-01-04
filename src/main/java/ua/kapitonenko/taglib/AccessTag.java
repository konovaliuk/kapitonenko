package ua.kapitonenko.taglib;

import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.domain.entities.User;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AccessTag extends TagSupport {
	private String route;
	
	public void setRoute(String route) {
		this.route = route;
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		User user = (User) session.getAttribute(Keys.USER);
		
		if ((user == null && !Application.guestAllowed(route)) ||
				    (user != null && Application.allowed(user.getUserRoleId(), route))) {
			return EVAL_BODY_INCLUDE;
		}
		
		return SKIP_BODY;
	}
}
