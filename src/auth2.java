import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import org.apache.commons.io.FileUtils;
import uk.ac.ic.doc.jpair.ibe.BFCtext;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Base64;

public class auth2 {
    private static final String ACCESS_TOKEN = "2A_G4MJY_cAAAAAAAAAALvMd4ynM94zbXN9VTuV0wxm7aR49AQv8xfS3NQN661u-";
    public static UserDefinedFileAttributeView view;

    public static DbxClientV2 connect() throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        return client;

    }

    public  static void listContent(DbxClientV2 client) throws DbxException {
        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

    }

    public static void upload(DbxClientV2 client, File file) throws DbxException {
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream(file)) {
            FileMetadata metadata = client.files().uploadBuilder("/"+file.getName())
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileOutputStream download(DbxClientV2 client, String path,String name) throws DbxException {
        DbxDownloader<FileMetadata> downloader = client.files().download("/"+name);
        try {
            FileOutputStream out = new FileOutputStream(path);
            downloader.download(out);
            //out.close();
            return out;
        } catch (DbxException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    	public static void setMetadata(File file, String attr, String value) throws IOException {
    	//**************************set policy
    	// value = date " " key
    	view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
    	view.write(attr, Charset.defaultCharset().encode(value));
    	}

    	//***************************get policy
    	public static String[] getMetadata(File file, String attr) throws IOException {
    	ByteBuffer buf = ByteBuffer.allocate(2024);
    	view.read(attr, buf);
    	buf.flip();
    	String value = Charset.defaultCharset().decode(buf).toString();
    	String[] splittedValue = value.split(" ");

    	return splittedValue;
    	}
    	
    public static void main(String args[]) throws DbxException, IOException, ClassNotFoundException {
        // Create Dropbox client
        java.security.KeyPair userkey =  jpair.genKeyPair();

        File file = new File("src/text.txt");


        DbxClientV2 client = connect();
        byte[] fileByte = RC4.read(file);
        byte[] keyByte = RC4.keyGen().getBytes();
        String key = new String(keyByte);
        RC4 rc = new RC4(key);

        File EncFile = new File("src/text2.enc");
        byte[] enText = rc.encrypt(fileByte);
        FileUtils.writeByteArrayToFile(EncFile, enText);
        BFCtext encKey = jpair.encrypt(userkey.getPublic(), key);
        //________________________________________
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        out = new ObjectOutputStream(bos);
        out.writeObject(encKey);
        out.flush();
        byte[] yourBytes = bos.toByteArray();
        // string to concat with policy in metadata
        String encodedKey = Base64.getEncoder().encodeToString(yourBytes);
        
        
        
        setMetadata(file, "policy" , "12/10/2019" +" " + encodedKey);
        // process to get back the object


        String date = getMetadata(file, "policy")[0];
        System.out.println("----------" + date);
        
        String MetaKey = getMetadata(file, "policy")[1];
        System.out.println("----------" + MetaKey);

        byte[] decoded = Base64.getDecoder().decode(MetaKey);

        ByteArrayInputStream inbos = new ByteArrayInputStream(decoded);
        ObjectInputStream onji = new ObjectInputStream(inbos);
        BFCtext cck = (BFCtext) onji.readObject();// result key
        //_____________
        
        
        //System.out.println(encKey.getU().getX().);
       // byte[] decryptedKey = jpair.decrypt(userkey.getPrivate(), encKey);
        byte[] decryptedKey = jpair.decrypt(userkey.getPrivate(), cck);
        upload(client, EncFile);
       /*setMetadata(EncFile, "key" , encKey );
        System.out.println("here : " +encKey);
        System.out.println("a");
       // File encFile = new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/encFile.txt");
       upload(client, EncFile);
        //EncFile.delete();

        //*********************************


        download(client, "src/down.enc",EncFile.getName());*/

     //   File downEncFile = new File("src/down.enc");
       // byte[] fileB = RC4.read(downEncFile);
        byte[] fileB = RC4.read(EncFile);

        //Object policyDate =  getMetadata(EncFile, "User.Policy.Date");
        // Object policyKey =  getMetadata(downEncFile, "key");

//        System.out.println("Date : " + policyDate);
        //System.out.println("Key : " + policyKey);

        //System.out.println("dec key : " + jpair.decrypt(userkey.getPrivate(),policyKey));
        RC4 nk = new RC4(new String(decryptedKey));
        byte[] deText = nk.decrypt(fileB);
        Path path2 = Paths.get("src/decFileFinal.txt");
        Files.write(path2, deText);





    }
}
