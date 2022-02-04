package PKG;

import java.util.Random;

import uk.ac.ic.doc.jpair.api.Pairing;
import uk.ac.ic.doc.jpair.ibe.BFCipher;
import uk.ac.ic.doc.jpair.pairing.Predefined;

public class PKG {
	
	private java.security.KeyPair masterKey ;

	public PKG() {
        //  GENERATE PAIRING "e"
        Pairing e = Predefined.nssTate();
        // SETUP
        this.masterKey=BFCipher.setup( e ,new Random());// do we need to store the master key
	}
	
	public java.security.KeyPair getUserKeys(String tmp){
		return BFCipher.extract( masterKey,"a",new Random());
	} 
}

