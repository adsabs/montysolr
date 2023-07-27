package org.adsabs;

import org.apache.solr.common.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


public class InvenioBitSet extends BitSet {

    private static final long serialVersionUID = 2837978456235402780L;

    public InvenioBitSet() {
        super();
    }

    public InvenioBitSet(int nbits) {
        super(nbits);
    }

    /**
     * As opposed to .fastLoad() here you are passing uncompressed bytes
     *
     * @param bytes byte array
     */
    public InvenioBitSet(byte[] bytes) {
        super(bytes == null ? 0 : bytes.length * 8);
        load(this, bytes);
    }

    /**
     * Invenio equivalent of .fastdump()
     *
     * @return byte[]
     */
    public byte[] fastDump() throws IOException {
        byte[] input = this.toByteArray();
        return compress(input);
    }

    /**
     * Invenio equivalent of .fastload() - at least on the face of it, I didn't
     * check (nor care for efficiency; so I don't know....)
     *
     * @param input output of the python fastdump
     * @return Invenio bitset
     */

    public static InvenioBitSet fastLoad(byte[] input) throws DataFormatException, IOException {
        InvenioBitSet bitSet = new InvenioBitSet();

        byte[] result = decompress(input);

        load(bitSet, result);

        return bitSet;
    }

    private static void load(InvenioBitSet bitset, byte[] data) {
        int i = 0;
        for (byte b : data) {
            for (int mask = 0x01; mask != 0x100; mask <<= 1) {
                if ((b & mask) != 0) {
                    bitset.set(i);
                }
                i += 1;
            }
        }
    }

    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream zipStream = new DeflaterOutputStream(baos);
        zipStream.write(data);
        zipStream.flush();
        zipStream.close();
        return baos.toByteArray();
    }

    private static byte[] decompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        InflaterInputStream zipStream = new InflaterInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = zipStream.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        bais.close();
        zipStream.close();
        return baos.toByteArray();
    }

    // helper function for debugging
    public static String getHexString(byte[] b) throws Exception {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Returns Base64 encoded (compressed) representation of this bitset
     * suitable for sending data over wire
     *
     * @return string base64 representation
     */
    public String toBase64() throws IOException {
        byte[] data = this.fastDump();
        return Base64.byteArrayToBase64(data, 0, data.length);
    }

}