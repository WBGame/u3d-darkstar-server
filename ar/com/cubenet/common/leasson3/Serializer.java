package ar.com.cubenet.common.leasson3;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Clase utilitaria para codificar / decodificar mensajes.
 * 
 * @author Sebastián Perruolo
 * 
 */
public class Serializer {
	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	public static ByteBuffer encodeString(final String s) {
		// System.out.println("codificando " + s);
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

	/**
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 * 
	 * @param buf
	 *            the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
	public static String decodeString(final ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			String result = new String(bytes, MESSAGE_CHARSET);
			// System.out.println("decodificando " + result);
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}
}
