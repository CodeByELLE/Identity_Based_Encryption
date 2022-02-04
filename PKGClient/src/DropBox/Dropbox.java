package DropBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class Dropbox {
	
	DbxClientV2 client;
	
	public Dropbox(String TokenT) {
		try {
			this.Connect(TokenT);
		} catch (DbxApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Connect(String TokenT) throws DbxApiException, DbxException {
		
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        this.client = new DbxClientV2(config, TokenT);
        // Get current account info
        FullAccount account = client.users().getCurrentAccount(); 
	}
	
	public void upload( File file) throws DbxException {
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream(file)) {
            FileMetadata metadata = this.client.files().uploadBuilder("/"+file.getName())
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void upload( File file, String nameDest) throws DbxException {
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream(file)) {
            FileMetadata metadata = this.client.files().uploadBuilder("/"+nameDest)
                    .uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public File download(String path,String name) throws DbxException {
        DbxDownloader<FileMetadata> downloader = this.client.files().download("/"+name);
        try {
            FileOutputStream out = new FileOutputStream(path);
            downloader.download(out);
            out.close();
            return new File(path);
        } catch (DbxException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
	
	 public  void listContent() throws DbxException {
	        // Get files and folder metadata from Dropbox root directory
	        ListFolderResult result = this.client.files().listFolder("");
	        while (true) {
	            for (Metadata metadata : result.getEntries()) {
	                System.out.println(metadata.getPathLower());
	            }

	            if (!result.getHasMore()) {
	                break;
	            }

	            result = this.client.files().listFolderContinue(result.getCursor());
	        }

	    }

}
