import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;

public class authenticating {
    private static final String ACCESS_TOKEN = "2A_G4MJY_cAAAAAAAAAALvMd4ynM94zbXN9VTuV0wxm7aR49AQv8xfS3NQN661u-";

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

    public static void upload(DbxClientV2 client) throws DbxException {
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/text.txt")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(DbxClientV2 client) throws DbxException {
        DbxDownloader<FileMetadata> downloader = client.files().download("/test.txt");
        try {
            FileOutputStream out = new FileOutputStream("C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/text3.txt");
            downloader.download(out);
            out.close();
        } catch (DbxException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String args[]) throws DbxException {
        // Create Dropbox client

        DbxClientV2 client = connect();
        listContent(client);
        upload(client);
        download(client);


    }
}