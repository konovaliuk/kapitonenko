package ua.kapitonenko.app.controller.helpers;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	public boolean isRedirect() {
		return redirect;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ResponseParams that = (ResponseParams) o;
		
		return new EqualsBuilder()
				       .append(redirect, that.redirect)
				       .append(uri, that.uri)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(uri)
				       .append(redirect)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ResponseParams{")
				       .append("uri=").append(uri)
				       .append(", redirect=").append(redirect)
				       .append("}")
				       .toString();
	}
}
