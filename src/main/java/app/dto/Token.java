package app.dto;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Token implements Serializable {

    private String username;
    
    private RoleDTO[] roles;
    
    private SelfPermissionDTO[] selfPermissions;
    
    private BasicPermissionDTO[] basicPermissions;
    
    private long timestamp;

    public Token() {

    }

    public Token(String username, long timestamp) {
        super();
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

	public RoleDTO[] getRoles() {
		return roles;
	}

	public void setRoles(RoleDTO[] roles) {
		this.roles = roles;
	}

	public SelfPermissionDTO[] getSelfPermissions() {
		return selfPermissions;
	}

	public void setSelfPermissions(SelfPermissionDTO[] selfPermissions) {
		this.selfPermissions = selfPermissions;
	}

	public BasicPermissionDTO[] getBasicPermissions() {
		return basicPermissions;
	}

	public void setBasicPermissions(BasicPermissionDTO[] basicPermissions) {
		this.basicPermissions = basicPermissions;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "Token [username=" + username + ", roles="
				+ (roles != null ? Arrays.asList(roles).subList(0, Math.min(roles.length, maxLen)) : null)
				+ ", selfPermissions="
				+ (selfPermissions != null
						? Arrays.asList(selfPermissions).subList(0, Math.min(selfPermissions.length, maxLen))
						: null)
				+ ", basicPermissions="
				+ (basicPermissions != null
						? Arrays.asList(basicPermissions).subList(0, Math.min(basicPermissions.length, maxLen))
						: null)
				+ ", timestamp=" + timestamp + "]";
	}

	

}
