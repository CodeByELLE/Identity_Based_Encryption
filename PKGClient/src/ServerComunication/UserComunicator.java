package ServerComunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.Date;

import javax.net.ssl.SSLSocket;

import Protocol.Authentication;
import Protocol.AuthenticationResp;
import Protocol.ClientRequest;
import Protocol.PKGRequest;

public class UserComunicator {
	
    private SSLSocket sslSocket = null;
    
    private ObjectOutputStream Oout;
    private ObjectInputStream Oin;
    
    UserComunicator(SSLSocket sslSocket){
    	
        this.sslSocket = sslSocket;

		try {	      
			this.sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
			this.sslSocket.startHandshake();
			
	        InputStream inputStream = sslSocket.getInputStream();
	        OutputStream outputStream = sslSocket.getOutputStream();
	        
	        this.Oout = new ObjectOutputStream(outputStream);
	        this.Oin = new ObjectInputStream(inputStream);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    /**
     * authenticate the user 
     **/
    public boolean authenticat(String Id,String password) {  	
    	try {
    		System.out.println("sending authentication");
        	Authentication auth = new Authentication(Id,password);
			this.Oout.writeObject(auth);
        	System.out.println("auth sent");
			AuthenticationResp authresp;
			authresp = (AuthenticationResp)this.Oin.readObject();
			System.out.println(" authetication status "+authresp.getStatus());
			if(authresp.getStatus()==1) {
				System.out.println("authetication true");
				return true;
			}else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    /**
     * Get the keys by policy date 
     * This method can't be called only if the authentication succeed
     * @param Date date defined in the policy
     **/
    
    public KeyPair getKeysByDatePolicy(Date DatePolicy) {
    	try {
    		ClientRequest clr = new ClientRequest(DatePolicy);
        	System.out.println(" sending request");
			this.Oout.writeObject(clr);
			System.out.println(" waiting for Keypaire");
			PKGRequest resp = (PKGRequest) this.Oin.readObject();
			return resp.getKeys();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
    }
    
    /**
     * Get the keys by policy ID 
     * This method can't be called only if the authentication succeed
     * @param Date date defined in the policy
     **/
    
    public KeyPair getKeysByPolicyID(Integer ID) {
    	try {
    		ClientRequest clr = new ClientRequest(ID);
        	System.out.println(" sending request");
			this.Oout.writeObject(clr);
			System.out.println(" waiting for Keypaire");
			KeyPair received = (KeyPair) this.Oin.readObject();
			return received;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
    }
     
 
}
