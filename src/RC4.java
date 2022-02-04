//package netsec;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import org.apache.commons.lang3.RandomStringUtils;


/**
 *
 * @author Siva
 */
public class RC4 {

    /**
     * @param args
     */
    static short[] S;
    static short[] T;
    public static final int MAX_FILE_SIZE = 1024;

    public RC4(String keyString) {

        if (keyString.length() < 1 && keyString.length() > 256) {
            throw new IllegalArgumentException("Key lenght should be in between 1 and 256");
        }

        byte[] tempKey = keyString.getBytes();
        short[] key = new short[tempKey.length];
        int keyLength = tempKey.length;

        for (int i = 0; i < keyLength; i++) {
            key[i] = (short) ((short) tempKey[i] & 0xff);
        }
        ksa(key);

    }


    public static String keyGen() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String pwd = RandomStringUtils.random( 15, characters );
        System.out.println( pwd );
        return pwd;
    }



    public static byte[] read(File file) throws IOException {
        if (file.length() > MAX_FILE_SIZE) {

        }
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    public void ksa(short[] key) {
        short temp;
        S = new short[256];
        T = new short[256];

        for (int i = 0; i < 256; i++) {
            S[i] = (short) i;
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + key[i % key.length]) % 256;

            temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }
        System.arraycopy(S, 0, T, 0, S.length);
    }

    public byte[] genPad(int length) {
        System.arraycopy(S, 0, T, 0, S.length);
        int i = 0, j = 0;
        short temp;
        byte[] tempPpad = new byte[length];
        for (int k = 0; k < length; k++) {
            i = (i + 1) % 256;
            j = (j + T[i]) % 256;

            temp = T[i];
            T[i] = T[j];
            T[j] = temp;

            tempPpad[k] = (byte) (T[(T[i] + T[j]) % 256]);
        }
        return tempPpad;
    }

    public byte[] encrypt(byte[] plain) {
        byte[] pad = genPad(plain.length);
        byte[] encrypt = new byte[plain.length];
        for (int i = 0; i < plain.length; i++) {
            encrypt[i] = (byte) (plain[i] ^ pad[i]);
        }
        return encrypt;
    }

    public byte[] decrypt(byte[] cipher) {
        byte[] plain = encrypt(cipher);
        return plain;
    }



	/*	public static void main(String[] args) {
			try{
				File file = new File("C:/Users/Emily/Desktop/DNA courses/Cryptography/Project/data/text.txt");
				byte[] fileByte = read(file);
				byte[] key = RC4.keyGen().getBytes();
				RC4 rc = new RC4(new String(key));
				byte[] enText = rc.encrypt(fileByte);

				setMetadata(file, "12/10/2019");
				Path path1 = Paths.get("C:/Users/Emily/Desktop/DNA courses/Cryptography/Project/data/EncOut.txt");

				Files.write(path1, enText);
				byte[] deText = rc.decrypt(enText);

				Path path2 = Paths.get("C:/Users/Emily/Desktop/DNA courses/Cryptography/Project/data/DectOut.txt");
				Files.write(path2, deText);

			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
*/
}















