package Server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import Protocol.Authentication;
import Protocol.AuthenticationResp;
import Protocol.ClientRequest;
import Protocol.PKGRequest;

class ServerThread extends Thread {
	
    private SSLSocket sslSocket = null;
    private String UserId;
     
    ServerThread(SSLSocket sslSocket){
        this.sslSocket = sslSocket;
    }
     
    public void run(){
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
         
        try{
            // Start handshake
            sslSocket.startHandshake();
             
            // Get session after the connection is established
            SSLSession sslSession = sslSocket.getSession();
             
        /*    System.out.println("SSLSession :");
            System.out.println("\tProtocol : "+sslSession.getProtocol());
            System.out.println("\tCipher suite : "+sslSession.getCipherSuite());
             */
            
            // Start handling application content
            InputStream inputStream = sslSocket.getInputStream();
            OutputStream outputStream = sslSocket.getOutputStream();
            
            ObjectInputStream Oin =  new ObjectInputStream(inputStream);
            ObjectOutputStream Oout = new ObjectOutputStream(outputStream);
            System.out.println("receiving authentication");
            Authentication auth = (Authentication)Oin.readObject();
            System.out.println("authentication received");
            if(auth.getUserID().equals("OK")&auth.getPassword().equals("OK")) {
            	AuthenticationResp authresp = new AuthenticationResp(1);
            	System.out.println("authentication sent "+authresp.getStatus());
            	Oout.writeObject(authresp);
            	this.UserId = auth.getUserID();
            	System.out.println("authentication of the client succeded ok respons sent");
            	
            	ClientRequest clrequ;
            	while(true) {
            		System.out.println("waiting for key request");
            		clrequ = (ClientRequest) Oin.readObject();
            		// need to check policy 
            		if(clrequ.getID()==null) {
            			if(HTTPSServer.policys.ChekPolicyDate(clrequ.getDatePolicy())!=-1) {
            				System.out.println("policiy verified");
	            			KeyPair tmp = HTTPSServer.keyGen.getUserKeys(this.UserId+"|"+clrequ.getDatePolicy());
	                		PKGRequest resp = new PKGRequest(tmp);
	                		Oout.writeObject(resp);
	                		System.out.println("Keys sent to"+this.UserId+"\n\t kp"+tmp.getPublic()+"\n\t"+tmp.getPrivate());
            			}else {
            				System.out.println("policiy Expired");
            				PKGRequest resp = new PKGRequest(null);
	                		Oout.writeObject(resp);
            			}
            		}
            		
            	}
            }else {
            	AuthenticationResp authresp = new AuthenticationResp(-1);
            	Oout.writeObject(authresp);
            	System.out.println("authentication of the client unsucsesful ok respons sent");
            }
            
            /*
            Oout.writeObject(new String("Hello client how are you doing"));
            String received = (String) Oin.readObject();
            System.out.println("Received string from the Client \n\t"+received);
            */
            sslSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
