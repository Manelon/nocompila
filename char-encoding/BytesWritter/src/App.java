import java.io.FileOutputStream;

public class App {
    public static void main(String[] args) throws Exception {
        //write all bytes to file
        FileOutputStream fos = new FileOutputStream("256bytes.txt");
        for (int i = 0; i < 256; i++) {
            fos.write(i);
        }
        fos.close();

        //Get EBCDIC bytes from string
        String str = "Hello World";
        byte[] bytes = str.getBytes("CP1047");
        System.out.println("EBCDIC bytes: " + bytes.length);
        //print arraybytes in hex
        for (byte b : bytes) {
            System.out.printf("%02X ", b);
        }

        //Get ASCII bytes from string
        str = "Hello World";
        bytes = str.getBytes();
        System.out.println("\nASCII bytes: " + bytes.length);
        //print arraybytes in hex
        for (byte b : bytes) {
            System.out.printf("%02X ", b);
        }
    }
}
