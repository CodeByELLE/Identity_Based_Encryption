package Protocol;

import java.io.Serializable;

public class Authentication implements Serializable{
	
	private String UserID;
	private String Password;
	
	public Authentication(String UserID, String Password) {
		// TODO Auto-generated constructor stub
		this.UserID = UserID;
		this.Password = Password;
	}
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	
	
}
