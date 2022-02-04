package Main;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.dropbox.core.DbxException;

import DropBox.Dropbox;
import EnkDec.EnkDec;
import FileUtil.fileManager;
import ServerComunication.HTTPSClient;
import ServerComunication.UserComunicator;

public class Main {

	public UserComunicator com;
	public Dropbox Dpbx;
	
	private String TokenT = "9I0urY_8cUAAAAAAAAAAZTWeV1jzZ8Vhg5T_Td9GEWJUVXjo0qLmSpqpqchECIid";
	
	private void connectWithPkg() {
		
		 	HTTPSClient client = new HTTPSClient();
	        this.com = client.run();
	        com.authenticat("OK", "OK");
	    }
	
		private void connectWithDropbox() {
			this.Dpbx = new Dropbox(TokenT);
		}	
		

	public void EncSendProsse(String Path , String nameDestination) throws ParseException, DbxException {
		// 
		File f = new File(Path);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		KeyPair keys = this.com.getKeysByDatePolicy(simpleDateFormat.parse("2/12/2019"));
		File[] r = EnkDec.encrypte(f, "2/12/2018", keys);
		String pathDest = nameDestination;
		this.Dpbx.upload(fileManager.zip(r[0].getPath(), r[1].getPath(), pathDest));
	}
	
	public void DecRcvProcesse(String fileName , String PathDestination) throws DbxException, ParseException {
		//download 
		File rr = this.Dpbx.download("src/ziprcv", fileName);
		fileManager.unzip(fileName, "src/rcvD");
		File folder = new File("src/rcvD");	
		// 0 encrypted 1 policy 
		String[] fff = folder.list();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		KeyPair keys = this.com.getKeysByDatePolicy(simpleDateFormat.parse("2/12/2019"));
		File rd = EnkDec.Decrypte(fff, keys,PathDestination);
		folder.delete();
	}
	
	public void testCode(String folderPath) throws ParseException, DbxException {
		File folder = new File(folderPath);
		long startTime,stopTime,elapsedTime;
		for(String l:folder.list()) {
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			File f = new File(folderPath+"/"+l);
			System.out.println("file "+l);
			System.out.println("Upload with Encryption process***********");
			
			startTime = System.currentTimeMillis();
				this.EncSendProsse(folderPath+"/"+l,l+"dbx");
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			
			System.out.println("\t elapsed time in Millis "+elapsedTime);
			System.out.println("\t elapsed time in s "+((double)elapsedTime)/1000);
			System.out.println("Upload plain Text ***********************");
			startTime = System.currentTimeMillis();
				this.Dpbx.upload(f, "test"+l);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("\t elapsed time in Millis "+elapsedTime);
			System.out.println("\t elapsed time in s "+((double)elapsedTime)/1000);
			System.out.println("Download with Encryption process *******");
			startTime = System.currentTimeMillis();
				this.DecRcvProcesse(l+"dbx",folderPath+"/received/decrypted/"+l);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("\t elapsed time in Millis "+elapsedTime);
			System.out.println("\t elapsed time in s "+((double)elapsedTime)/1000);
			System.out.println("Download in plain **********************");
			startTime = System.currentTimeMillis();
				this.Dpbx.download( folderPath+"/received/plain/","test"+l);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("\t elapsed time in Millis "+elapsedTime);
			System.out.println("\t elapsed time in s "+((double)elapsedTime)/1000);
			System.out.println("\n**************************************");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
	}
	
	public static void main(String[] args) throws IOException, DbxException, ParseException {
		Main l = new Main();
		l.connectWithPkg();
		l.connectWithDropbox();
		System.out.println("give path to testing folder");
		Scanner sc = new Scanner(System.in);
		String path = sc.next();
		l.testCode(path);
    }
	
}
