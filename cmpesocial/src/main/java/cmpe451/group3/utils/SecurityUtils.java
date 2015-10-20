package cmpe451.group3.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
	
	public static String getHashed(String password){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
            return generatedPassword;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
		//if there is a problem with hashing return itself
		return password;
	}
	
	public static boolean checkPassword(String hashedPassword, String password){
		if(hashedPassword.equals(getHashed(password)))
			return true;
		return false;
	}
}
