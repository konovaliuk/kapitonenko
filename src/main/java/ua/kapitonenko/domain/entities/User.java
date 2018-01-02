package ua.kapitonenko.domain.entities;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

public class User extends BaseEntity {
	
	private Long userRoleId;
	private String username;
	private String passwordHash;
	private boolean active;
	private Timestamp createdAt;
	private Timestamp deletedAt;
	
	public User() {
	}
	
	public User(Long userRoleId, String username, String password) {
		this.userRoleId = userRoleId;
		this.username = username;
		setPassword(password);
	}
	
	public void setPassword(String password) {
		generatePasswordHash(password);
	}
	
	public Long getUserRoleId() {
		return this.userRoleId;
	}
	
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasswordHash() {
		return this.passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public Timestamp getCreatedAt() {
		return this.createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Timestamp getDeletedAt() {
		return this.deletedAt;
	}
	
	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public void generatePasswordHash(String password) {
		passwordHash = DigestUtils.sha256Hex(password);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		User user = (User) o;
		
		return new EqualsBuilder()
				       .append(getId(), user.getId())
				       .append(userRoleId, user.userRoleId)
				       .append(username, user.username)
				       .append(passwordHash, user.passwordHash)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(userRoleId)
				       .append(username)
				       .append(passwordHash)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("User{")
				       .append("id=").append(getId())
				       .append(", userRoleId=").append(userRoleId)
				       .append(", username=").append(username)
				       .append(", passwordHash=").append(passwordHash)
				       .append(", active=").append(active)
				       .append(", createdAt=").append(createdAt)
				       .append(", deletedAt=").append(deletedAt)
				       .append("}")
				       .toString();
	}
	
}