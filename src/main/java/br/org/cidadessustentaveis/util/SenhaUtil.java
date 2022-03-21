package br.org.cidadessustentaveis.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SenhaUtil  implements PasswordEncoder {

	public static String criptografarSHA2(String texto) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(texto.getBytes("UTF-8"));		 
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
			  hexString.append(String.format("%02X", 0xFF & b));
			}
			String senhaCriptgrafada = hexString.toString();
			return senhaCriptgrafada;
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		  e.printStackTrace();
		  return "";
		}
	}
	
	public static String gerarSenha(Integer qtdCaracteres) {
		String[] carct ={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j",
				"k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F",
				"G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};		
	    String senha="";
	    for (int x=0; x<qtdCaracteres; x++){
	        int j = (int) (Math.random()*carct.length);
	        senha += carct[j];
	    }
	    
	    return senha;
	}
	
	@Override
	public String encode(CharSequence texto) {
		return criptografarSHA2(texto.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return(encodedPassword.toLowerCase().equals(encode(rawPassword).toLowerCase()));
	}
}