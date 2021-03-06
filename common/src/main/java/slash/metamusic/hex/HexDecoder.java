/*
  You may freely copy, distribute, modify and use this class as long
  as the original author attribution remains intact.  See message
  below.

  Copyright (C) 2003 Christian Pesch. All Rights Reserved.
 */

package slash.metamusic.hex;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Helps to decode hexadecimally encoded digits to bytes, shorts,
 * ints or a hexadecimally encoded Writer to an OutputStream.
 *
 * @author Christian Pesch
 * @version $Id: HexDecoder.java 167 2003-12-08 11:41:43Z cpesch $
 */

public class HexDecoder extends Writer {
    private OutputStream outputStream;

    public HexDecoder(OutputStream stream) {
        this.outputStream = stream;
    }

    public void write(String stringified, int from, int length) throws IOException {
        byte[] result = new byte[length / 2];

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) ((decoding[stringified.charAt(from + 2 * i)] << 4) +
                    decoding[stringified.charAt(from + 1 + 2 * i)]);
        }
        outputStream.write(result);
    }

    public void write(char[] stringified, int from, int length) throws IOException {
        write(new String(stringified), from, length);
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }

    /**
     * Decode the given string to a byte array. Requires, that the string is a
     * sequence of hex digits of even length.
     *
     * @param string the hex string to be decoded
     * @return the decoded bytes, where result[i] is computed from string.charAt(2*i) and string.charAt(2*i+1)
     * @see Character#digit
     */
    public static byte[] decodeBytes(String string) {
        if (string.length() % 2 != 0)
            throw new IllegalArgumentException("Not an even number of hex digits: " + string);
        byte[] result = new byte[string.length() / 2];
        for (int i = 0; i < result.length; ++i) {
            int highNibble = Character.digit(string.charAt(2 * i), 16);
            int lowNibble = Character.digit(string.charAt(2 * i + 1), 16);
            if (highNibble == -1 || lowNibble == -1)
                throw new IllegalArgumentException("Not a string of hex digits: " + string);
            result[i] = (byte) (highNibble * 16 + lowNibble);
        }
        return result;
    }


    private static byte[] decoding = new byte['g'];

    static {
        decoding['0'] = 0;
        decoding['1'] = 1;
        decoding['2'] = 2;
        decoding['3'] = 3;
        decoding['4'] = 4;
        decoding['5'] = 5;
        decoding['6'] = 6;
        decoding['7'] = 7;
        decoding['8'] = 8;
        decoding['9'] = 9;
        decoding['a'] = 10;
        decoding['b'] = 11;
        decoding['c'] = 12;
        decoding['d'] = 13;
        decoding['e'] = 14;
        decoding['f'] = 15;
        decoding['A'] = 10;
        decoding['B'] = 11;
        decoding['C'] = 12;
        decoding['D'] = 13;
        decoding['E'] = 14;
        decoding['F'] = 15;
    }

    public static byte decodeByte(String string, int pos) throws IndexOutOfBoundsException {
        return (byte) ((decoding[string.charAt(pos)] << 4) + decoding[string.charAt(pos + 1)]);
    }

    public static short decodeShort(String string, int pos) throws IndexOutOfBoundsException {
        return (short) ((decoding[string.charAt(pos)] << 12) + (decoding[string.charAt(pos + 1)] << 8) +
                (decoding[string.charAt(pos + 2)] << 4) + decoding[string.charAt(pos + 3)]);
    }

    public static int decodeInt(String string, int pos) throws IndexOutOfBoundsException {
        return ((decoding[string.charAt(pos)] << 28) + (decoding[string.charAt(pos + 1)] << 24) +
                (decoding[string.charAt(pos + 2)] << 20) + (decoding[string.charAt(pos + 3)] << 16) +
                (decoding[string.charAt(pos) + 4] << 12) + (decoding[string.charAt(pos + 5)] << 8) +
                (decoding[string.charAt(pos + 6)] << 4) + decoding[string.charAt(pos + 7)]);
    }
}
