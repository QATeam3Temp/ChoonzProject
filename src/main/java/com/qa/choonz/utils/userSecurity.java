package com.qa.choonz.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.persistence.domain.User;

public class userSecurity {

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	
    public static final int SALT_BYTES = 16;
    public static final int HASH_BYTES = 16;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
	
	public static @NotNull byte[] getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        
		return salt;
	}
	
	public static @NotNull @Size(max = 100) String encrypt(@NotNull @Size(max = 100) String password,
			@NotNull byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		return createHash(password, salt);
	}
	
    public static String createHash(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            return createHash(password.toCharArray(), salt);
        }
    
    public static String createHash(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {

            byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
            return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
        }
    
    
    public static boolean verifyLogin(User user, String password) {
    	
    	String pass = user.getPassword();
    	
    	try {
			return validatePassword(password, pass);
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (InvalidKeySpecException e) {
			return false;
		}
    }
    
    
    public static boolean validatePassword(String password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            return validatePassword(password.toCharArray(), goodHash);
        }

    public static boolean validatePassword(char[] password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            
            String[] params = goodHash.split(":");
            int iterations = Integer.parseInt(params[ITERATION_INDEX]);
            byte[] salt = fromHex(params[SALT_INDEX]);
            byte[] hash = fromHex(params[PBKDF2_INDEX]);
            byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
            
            return hash.equals(testHash);
        }
    
    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }
    
    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        }


}
