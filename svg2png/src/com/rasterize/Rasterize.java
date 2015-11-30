package com.rasterize;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Rasterize {

	public static class RasterizerException extends Exception {

		public RasterizerException() {
		}

		public RasterizerException(String message) {
			super(message);
		}

		public RasterizerException(Throwable cause) {
			super(cause);
		}

		public RasterizerException(String message, Throwable cause) {
			super(message, cause);
		}

		public RasterizerException(String message, Throwable cause,
				boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

	}

    public static void svgToPngFileStream(String inFile, String outFile) throws RasterizerException, IOException {
        String textSvg = new String(Files.readAllBytes(Paths.get(inFile)));
        FileOutputStream pngOut = new FileOutputStream(outFile);
        svgTextToPngStream(textSvg, pngOut);
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
	public static void main(String[] args) throws IOException, RasterizerException {
        if (args.length < 2) {
            throw new IllegalArgumentException("input and output files required.");
        }
		String textSvg = new String(Files.readAllBytes(Paths.get("resources/circle.svg")));
		String pngFileName = "output/circle.png";
		FileOutputStream pngOut = new FileOutputStream(pngFileName);
		svgTextToPngStream(textSvg, pngOut);
	}
}
