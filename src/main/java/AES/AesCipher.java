package AES;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher {
    private static final String CRYPT_ALGORITHM = "AES";
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final String CHAR_ENCODING = "UTF-8";

    private String mCryptKey;
    private String mCryptIV;

    /**
     * Set crypt key
     *
     * @param cryptKey16CharStr
     * @param cryptInitializationVector16CharStr
     */
    public void setCryptKey(String cryptKey16CharStr, String cryptInitializationVector16CharStr) {

        mCryptKey = cryptKey16CharStr;
        mCryptIV = cryptInitializationVector16CharStr;
        checkKeys();
    }

    /**
     * Encrypt text to encrypted-text
     *
     * @param text
     * @return
     */
    public String encrypt(String text) {
        checkKeys();
        if (text == null) {
            return null;
        }
        String retVal = null;

        try {

            final SecretKeySpec key = new SecretKeySpec(mCryptKey.getBytes(CHAR_ENCODING), CRYPT_ALGORITHM);
            final IvParameterSpec iv = new IvParameterSpec(mCryptIV.getBytes(CHAR_ENCODING));

            final Cipher cipher = Cipher.getInstance(PADDING);

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] encrypted = cipher.doFinal(text.getBytes(CHAR_ENCODING));

            retVal = new String(encodeHex(encrypted));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Decrypt encrypted-text
     *
     * @param text
     * @return
     */
    public String decrypt(String text) {

        System.out.println("Decrypting...");
        checkKeys();

        if (text == null) {
            return null;
        }

        String retVal = null;

        try {

            final SecretKeySpec secretKeySpec = new SecretKeySpec(mCryptKey.getBytes(CHAR_ENCODING), CRYPT_ALGORITHM);
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(mCryptIV.getBytes(CHAR_ENCODING));

            final Cipher cipher = Cipher.getInstance(PADDING);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            final byte[] decrypted = cipher.doFinal(decodeHex(text.toCharArray()));

            retVal = new String(decrypted, CHAR_ENCODING);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Check if key is compliant
     */
    private void checkKeys() {
        if (mCryptKey == null || mCryptKey.length() != 16 || mCryptIV == null || mCryptIV.length() != 16) {
            throw new RuntimeException("Please call setCryptKey before enctypt/decrypt and make sure that the length of the cryptKey16CharStr is 16 characters");
        }

    }

    /**
     *
     * Converts an array of characters representing hexadecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements. <br>
     * Portion of Apache Software Foundation
     *
     * @param data
     *            An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied
     *         char array.
     * @throws Exception
     *             Thrown if an odd number or illegal of characters is supplied
     *
     *
     */
    private byte[] decodeHex(char[] data) throws Exception {

        int len = data.length;
        System.out.println(len);
        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {

            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer. <br>
     * Portion of Apache Software Foundation
     *
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source
     * @return An integer
     * @throws Exception
     *             Thrown if ch is an illegal hex character
     */
    private int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte. <br>
     * Portion of Apache Software Foundation
     *
     * @param data
     *            a byte[] to convert to Hex characters
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     *
     *
     */
    private char[] encodeHex(byte[] data) {

        final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }
}
