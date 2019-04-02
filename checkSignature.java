import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class main {
    private static final String salt = "SaltySalt";
    private static final String password = "SuperSecret";
    private static final int IV_LENGTH = 16;


    public static void main(String[] args) {
        try {
            System.out.println("1. Подпишем исходный документ.");
            String path = consoleEnabler();
            String hash = String.valueOf(fileHashReader(path));
            System.out.println("Контрольная сумма файла : " + hash);
            System.out.println("Проведем шифрование контрольной суммы.");
            String assign = encrpytString(hash);
            System.out.println("Зашифрованная подпись : " + assign);
            System.out.println("1. Проведем валидацию подписанного документа.");
            String secondPath = "G:\\test.7z";
            String SecondHash = String.valueOf(fileHashReader(secondPath));
            System.out.println("Контрольная сумма файла : " + SecondHash + ".\n Проверим достоверность цифровой подписи.");
            String decrypted = decrpytString (assign);
            System.out.println(decrypted);
            System.out.println();
            if( decrypted.equals(SecondHash)) {
                System.out.println("Декодированная конрольная сумма :" + decrypted + ". \n Документ не скомпрометирован.");
            } else {
                System.out.println("Контрольная сумма документа не соответствует контрольной сумме подписи.");
            }


        } catch (IOException E){
            System.out.println("Ошибка ввода-вывода");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String consoleEnabler () {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите путь до файла: ");
        String intput = in.nextLine();
        in.close();

        return intput;
    }

    private static String fileHashReader (String FILENAME) throws IOException, NoSuchAlgorithmException {
        int hash;
        FileReader fr= new FileReader(FILENAME);
        Scanner scan = new Scanner(fr);
        hash = fr.hashCode();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String hex = checksum(FILENAME, md);
        fr.close();
        return hex;
    }

    private static byte[] getSaltBytes() throws Exception {
        return salt.getBytes("UTF-8");
    }

    private static String getMasterPassword() {
        System.out.println("Введите пароль:");
        String password = consoleEnabler();
        return password;
    }

    public static String encrpytString (String input)  throws Exception{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), getSaltBytes(), 65536, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encryptedTextBytes = cipher.doFinal(input.getBytes("UTF-8"));
            byte[] finalByteArray = new byte[ivBytes.length + encryptedTextBytes.length];
            System.arraycopy(ivBytes, 0, finalByteArray, 0, ivBytes.length);
            System.arraycopy(encryptedTextBytes, 0, finalByteArray, ivBytes.length, encryptedTextBytes.length);
            return DatatypeConverter.printBase64Binary(finalByteArray);

    }

    public static String decrpytString (String input) throws Exception {
        if (input.length() <= IV_LENGTH) {
            throw new Exception("The input string is not long enough to contain the initialisation bytes and data.");
        }
        byte[] byteArray = DatatypeConverter.parseBase64Binary(input);
        byte[] ivBytes = new byte[IV_LENGTH];
        System.arraycopy(byteArray, 0, ivBytes, 0, 16);
        byte[] encryptedTextBytes = new byte[byteArray.length - ivBytes.length];
        System.arraycopy(byteArray, IV_LENGTH, encryptedTextBytes, 0, encryptedTextBytes.length);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), getSaltBytes(), 65536, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        return new String(decryptedTextBytes);

    }

    private static String checksum(String filepath, MessageDigest md) throws IOException {

        // DigestInputStream is better, but you also can hash file like this.
        try (InputStream fis = new FileInputStream(filepath)) {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        }

        // из 2ичной в 16 ричную
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }

}
