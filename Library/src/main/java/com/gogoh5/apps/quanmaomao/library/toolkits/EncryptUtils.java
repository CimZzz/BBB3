package com.gogoh5.apps.quanmaomao.library.toolkits;

import java.util.Random;

public final class EncryptUtils {
    static {
        System.loadLibrary("encrypt");
    }

    public static native byte[] encode(String str, byte[] key);
    public static native byte[] decode(byte [] key);

    public static byte[] randomKey() {
        Random random = new Random();
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            int value = random.nextInt(94) + 32;    // printable character.
            result[i] = (byte) value;
        }
        return result;
    }
}

