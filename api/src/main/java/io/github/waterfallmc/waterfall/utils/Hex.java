package io.github.waterfallmc.waterfall.utils;

import java.util.Arrays;
import java.util.Objects;

public class Hex {

    public static byte[] decode(CharSequence chars) {
        byte[] bytes = new byte[chars.length() >> 1];
        decode(chars, 0, bytes, 0, bytes.length);
        return bytes;
    }

    public static void decode(char[] chars, int charOffset, byte[] dest, int offset, int length) {
        decode(new CharSequence() {
            @Override
            public int length() {
                return chars.length;
            }

            @Override
            public char charAt(int index) {
                return chars[index];
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return toString().substring(start, end);
            }

            @Override
            public String toString() {
                return new String(chars, charOffset, chars.length);
            }
        });
    }

    public static void decode(CharSequence chars, int charOffset, byte[] dest, int offset, int length) {
        Objects.requireNonNull(chars, "Null chars");
        Objects.requireNonNull(chars, "Null destination");
        final int numChars = chars.length();
        if ((numChars & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters: " + numChars);
        } else if (length < (numChars - charOffset) >> 1) {
            throw new IllegalArgumentException("Too many bytes to fill with " + numChars + " characters: " + length);
        } else if (offset < 0 || charOffset < 0 || length < 0 || length * 2 > numChars - charOffset || length > dest.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0, charIndex = charOffset; i < length; i++) {
            char first = chars.charAt(charIndex++);
            char second = chars.charAt(charIndex++);
            dest[i + offset] = (byte) ((toDigit(first) << 4) | (toDigit(second)));
        }
    }

    public static String encodeString(byte[] bytes) {
        return new String(encode(bytes));
    }

    public static char[] encode(byte[] bytes) {
        char[] chars = new char[bytes.length << 1];
        encode(chars, 0, bytes, 0, bytes.length);
        return chars;
    }

    public static void encode(char[] chars, int charOffset, byte[] source, int offset, int length) {
        Objects.requireNonNull(chars, "Null chars");
        Objects.requireNonNull(chars, "Null bytes");
        if (offset < 0 || charOffset < 0 || length < 0 || length * 2 > chars.length - charOffset || length > source.length - offset) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return;
        }
        for (int i = 0, charIndex = charOffset; i < length; i++) {
            byte b = source[i + offset];
            chars[charIndex++] = fromDigit((byte) ((b >> 4) & 0xF));
            chars[charIndex++] = fromDigit((byte) (b & 0xF));
        }
    }
    private static final char[] ENCODE_TABLE = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static final byte[] DECODE_TABLE = new byte[128];

    static {
        Arrays.fill(DECODE_TABLE, (byte) -1);
        for (int value = 0; value < ENCODE_TABLE.length; value++) {
            char c = ENCODE_TABLE[value];
            DECODE_TABLE[c] = (byte) value;
            char upper;
            if ((upper = Character.toUpperCase(c)) != c) {
                DECODE_TABLE[upper] = (byte) value;
            }
        }
    }

    public static byte toDigit(char c) {
        byte value;
        if (c < DECODE_TABLE.length) {
            value = DECODE_TABLE[c];
        } else {
            value = -1;
        }
        if (value < 0) throw new IllegalArgumentException("Invalid character " + c);
        return value;
    }

    private static char fromDigit(byte b) {
        assert (b & 0xF) == b : "Out of range " + b;
        return ENCODE_TABLE[b];
    }
}