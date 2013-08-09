package br.com.vexillum.util;

import java.security.MessageDigest;

import br.com.vexillum.control.manager.ExceptionManager;

public class EncryptUtils {

	public static String encryptOnSHA512(String password) {                
        MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-512");
			byte messageDigest[] = algorithm.digest(password.getBytes("UTF-8"));                
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
              hexString.append(String.format("%02X", 0xFF & b));
            }
            String senha = hexString.toString();   
            return senha;
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
		return password;
	}     
}
