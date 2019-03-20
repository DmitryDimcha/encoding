import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Code {

    public static void main(String[] args) throws IOException {
        String directory = System.getProperty("user.home");
        String fileName = "utf-32-be.txt";
        String source = directory + File.separator + fileName;
        String encoded = directory + File.separator + fileName + ".back.txt";
        fileWriter(encoded, getFirstFourByte(fileReader(source)));

    }

    static byte[] getFirstFourByte(byte[] bytes) throws IOException {
        ArrayList<Integer> inter = new ArrayList<>(); //Type №2
        List<Byte> arrays = new ArrayList<>();
        List<Byte> out = new ArrayList<>();
        int k = 0;
        int charCount = bytes.length / 4;

        for (int j = 0; j != charCount; j++) {
            for (int i = 0; i != 4; i++) {
                arrays.add(bytes[k]);
                k++;
            }
            out.addAll(Arrays.asList());
            for( byte b : intToBytes(IntUCStoUTF8(byteArrayToLeInt(toByteArray(arrays)))))out.add(b);
            //out.addAll(Arrays.asList(ArrayUtils.toObject()));
            //out.addAll(UCStoUTF8(UTF32BEtoUCS(arrays)));
            arrays.clear();

        }

        return toByteArray(out);
    }

    public static int byteArrayToLeInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

    static byte[] fileReader(String absolutePath) throws IOException {
        Path path = Paths.get(absolutePath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    static void fileWriter(String absolutePath, byte[] fileContent) throws IOException {
        FileOutputStream fos = new FileOutputStream(absolutePath);
        fos.write(fileContent);
    }

    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    public static List<Byte> UTF32BEtoUCS(List<Byte> in) {
        byte[] bytes = toByteArray(in);
        if ((bytes[0]== 0b00000000) ) {
            in.remove(0);
            if ((bytes[1] == 0b00000000)) {
                in.remove(0);
                //if ((bytes[2] == 0b00000000)) {
                  //  in.remove(0);
                //}
            }
        }
        Collections.reverse(in);
        return in;
    }


    public static List<Byte> UCStoUTF8(List<Byte> in) {
        byte a1 = (byte) 0b10000000;
        byte a2 = (byte) 0b11000000;
        byte a3 = (byte) 0b11100000;
        byte buffer1;
        byte buffer2;
        byte buffer3;
        byte buffer4;

        byte[] bytes = toByteArray(in);

        if (bytes.length == 1){
            return in;  //число < 128, вывод без изменений
        } else if (bytes.length == 2 && bytes[0] < 0b00001000){ //усли число 128 < n < 2048
            in.clear();
            buffer1 = (byte) (0b00111111 * bytes[1]);
            buffer1 = (byte) (0b10000000 + buffer1);

            buffer2 = (byte)  (bytes[1] >> 5);
            buffer2 = (byte) (0b00000011 * buffer2); //сдвиг последних 2х бит первой часть влево
            buffer3 = (byte) (0b00011111 * bytes[0]);
            buffer3 = (byte) (buffer3 << 1);
            buffer4 = (byte) (buffer3+buffer2);
            buffer4 = (byte) (0b00011111 * buffer4);
            buffer4 = (byte) (0b11000000 + buffer4);
            in.add(0, buffer4);
            return in;

        } else if (bytes.length == 3 && bytes[0] < 0b01000000){//усли число 247 < n <  65535
            buffer1 = (byte) (0b00111111 * bytes[2]);
            buffer1 = (byte) (0b10000000 + buffer1);
            in.add(0, buffer1);

            buffer2 = (byte) (bytes[1] >> 5); //сдвиг последних 2х бит первой часть влево
            buffer2 = (byte) (0b00000011 * buffer2);
            buffer3 = (byte) (0b00111111 * bytes[0]);
            buffer3 = (byte) (buffer3 << 1);
            buffer4 = (byte) (buffer3+buffer2);
            buffer4 = (byte) (0b00111111 * buffer4);
            buffer4 = (byte) (buffer4 << 2);
            buffer4 = (byte) (0b10000000 + buffer4);
            in.add(0, buffer4);

            buffer1 = (byte)(0b11110000 * bytes[0]);
            buffer1 = (byte)(buffer1 >> 3);
            in.add((byte)(0b11100000+buffer1));
            return in;

        }

        return in;
    }
    public static int IntUCStoUTF8(int in) throws IOException {
        byte a1 = (byte) 0b10000000;
        byte a2 = (byte) 0b11000000;
        byte a3 = (byte) 0b11100000;
        byte buffer1;
        byte buffer2;
        byte buffer3;
        byte buffer4;

        byte[] bytes = intToBytes(in);

        if (in < 128){
            return in;  //число < 128, вывод без изменений
        } else if (in < 2048){ //усли число 128 < n < 2048
            buffer1 = (byte) (0b00111111 * bytes[1]);
            buffer1 = (byte) (0b10000000 + buffer1);

            buffer2 = (byte)  (bytes[1] >> 5);
            buffer2 = (byte) (0b00000011 * buffer2); //сдвиг последних 2х бит первой часть влево
            buffer3 = (byte) (0b00011111 * bytes[0]);
            buffer3 = (byte) (buffer3 << 1);
            buffer4 = (byte) (buffer3+buffer2);
            buffer4 = (byte) (0b00011111 * buffer4);
            buffer4 = (byte) (0b11000000 + buffer4);
            bytes[1] = buffer1;
            bytes[0] = buffer4;
            return byteArrayToLeInt(bytes);

        } else if (in < 65535){//усли число 247 < n <  65535
            buffer1 = (byte) (0b00111111 * bytes[2]);
            buffer1 = (byte) (0b10000000 + buffer1);

            buffer2 = (byte) (bytes[1] >> 5); //сдвиг последних 2х бит первой часть влево
            buffer2 = (byte) (0b00000011 * buffer2);
            buffer3 = (byte) (0b00111111 * bytes[0]);
            buffer3 = (byte) (buffer3 << 1);
            buffer4 = (byte) (buffer3+buffer2);
            buffer4 = (byte) (0b00111111 * buffer4);
            buffer4 = (byte) (buffer4 << 2);
            buffer4 = (byte) (0b10000000 + buffer4);

            buffer2 = (byte)(0b11110000 * bytes[0]);
            buffer2 = (byte)(buffer2 >> 3);
            buffer2 = (byte)(0b11100000+buffer2);
            bytes[2] = buffer1;
            bytes[1] = buffer4;
            bytes[0] = buffer2;
            return byteArrayToLeInt(bytes);

        }

        return in;
    }

    public static byte[] intToBytes(int x) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        out.writeInt(x);
        out.close();
        byte[] int_bytes = bos.toByteArray();
        bos.close();
        return int_bytes;
    }

}
