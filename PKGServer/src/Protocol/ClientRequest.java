package Protocol;

import java.io.Serializable;
import java.util.Date;

public class ClientRequest implements Serializable{
	
	private Date DatePolicy;
	private Integer ID;
	
	public ClientRequest(Date DatePolicy) {
		this.DatePolicy = DatePolicy;
	}
	
	public ClientRequest(Integer ID) {
		this.ID = ID;
	}
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Date getDatePolicy() {
		return DatePolicy;
	}
	public void setDatePolicy(Date datePolicy) {
		DatePolicy = datePolicy;
	}
	
	
	
	
}
