package ua.kapitonenko.app.controller.helpers;

public class ResponseParams {
	private String uri;
	private boolean redirect;
	
	public ResponseParams(String uri, boolean redirect) {
		this.uri = uri;
		this.redirect = redirect;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public boolean isRedirect() {
		return redirect;
	}
	
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
}
