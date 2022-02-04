package FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class ReadAndDelletFirst {
	public static void main(String []args) throws IOException {
		
		//removeFirstLine("src/text");
		System.out.println("First Line>> "+readFirstLine("src/text"));
		// create new file with the presumed policy
		newFile("src/poli", "aze ezeze");
		// concat the policy file and the text file
		concatFiles("src/poli", "src/text", "src/result");
	}
	
	public static void removeFirstLine(String fileName) throws IOException {  
	    RandomAccessFile raf = new RandomAccessFile(fileName, "rw");          
	     //Initial write position                                             
	    long writePosition = raf.getFilePointer();                            
	    raf.readLine();                                                       
	    // Shift the next lines upwards.                                      
	    long readPosition = raf.getFilePointer();                             

	    byte[] buff = new byte[1024];                                         
	    int n;                                                                
	    while (-1 != (n = raf.read(buff))) {                                  
	        raf.seek(writePosition);                                          
	        raf.write(buff, 0, n);                                            
	        readPosition += n;                                                
	        writePosition += n;                                               
	        raf.seek(readPosition);                                           
	    }                                                                     
	    raf.setLength(writePosition);                                         
	    raf.close();                                                          
	}
	
	public static void newFile(String file, String content) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.println(content);
		writer.close();
	}
	
	public static String readFirstLine(String file) throws IOException {
		File fl = new File(file);
		RandomAccessFile f = new RandomAccessFile(fl, "rw");
		f.seek(0); // to the beginning
		String tmp = f.readLine();
		f.close();
		return tmp;
	}

	public static void concatFiles(String F1, String F2, String R) throws IOException {
		// PrintWriter object for file3.txt 
        PrintWriter pw = new PrintWriter(R); 
          
        // BufferedReader object for file1.txt 
        BufferedReader br = new BufferedReader(new FileReader(F1)); 
          
        String line = br.readLine(); 
          
        // loop to copy each line of  
        // file1.txt to  file3.txt 
        while (line != null) 
        { 
            pw.println(line); 
            line = br.readLine(); 
        } 
          
        br = new BufferedReader(new FileReader(F2)); 
          
        line = br.readLine(); 
          
        // loop to copy each line of  
        // file2.txt to  file3.txt 
        while(line != null) 
        { 
            pw.println(line); 
            line = br.readLine(); 
        } 
          
        pw.flush(); 
          
        // closing resources 
        br.close(); 
        pw.close(); 

	}
}
