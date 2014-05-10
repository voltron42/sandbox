package com.rasterize;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class Rasterize {

	public static class RasterizerException extends Exception {

		public RasterizerException() {
			// TODO Auto-generated constructor stub
		}

		public RasterizerException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public RasterizerException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}

		public RasterizerException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public RasterizerException(String message, Throwable cause,
				boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

	}
	
	public static void svgTextToPngStream(String svgText, OutputStream pngStream) throws RasterizerException {
		ByteArrayInputStream svgStream = new ByteArrayInputStream(svgText.getBytes());
		TranscoderInput svgInput = new TranscoderInput(svgStream);
		TranscoderOutput pngOutput = new TranscoderOutput(pngStream);
		PNGTranscoder ptc = new PNGTranscoder();
		try {
			ptc.transcode(svgInput, pngOutput);
		} catch (TranscoderException e) {
			throw new RasterizerException(e);
		}
	}

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws RasterizerException 
	 */
	public static void main(String[] args) throws FileNotFoundException, RasterizerException {
		String textSvg = "<svg xmlns=\"http://www.w3.org/2000/svg\"  width=\"50\" height=\"50\"><circle r=\"25\" cx=\"25\" cy=\"25\" fill=\"red\" stroke=\"blue\"/></svg>";
		String pngFileName = "circle.png";
		FileOutputStream pngOut = new FileOutputStream(pngFileName);
		svgTextToPngStream(textSvg, pngOut);
	}
}
