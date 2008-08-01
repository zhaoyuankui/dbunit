/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.dbunit.util;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * I am placing this code in the Public Domain. Do with it as you will.
 * This software comes with no guarantees or warranties but with
 * plenty of well-wishing instead!
 * Please visit <a href="http://iharder.net/base64">http://iharder.net/base64</a>
 * periodically to check for updates or to contribute improvements.
 * </p>
 *
 * @author Robert Harder (rharder@usa.net)
 * @author Last changed by: $Author$
 * @version $Revision$ $Date$
 * @since 1.3
 */
public class Base64
{

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(Base64.class);

    /** Specify encoding (value is <tt>true</tt>). */
    public final static boolean ENCODE = true;


    /** Specify decoding (value is <tt>false</tt>). */
    public final static boolean DECODE = false;


    /** Maximum line length (76) of Base64 output. */
    private final static int MAX_LINE_LENGTH = 76;


    /** The equals sign (=) as a byte. */
    private final static byte EQUALS_SIGN = (byte)'=';


    /** The new line character (\n) as a byte. */
    private final static byte NEW_LINE = (byte)'\n';


    /** The 64 valid Base64 values. */
    private final static byte[] ALPHABET =
            {
                (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G',
                (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N',
                (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U',
                (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z',
                (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
                (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n',
                (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u',
                (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z',
                (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
                (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/'
            };

    /**
     * Translates a Base64 value to either its 6-bit reconstruction value
     * or a negative number indicating some other meaning.
     **/
    private final static byte[] DECODABET =
            {
                -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal  0 -  8
                -5, -5, // Whitespace: Tab and Linefeed
                -9, -9, // Decimal 11 - 12
                -5, // Whitespace: Carriage Return
                -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 - 26
                -9, -9, -9, -9, -9, // Decimal 27 - 31
                -5, // Whitespace: Space
                -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
                62, // Plus sign at decimal 43
                -9, -9, -9, // Decimal 44 - 46
                63, // Slash at decimal 47
                52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
                -9, -9, -9, // Decimal 58 - 60
                -1, // Equals sign at decimal 61
                -9, -9, -9, // Decimal 62 - 64
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through 'N'
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O' through 'Z'
                -9, -9, -9, -9, -9, -9, // Decimal 91 - 96
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a' through 'm'
                39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n' through 'z'
                -9, -9, -9, -9                                 // Decimal 123 - 126
                /*,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 127 - 139
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 140 - 152
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 153 - 165
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 166 - 178
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 179 - 191
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 192 - 204
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 205 - 217
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 218 - 230
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 231 - 243
                -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9         // Decimal 244 - 255 */
            };

    private final static byte BAD_ENCODING = -9; // Indicates error in encoding
    private final static byte WHITE_SPACE_ENC = -5; // Indicates white space in encoding
    private final static byte EQUALS_SIGN_ENC = -1; // Indicates equals sign in encoding


    /** Defeats instantiation. */
    private Base64()
    {
    }


    /** Testing. */
    public static void main(String[] args)
    {
        logger.debug("main(args=" + args + ") - start");

        String s = "Hello, world";
        s = "abcd";
        //s = System.getProperties().toString();
        //System.out.println( s + ": \n [" + encode( s ) + "]\n [" + decode(encode(s)) + "]" );

        byte[] b = encodeString(s).getBytes();
        byte[] c = decode(b, 0, b.length);

        System.out.println("\n\n" + s + ":" + new String(b) + ":" + new String(c));

        try
        {
            FileInputStream fis = new FileInputStream("c:\\abcd.txt");
            InputStream b64is = new InputStream(fis, DECODE);
            int ib = 0;
            while ((ib = b64is.read()) > 0)
            {   //System.out.print( new String( ""+(char)ib ) );
            }
        }   // end try
        catch (Exception e)
        {
            logger.error("main()", e);

            e.printStackTrace();
        }
    }


/* ********  E N C O D I N G   M E T H O D S  ******** */


    /**
     * Encodes the first three bytes of array <var>threeBytes</var>
     * and returns a four-byte array in Base64 notation.
     *
     * @param threeBytes the array to convert
     * @return four byte array in Base64 notation.
     * @since 1.3
     */
    private static byte[] encode3to4(byte[] threeBytes)
    {
        logger.debug("encode3to4(threeBytes=" + threeBytes + ") - start");

        return encode3to4(threeBytes, 3);
    }   // end encodeToBytes


    /**
     * Encodes up to the first three bytes of array <var>threeBytes</var>
     * and returns a four-byte array in Base64 notation.
     * The actual number of significant bytes in your array is
     * given by <var>numSigBytes</var>.
     * The array <var>threeBytes</var> needs only be as big as
     * <var>numSigBytes</var>.
     *
     * @param threeBytes the array to convert
     * @param numSigBytes the number of significant bytes in your array
     * @return four byte array in Base64 notation.
     * @since 1.3
     */
    private static byte[] encode3to4(byte[] threeBytes, int numSigBytes)
    {
        logger.debug("encode3to4(threeBytes=" + threeBytes + ", numSigBytes=" + numSigBytes + ") - start");

        byte[] dest = new byte[4];
        encode3to4(threeBytes, 0, numSigBytes, dest, 0);
        return dest;
    }


    /**
     * Encodes up to three bytes of the array <var>source</var>
     * and writes the resulting four Base64 bytes to <var>destination</var>.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays
     * are large enough to accomodate <var>srcOffset</var> + 3 for
     * the <var>source</var> array or <var>destOffset</var> + 4 for
     * the <var>destination</var> array.
     * The actual number of significant bytes in your array is
     * given by <var>numSigBytes</var>.
     *
     * @param source the array to convert
     * @param srcOffset the index where conversion begins
     * @param numSigBytes the number of significant bytes in your array
     * @param destination the array to hold the conversion
     * @param destOffset the index where output will be put
     * @return the <var>destination</var> array
     * @since 1.3
     */
    private static byte[] encode3to4(
            byte[] source, int srcOffset, int numSigBytes,
            byte[] destination, int destOffset)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("encode3to4(source=" + source + ", srcOffset=" + srcOffset + ", numSigBytes=" + numSigBytes
                + ", destination=" + destination + ", destOffset=" + destOffset + ") - start");

        //           1         2         3
        // 01234567890123456789012345678901 Bit position
        // --------000000001111111122222222 Array position from threeBytes
        // --------|    ||    ||    ||    | Six bit groups to index ALPHABET
        //          >>18  >>12  >> 6  >> 0  Right shift necessary
        //                0x3f  0x3f  0x3f  Additional AND

        // Create buffer with zero-padding if there are only one or two
        // significant bytes passed in the array.
        // We have to shift left 24 in order to flush out the 1's that appear
        // when Java treats a value as negative that is cast from a byte to an int.
        int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
                | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
                | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

        switch (numSigBytes)
        {
            case 3:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
                destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
                return destination;

            case 2:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;

            case 1:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;

            default:
                return destination;
        }   // end switch
    }   // end encode3to4


    /**
     * Serializes an object and returns the Base64-encoded
     * version of that serialized object. If the object
     * cannot be serialized or there is another error,
     * the method will return <tt>null</tt>.
     *
     * @param serializableObject The object to encode
     * @return The Base64-encoded object
     * @since 1.4
     */
    public static String encodeObject(java.io.Serializable serializableObject)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("encodeObject(serializableObject=" + serializableObject + ") - start");

        java.io.ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        java.io.ObjectOutputStream oos = null;

        try
        {
            baos = new java.io.ByteArrayOutputStream();
            b64os = new OutputStream(baos, Base64.ENCODE);
            oos = new java.io.ObjectOutputStream(b64os);

            oos.writeObject(serializableObject);
        }   // end try
        catch (java.io.IOException e)
        {
            logger.error("encodeObject()", e);

            e.printStackTrace();
            return null;
        }   // end catch
        finally
        {
            try
            {
                oos.close();
            }
            catch (Exception e)
            {
                logger.error("encodeObject()", e);
            }
            try
            {
                b64os.close();
            }
            catch (Exception e)
            {
                logger.error("encodeObject()", e);
            }
            try
            {
                baos.close();
            }
            catch (Exception e)
            {
                logger.error("encodeObject()", e);
            }
        }   // end finally

        return new String(baos.toByteArray());
    }   // end encode


    /**
     * Encodes a byte array into Base64 notation.
     * Equivalen to calling
     * <code>encodeBytes( source, 0, source.length )</code>
     *
     * @param source The data to convert
     * @since 1.4
     */
    public static String encodeBytes(byte[] source)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("encodeBytes(source=" + source + ") - start");

        return encodeBytes(source, 0, source.length);
    }   // end encodeBytes


    /**
     * Encodes a byte array into Base64 notation.
     *
     * @param source The data to convert
     * @param off Offset in array where conversion should begin
     * @param len Length of data to convert
     * @since 1.4
     */
    public static String encodeBytes(byte[] source, int off, int len)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("encodeBytes(source=" + source + ", off=" + off + ", len=" + len + ") - start");

        int len43 = len * 4 / 3;
        byte[] outBuff = new byte[(len43)                      // Main 4:3
                + ((len % 3) > 0 ? 4 : 0)      // Account for padding
                + (len43 / MAX_LINE_LENGTH)]; // New lines
        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        for (; d < len2; d += 3, e += 4)
        {
            encode3to4(source, d, 3, outBuff, e);

            lineLength += 4;
            if (lineLength == MAX_LINE_LENGTH)
            {
                outBuff[e + 4] = NEW_LINE;
                e++;
                lineLength = 0;
            }   // end if: end of line
        }   // en dfor: each piece of array

        if (d < len)
        {
            encode3to4(source, d, len - d, outBuff, e);
            e += 4;
        }   // end if: some padding needed

        return new String(outBuff, 0, e);
    }   // end encodeBytes


    /**
     * Encodes a string in Base64 notation with line breaks
     * after every 75 Base64 characters.
     *
     * @param s the string to encode
     * @return the encoded string
     * @since 1.3
     */
    public static String encodeString(String s)
    {
        logger.debug("encodeString(s={}) - start", s);

        return encodeBytes(s.getBytes());
    }   // end encodeString




/* ********  D E C O D I N G   M E T H O D S  ******** */


    /**
     * Decodes the first four bytes of array <var>fourBytes</var>
     * and returns an array up to three bytes long with the
     * decoded values.
     *
     * @param fourBytes the array with Base64 content
     * @return array with decoded values
     * @since 1.3
     */
    private static byte[] decode4to3(byte[] fourBytes)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("decode4to3(fourBytes=" + fourBytes + ") - start");

        byte[] outBuff1 = new byte[3];
        int count = decode4to3(fourBytes, 0, outBuff1, 0);
        byte[] outBuff2 = new byte[count];

        for (int i = 0; i < count; i++)
            outBuff2[i] = outBuff1[i];

        return outBuff2;
    }


    /**
     * Decodes four bytes from array <var>source</var>
     * and writes the resulting bytes (up to three of them)
     * to <var>destination</var>.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays
     * are large enough to accomodate <var>srcOffset</var> + 4 for
     * the <var>source</var> array or <var>destOffset</var> + 3 for
     * the <var>destination</var> array.
     * This method returns the actual number of bytes that
     * were converted from the Base64 encoding.
     *
     *
     * @param source the array to convert
     * @param srcOffset the index where conversion begins
     * @param destination the array to hold the conversion
     * @param destOffset the index where output will be put
     * @return the number of decoded bytes converted
     * @since 1.3
     */
    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("decode4to3(source=" + source + ", srcOffset=" + srcOffset + ", destination=" + destination
                + ", destOffset=" + destOffset + ") - start");

        // Example: Dk==
        if (source[srcOffset + 2] == EQUALS_SIGN)
        {
            int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                    | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12);

            destination[destOffset] = (byte)(outBuff >>> 16);
            return 1;
        }

        // Example: DkL=
        else if (source[srcOffset + 3] == EQUALS_SIGN)
        {
            int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                    | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
                    | ((DECODABET[source[srcOffset + 2]] << 24) >>> 18);

            destination[destOffset] = (byte)(outBuff >>> 16);
            destination[destOffset + 1] = (byte)(outBuff >>> 8);
            return 2;
        }

        // Example: DkLE
        else
        {
            int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                    | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
                    | ((DECODABET[source[srcOffset + 2]] << 24) >>> 18)
                    | ((DECODABET[source[srcOffset + 3]] << 24) >>> 24);

            destination[destOffset] = (byte)(outBuff >> 16);
            destination[destOffset + 1] = (byte)(outBuff >> 8);
            destination[destOffset + 2] = (byte)(outBuff);
            return 3;
        }
    }   // end decodeToBytes


    /**
     * Decodes data from Base64 notation.
     *
     * @param s the string to decode
     * @return the decoded data
     * @since 1.4
     */
    public static byte[] decode(String s)
    {
        logger.debug("decode(s={}) - start", s);

        byte[] bytes = s.getBytes();
        return decode(bytes, 0, bytes.length);
    }   // end decode


    /**
     * Decodes data from Base64 notation and
     * returns it as a string.
     * Equivalent to calling
     * <code>new String( decode( s ) )</code>
     *
     * @param s the string to decode
     * @return The data as a string
     * @since 1.4
     */
    public static String decodeToString(String s)
    {
        logger.debug("decodeToString(s={}) - start", s);

        return new String(decode(s));
    }   // end decodeToString


    /**
     * Attempts to decode Base64 data and deserialize a Java
     * Object within. Returns <tt>null if there was an error.
     *
     * @param encodedObject The Base64 data to decode
     * @return The decoded and deserialized object
     * @since 1.4
     */
    public static Object decodeToObject(String encodedObject)
    {
        logger.debug("decodeToObject(encodedObject={} - start", encodedObject);

        byte[] objBytes = decode(encodedObject);

        java.io.ByteArrayInputStream bais = null;
        java.io.ObjectInputStream ois = null;

        try
        {
            bais = new java.io.ByteArrayInputStream(objBytes);
            ois = new java.io.ObjectInputStream(bais);

            return ois.readObject();
        }   // end try
        catch (java.io.IOException e)
        {
            logger.error("decodeToObject()", e);

            e.printStackTrace();
            return null;
        }   // end catch
        catch (ClassNotFoundException e)
        {
            logger.error("decodeToObject()", e);

            e.printStackTrace();
            return null;
        }   // end catch
        finally
        {
            try
            {
                bais.close();
            }
            catch (Exception e)
            {
                logger.error("decodeToObject()", e);
            }
            try
            {
                ois.close();
            }
            catch (Exception e)
            {
                logger.error("decodeToObject()", e);
            }
        }   // end finally
    }   // end decodeObject


    /**
     * Decodes Base64 content in byte array format and returns
     * the decoded byte array.
     *
     * @param source The Base64 encoded data
     * @param off    The offset of where to begin decoding
     * @param len    The length of characters to decode
     * @return decoded data
     * @since 1.3
     */
    public static byte[] decode(byte[] source, int off, int len)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("decode(source=" + source + ", off=" + off + ", len=" + len + ") - start");

        int len34 = len * 3 / 4;
        byte[] outBuff = new byte[len34]; // Upper limit on size of output
        int outBuffPosn = 0;

        byte[] b4 = new byte[4];
        int b4Posn = 0;
        int i = 0;
        byte sbiCrop = 0;
        byte sbiDecode = 0;
        for (i = 0; i < len; i++)
        {
            sbiCrop = (byte)(source[i] & 0x7f); // Only the low seven bits
            sbiDecode = DECODABET[sbiCrop];

            if (sbiDecode >= WHITE_SPACE_ENC) // White space, Equals sign or better
            {
                if (sbiDecode >= EQUALS_SIGN_ENC)
                {
                    b4[b4Posn++] = sbiCrop;
                    if (b4Posn > 3)
                    {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                        b4Posn = 0;

                        // If that was the equals sign, break out of 'for' loop
                        if (sbiCrop == EQUALS_SIGN)
                            break;
                    }   // end if: quartet built

                }   // end if: equals sign or better

            }   // end if: white space, equals sign or better
            else
            {
                System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
                return null;
            }   // end else:
        }   // each input character

        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }   // end decode




    /* ********  I N N E R   C L A S S   I N P U T S T R E A M  ******** */



    /**
     * A {@link Base64.InputStream} will read data from another
     * {@link java.io.InputStream}, given in the constructor,
     * and encode/decode to/from Base64 notation on the fly.
     *
     * @see Base64
     * @see java.io.FilterInputStream
     * @since 1.3
     */
    public static class InputStream extends java.io.FilterInputStream
    {

        /**
         * Logger for this class
         */
        private static final Logger logger = LoggerFactory.getLogger(InputStream.class);

        private boolean encode;         // Encoding or decoding
        private int position;       // Current position in the buffer
        private byte[] buffer;         // Small buffer holding converted data
        private int bufferLength;   // Length of buffer (3 or 4)
        private int numSigBytes;    // Number of meaningful bytes in the buffer


        /**
         * Constructs a {@link Base64.InputStream} in DECODE mode.
         *
         * @param in the {@link java.io.InputStream} from which to read data.
         * @since 1.3
         */
        public InputStream(java.io.InputStream in)
        {
            this(in, Base64.DECODE);
        }   // end constructor


        /**
         * Constructs a {@link Base64.InputStream} in
         * either ENCODE or DECODE mode.
         *
         * @param in the {@link java.io.InputStream} from which to read data.
         * @param encode Conversion direction
         * @see Base64#ENCODE
         * @see Base64#DECODE
         * @since 1.3
         */
        public InputStream(java.io.InputStream in, boolean encode)
        {
            super(in);
            this.encode = encode;
            this.bufferLength = encode ? 4 : 3;
            this.buffer = new byte[bufferLength];
            this.position = -1;
        }   // end constructor

        /**
         * Reads enough of the input stream to convert
         * to/from Base64 and returns the next byte.
         *
         * @return next byte
         * @since 1.3
         */
        public int read() throws java.io.IOException
        {
            logger.debug("read() - start");

            // Do we need to get data?
            if (position < 0)
            {
                if (encode)
                {
                    byte[] b3 = new byte[3];
                    numSigBytes = 0;
                    for (int i = 0; i < 3; i++)
                    {
                        try
                        {
                            int b = in.read();

                            // If end of stream, b is -1.
                            if (b >= 0)
                            {
                                b3[i] = (byte)b;
                                numSigBytes++;
                            }   // end if: not end of stream

                        }   // end try: read
                        catch (java.io.IOException e)
                        {
                            logger.error("read()", e);

                            // Only a problem if we got no data at all.
                            if (i == 0)
                                throw e;

                        }   // end catch
                    }   // end for: each needed input byte

                    if (numSigBytes > 0)
                    {
                        encode3to4(b3, 0, numSigBytes, buffer, 0);
                        position = 0;
                    }   // end if: got data
                }   // end if: encoding

                // Else decoding
                else
                {
                    byte[] b4 = new byte[4];
                    int i = 0;
                    for (i = 0; i < 4; i++)
                    {
                        int b = 0;
                        do
                        {
                            b = in.read();
                        }
                        while (b >= 0 && DECODABET[b & 0x7f] < WHITE_SPACE_ENC);

                        if (b < 0)
                            break; // Reads a -1 if end of stream

                        b4[i] = (byte)b;
                    }   // end for: each needed input byte

                    if (i == 4)
                    {
                        numSigBytes = decode4to3(b4, 0, buffer, 0);
                        position = 0;
                    }   // end if: got four characters

                }   // end else: decode
            }   // end else: get data

            // Got data?
            if (position >= 0)
            {
                // End of relevant data?
                if (position >= numSigBytes)
                    return -1;

                int b = buffer[position++];

                if (position >= bufferLength)
                    position = -1;

                return b;
            }   // end if: position >= 0

            // Else error
            else
                return -1;
        }   // end read


        /**
         * Calls {@link #read} repeatedly until the end of stream
         * is reached or <var>len</var> bytes are read.
         * Returns number of bytes read into array or -1 if
         * end of stream is encountered.
         *
         * @param dest array to hold values
         * @param off offset for array
         * @param len max number of bytes to read into array
         * @return bytes read into array or -1 if end of stream is encountered.
         * @since 1.3
         */
        public int read(byte[] dest, int off, int len) throws java.io.IOException
        {
        	if(logger.isDebugEnabled())
        		logger.debug("read(dest=" + dest + ", off=" + off + ", len=" + len + ") - start");

            int i;
            int b;
            for (i = 0; i < len; i++)
            {
                b = read();

                if (b < 0)
                    return -1;

                dest[off + i] = (byte)b;
            }   // end for: each byte read
            return i;
        }   // end read

    }   // end inner class InputStream






    /* ********  I N N E R   C L A S S   O U T P U T S T R E A M  ******** */



    /**
     * A {@link Base64.OutputStream} will write data to another
     * {@link java.io.OutputStream}, given in the constructor,
     * and encode/decode to/from Base64 notation on the fly.
     *
     * @see Base64
     * @see java.io.FilterOutputStream
     * @since 1.3
     */
    public static class OutputStream extends java.io.FilterOutputStream
    {

        /**
         * Logger for this class
         */
        private static final Logger logger = LoggerFactory.getLogger(OutputStream.class);

        private boolean encode;
        private int position;
        private byte[] buffer;
        private int bufferLength;
        private int lineLength;


        /**
         * Constructs a {@link Base64.OutputStream} in ENCODE mode.
         *
         * @param out the {@link java.io.OutputStream} to which data will be written.
         * @since 1.3
         */
        public OutputStream(java.io.OutputStream out)
        {
            this(out, Base64.ENCODE);
        }   // end constructor


        /**
         * Constructs a {@link Base64.OutputStream} in
         * either ENCODE or DECODE mode.
         *
         * @param out the {@link java.io.OutputStream} to which data will be written.
         * @param encode Conversion direction
         * @see Base64#ENCODE
         * @see Base64#DECODE
         * @since 1.3
         */
        public OutputStream(java.io.OutputStream out, boolean encode)
        {
            super(out);
            this.encode = encode;
            this.bufferLength = encode ? 3 : 4;
            this.buffer = new byte[bufferLength];
            this.position = 0;
            this.lineLength = 0;
        }   // end constructor


        /**
         * Writes the byte to the output stream after
         * converting to/from Base64 notation.
         * When encoding, bytes are buffered three
         * at a time before the output stream actually
         * gets a write() call.
         * When decoding, bytes are buffered four
         * at a time.
         *
         * @param theByte the byte to write
         * @since 1.3
         */
        public void write(int theByte) throws java.io.IOException
        {
        	if(logger.isDebugEnabled())
        		logger.debug("write(theByte=" + theByte + ") - start");

            buffer[position++] = (byte)theByte;
            if (position >= bufferLength)
            {
                if (encode)
                {
                    out.write(Base64.encode3to4(buffer, bufferLength));

                    lineLength += 4;
                    if (lineLength >= MAX_LINE_LENGTH)
                    {
                        out.write(NEW_LINE);
                        lineLength = 0;
                    }   // end if: end o fline
                }   // end if: encoding
                else
                    out.write(Base64.decode4to3(buffer));

                position = 0;
            }   // end if: convert and flush
        }   // end write


        /**
         * Calls {@link #write} repeatedly until <var>len</var>
         * bytes are written.
         *
         * @param theBytes array from which to read bytes
         * @param off offset for array
         * @param len max number of bytes to read into array
         * @since 1.3
         */
        public void write(byte[] theBytes, int off, int len) throws java.io.IOException
        {
        	if(logger.isDebugEnabled())
        		logger.debug("write(theBytes=" + theBytes + ", off=" + off + ", len=" + len + ") - start");

            for (int i = 0; i < len; i++)
            {
                write(theBytes[off + i]);
            }   // end for: each byte written

        }   // end write


        /**
         * Appropriately pads Base64 notation when encoding
         * or throws an exception if Base64 input is not
         * properly padded when decoding.
         *
         * @since 1.3
         */
        public void flush() throws java.io.IOException
        {
            logger.debug("flush() - start");

            if (position > 0)
            {
                if (encode)
                {
                    out.write(Base64.encode3to4(buffer, position));
                }   // end if: encoding
                else
                {
                    throw new java.io.IOException("Base64 input not properly padded.");
                }   // end else: decoding
            }   // end if: buffer partially full

            super.flush();
            out.flush();
        }   // end flush


        /**
         * Flushes and closes stream.
         *
         * @since 1.3
         */
        public void close() throws java.io.IOException
        {
            logger.debug("close() - start");

            this.flush();

            super.close();
            out.close();

            buffer = null;
            out = null;
        }   // end close

    }   // end inner class OutputStream


}   // end class Base64



