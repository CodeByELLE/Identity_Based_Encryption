package EnkDec;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.util.Base64;

import org.apache.commons.io.FileUtils;



import FileUtil.Metadata;
import FileUtil.ReadAndDelletFirst;
import FileUtil.fileManager;
import uk.ac.ic.doc.jpair.ibe.BFCtext;

public class EnkDec {
	
	public static File[] encrypte(File f,String Policy,KeyPair userkey) {
		
		try {
			byte[] fileByte = RC4.read(f);
			byte[] keyByte = RC4.keyGen().getBytes();
	        String key = new String(keyByte);
	        RC4 rc = new RC4(key);
	        
	        File EncFile = new File("tmp");
	        byte[] enText = rc.encrypt(fileByte);
	        FileUtils.writeByteArrayToFile(EncFile, enText);
	        BFCtext encKey = jpair.encrypt(userkey.getPublic(), key);
	        
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutput out = null;
	        out = new ObjectOutputStream(bos);
	        out.writeObject(encKey);
	        out.flush();
	        byte[] yourBytes = bos.toByteArray();
	        String encodedKey = Base64.getEncoder().encodeToString(yourBytes);
	        //------------------------------------------------------------------------------------------
	        File [] result = new File[2];
	        result[0]=EncFile;
	        result[1]=fileManager.createPolicyFile(Policy+" "+encodedKey, "tmp2");
	        
	        return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
	}

	public static File Decrypte(String []files,KeyPair userkey,String toPath) {
		
		String[] meta;
		String tmp;
		try {
			 File fecn = new File(files[0]);
			 File Policy = new File(files[1]);
			 System.out.println("*******files order "+fecn.getPath()+" "+Policy.getPath());
			 tmp =fileManager.getPolicy(Policy);
			 Policy.delete();
			 meta = tmp.split(" ");
			
			 
			 String date = meta[0];   
		     String MetaKey = meta[1];
		     byte[] decoded = Base64.getDecoder().decode(MetaKey);
		     ByteArrayInputStream inbos = new ByteArrayInputStream(decoded);
		     ObjectInputStream onji = new ObjectInputStream(inbos);
		     BFCtext cck = (BFCtext) onji.readObject();// result key
		     byte[] decryptedKey = jpair.decrypt(userkey.getPrivate(), cck);
		     byte[] fileB = RC4.read(fecn);
		     RC4 nk = new RC4(new String(decryptedKey));
		     byte[] deText = nk.decrypt(fileB);
		     File EncFile = new File(toPath);
		     FileUtils.writeByteArrayToFile(EncFile, deText);
		     fecn.delete();
		     //setMetadata
		     return EncFile;
		     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return null;
	}

}
