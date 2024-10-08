package edu.eci.arsw.math;

public class Main {

    public static void main(String[] args) {
        System.out.println(bytesToHex(PiDigits.getDigits(0, 10, 5)));
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 100, 5)));
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 1000000, 20)));
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexChars.length; i += 2) {
            sb.append(hexChars[i + 1]);
        }
        return sb.toString();
    }
}
