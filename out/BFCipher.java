/*
 * ENCRYPTION USING Jpair SCHEME
 * DONE ACCORDING IBE SCHEME
 */



import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import uk.ac.ic.doc.jpair.api.*;
import uk.ac.ic.doc.jpair.ibe.*;
import uk.ac.ic.doc.jpair.ibe.key.*;
import uk.ac.ic.doc.jpair.pairing.*;







class BFcipher {

    public static java.security.KeyPair genKeyPair() {
        //  GENERATE PAIRING "e"
        Pairing e = Predefined.nssTate();

        // SETUP
        java.security.KeyPair masterKey=BFCipher.setup( e ,new Random());


        //  EXTRACT
        java.security.KeyPair userKey=BFCipher.extract( masterKey,"Client1",new Random());

        return userKey;
    }


    public static BFCtext encrypt(PublicKey uPub, String s) {
        //  ENCRYPT
        byte msgenc[]= new byte[50];
        msgenc = s.getBytes();
        BFCtext msgCipher = BFCipher.encrypt((BFUserPublicKey) uPub,msgenc, new Random()) ;
        return msgCipher;
    }

    public static byte[] decrypt(PrivateKey uPri, BFCtext msgCipher) {
        //  DECRYPT
        byte msgdec[]=new byte[50];
        msgdec=BFCipher.decrypt(msgCipher, (BFUserPrivateKey) uPri) ;

        return msgdec;
    }


    /*public static void main(String[] args) {
        java.security.KeyPair userKey = genKeyPair();
        BFCtext encMsg = encrypt(userKey.getPublic(), "test message");
        byte[] msgdec = decrypt(userKey.getPrivate(), encMsg);

        System.out.println("AFTER DECRYPTION : " );
        // Printing message
        for(int j = 0; j < msgdec.length; j++) {
            System.out.print((char)msgdec[j]);
        }
    }*/


    public static void encSecretKey(String key) {
        java.security.KeyPair userKey = genKeyPair();
        BFCtext encMsg = encrypt(userKey.getPublic(), key);
        byte[] msgdec = decrypt(userKey.getPrivate(), encMsg);
        System.out.println("encrypted key : " + msgdec.toString());
        System.out.println("AFTER DECRYPTION : " );
        // Printing message
        for(int j = 0; j < msgdec.length; j++) {
            System.out.print((char)msgdec[j]);
        }
    }



}