package ar.com.cubenet.server.leasson2;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Transcoder {

	/** 
	 * The message encoding. (Esto deberia negociarse con el cliente y no ser 
	 * estatico). 
	 */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 *
	 * @param s the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	public static ByteBuffer encodeString(String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET +
					" not found", e);
		}
	}

	/**
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 *
	 * @param buf the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
	public static String decodeString(ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET +
					" not found", e);
		}
	}

}
