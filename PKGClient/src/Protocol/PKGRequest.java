package Protocol;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PKGRequest implements Serializable{
		
	KeyPair Keys;
	
	public PKGRequest(KeyPair Keys) {
		this.Keys = Keys;
	}

	public KeyPair getKeys() {
		return Keys;
	}

	public void setKeys(KeyPair keys) {
		Keys = keys;
	}
	
}
