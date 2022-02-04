package Protocol;

import java.io.Serializable;

public class AuthenticationResp implements Serializable{
	
	// value 1 for authenticated or -1 failed to authenticate 
	private int Status;
	
	public AuthenticationResp(int Status) {
		this.Status = Status;
	}
	
	/**
	 * -1 failed to authenticate
	 *  1 authentication succeed 
	 **/
	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
	
	

}
