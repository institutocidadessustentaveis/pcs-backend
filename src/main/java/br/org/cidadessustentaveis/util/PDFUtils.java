package br.org.cidadessustentaveis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class PDFUtils {
	
	public static String Pdf2String(String nomeArquivo) throws IOException {
		byte[] input_file = Files.readAllBytes(Paths.get(nomeArquivo));
        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString =  new String(encodedBytes);
	    return encodedString;
	}
	
	public static byte[] B64Decode(String pdfInBase64) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(pdfInBase64.getBytes());
	    return decodedBytes;
	}
	 

	
}
