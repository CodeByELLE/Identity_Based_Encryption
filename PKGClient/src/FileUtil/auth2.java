package FileUtil;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import EnkDec.RC4;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.Date;


public class auth2 {
    private static final String ACCESS_TOKEN = "2A_G4MJY_cAAAAAAAAAAOY3PRBCLspJZL3RfLiXbs1rbPbj2dyHUU-zTzTRIuebs";

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
            FileMetadata metadata = client.files().uploadBuilder("/zipFile2.zip")
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileOutputStream download(DbxClientV2 client, String path) throws DbxException {
        DbxDownloader<FileMetadata> downloader = client.files().download("/zipFile2.zip");
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

    public static void main(String args[]) throws DbxException, IOException {
        // Create Dropbox client
        File file = new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/text.txt");


        DbxClientV2 client = connect();
        byte[] fileByte = RC4.read(file);
        String key =  RC4.keyGen();
        RC4 rc = new RC4(key);
        byte[] enText = rc.encrypt(fileByte);
        FileUtils.writeByteArrayToFile(new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/encFile.txt"), enText);
        //rc.setMetadata(file, "12/10/2019");

        File policyFile = fileManager.createPolicyFile("12/01/3283"+ " " + key, "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/policy.txt");
        System.out.println("a");
        File encFile = new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/encFile.txt");
        Path encFilePath = Paths.get("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/encFile.txt");

        File zippedFiles = fileManager.zip(encFilePath.toString(), policyFile.getPath().toString(), "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/archive.zip");
        upload(client, zippedFiles);

        //*********************************


        download(client, "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downZip.zip");
        File downZip = new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downZip.zip");
        fileManager.unzip("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downZip.zip", "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/unzipFiles" );
        System.out.println(fileManager.getPolicy(new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downZip.zip")));

        //byte[] fileB = rc.read(new File("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downEncFile.txt"));

        /*Path f = Paths.get("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/downZip.zip");



        byte[] deText = rc.decrypt(fileB);

        Path path2 = Paths.get("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/decFileFinal.txt");
        Files.write(path2, deText);*/





    }
}