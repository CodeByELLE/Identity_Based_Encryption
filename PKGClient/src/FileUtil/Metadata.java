package FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class Metadata {

	
	public static void setMetadata(File file, String attr, String value) throws IOException {
    	//**************************set policy
    	// value = date " " key
		UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
    	view.write(attr, Charset.defaultCharset().encode(value));
    	}
	
	public static String getMetadata(File file, String attr) throws IOException {
		UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
    	ByteBuffer buf = ByteBuffer.allocate(2024);
    	view.read(attr, buf);
    	buf.flip();
    	String value = Charset.defaultCharset().decode(buf).toString();
    	return value;
    	}
}
