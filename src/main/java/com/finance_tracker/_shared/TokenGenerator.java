package com.finance_tracker._shared;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom(); // Cryptographically strong random number generator

    /**
     * Generates a random hexadecimal token of a specified byte length.
     * Each byte translates to two hexadecimal characters, so a 16-byte token
     * will result in a 32-character hex string.
     *
     * @param byteLength The desired length of the token in bytes.
     * @return A randomly generated hexadecimal string.
     */
    public static String generateHexToken(int byteLength) {
        byte[] bytes = new byte[byteLength];

        secureRandom.nextBytes(bytes);
        return new BigInteger(1, bytes).toString(16);
    }
}
