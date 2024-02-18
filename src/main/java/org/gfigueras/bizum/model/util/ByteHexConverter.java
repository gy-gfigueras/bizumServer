package org.gfigueras.bizum.model.util;

public class ByteHexConverter {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int byteValue = (Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16);
            result[i / 2] = (byte) byteValue;
        }

        return result;
    }
}