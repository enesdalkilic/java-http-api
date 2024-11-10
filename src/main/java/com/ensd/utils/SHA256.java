package com.ensd.utils;

import java.util.ArrayList;
import java.util.List;

public class SHA256 {

    private static final int ttMask = 0xFFFFFFFF;
    // SHA-256 constants and initial hash values
    private static final int[] hashValues = {0x6a09e667,
            0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};

    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    private static final List<Integer> messageSchedule = new ArrayList<>();


    public static String generate(String msg) {
        String binaryMSG = stringTo8BitBinary(msg);
        String msgSkdString = padTo512Bits(binaryMSG);

        prepareMessageSchedule(msgSkdString);
        expandMsgSkd();
        compress();

        return hashToHexString(hashValues);
    }

    public static Boolean compare(String text, String hash) {
        String textHash = generate(text);
        return textHash.equals(hash);
    }

    /**
     * Utils for generating.
     **/

    private static String hashToHexString(int[] hashValues) {
        StringBuilder hexString = new StringBuilder();
        for (int hash : hashValues) {
            hexString.append(String.format("%08x", hash));
        }
        return hexString.toString();
    }

    private static String stringTo8BitBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            String binaryString = Integer.toBinaryString(c);
            binary.append(String.format("%8s", binaryString).replace(' ', '0'));
        }
        return binary.toString();
    }

    private static String padTo512Bits(String binaryMessage) {
        int originalLength = binaryMessage.length();
        binaryMessage += "1";
        while (binaryMessage.length() % 512 != 448) {
            binaryMessage += "0";
        }
        String lengthBinary = String.format("%64s", Integer.toBinaryString(originalLength)).replace(' ', '0');
        binaryMessage += lengthBinary;
        return binaryMessage;
    }

    private static void prepareMessageSchedule(String binaryMessage) {
        for (int i = 0; i < 512; i += 32) {
            String word = binaryMessage.substring(i, i + 32);
            messageSchedule.add(Integer.parseUnsignedInt(word, 2));
        }
    }

    private static void expandMsgSkd() {
        for (int i = 16; i < 64; i++) {
            int s0 = sigma0(messageSchedule.get(i - 15));
            int s1 = sigma1(messageSchedule.get(i - 2));
            int newWord = s1 + messageSchedule.get(i - 7) + s0 + messageSchedule.get(i - 16);

            messageSchedule.add(newWord & ttMask);
        }
    }

    private static void compress() {

        int a = hashValues[0], b = hashValues[1], c = hashValues[2], d = hashValues[3];
        int e = hashValues[4], f = hashValues[5], g = hashValues[6], h = hashValues[7];

        for (int i = 0; i < 64; i++) {
            int T1 = h + bigSigma1(e) + xor(e, f, g) + K[i] + messageSchedule.get(i);
            int T2 = bigSigma0(a) + maj(a, b, c);
            h = g;
            g = f;
            f = e;
            e = d + T1;
            d = c;
            c = b;
            b = a;
            a = T1 + T2;
        }

        hashValues[0] = (hashValues[0] + a) & 0xFFFFFFFF;
        hashValues[1] = (hashValues[1] + b) & 0xFFFFFFFF;
        hashValues[2] = (hashValues[2] + c) & 0xFFFFFFFF;
        hashValues[3] = (hashValues[3] + d) & 0xFFFFFFFF;
        hashValues[4] = (hashValues[4] + e) & 0xFFFFFFFF;
        hashValues[5] = (hashValues[5] + f) & 0xFFFFFFFF;
        hashValues[6] = (hashValues[6] + g) & 0xFFFFFFFF;
        hashValues[7] = (hashValues[7] + h) & 0xFFFFFFFF;
    }


    /*
     *
     * Additional helper methods
     *
     * */

    private static int bigSigma0(int x) {
        return Integer.rotateRight(x, 2) ^ Integer.rotateRight(x, 13) ^ Integer.rotateRight(x, 22);
    }

    private static int bigSigma1(int x) {
        return Integer.rotateRight(x, 6) ^ Integer.rotateRight(x, 11) ^ Integer.rotateRight(x, 25);
    }

    private static int sigma0(int x) {
        return Integer.rotateRight(x, 7) ^ Integer.rotateRight(x, 18) ^ (x >>> 3);
    }

    private static int sigma1(int x) {
        return Integer.rotateRight(x, 17) ^ Integer.rotateRight(x, 19) ^ (x >>> 10);
    }

    private static int xor(int x, int y, int z) {
        return (x & y) ^ (~x & z);
    }

    private static int maj(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }
}
