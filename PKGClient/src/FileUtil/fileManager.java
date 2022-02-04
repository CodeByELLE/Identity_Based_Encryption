package FileUtil;

import java.io.*;
import java.security.KeyPair;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class fileManager {



    public static String getPolicy(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        return br.readLine();
    }

    public static File createPolicyFile(String policy, String pathPolicyFile){
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(pathPolicyFile), "utf-8"));
            writer.write(policy);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        return new File(pathPolicyFile);
    }


    public static File zip(String pathFile1, String pathFile2, String pathDest){
        String zipFile = pathDest;
        String[] srcFiles = { pathFile1, pathFile2};
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i=0; i < 2; i++) {
                File srcFile = new File(srcFiles[i]);
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
        return new File(pathDest);
    }


    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

            String pathFile1 = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/text.txt";
            String pathFile2 = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/encFile.txt";
            String pathDesc = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/zippedFile.zip";
            String policyPath = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/policy.txt";
            zip(pathFile1, pathFile2, pathDesc);
            createPolicyFile("12/03/2093", policyPath);

            String zipFilePath = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/archive.zip";
            String destDir = "C:/Users/Meryem/Desktop/DNA-POO/crypto/projet/implimentation/output";

            unzip(zipFilePath, destDir);


    }

}
