package br.com.vexillum.util;

import java.io.InputStream;

import org.zkoss.util.media.Media;

public class ZKUtils {

	@SuppressWarnings("resource")
	public static InputStream mediaToStream(Media media) throws Exception{
		return (media.isBinary() ? media.getStreamData() : new ReaderInputStream(media.getReaderData()));
	}
	
}
