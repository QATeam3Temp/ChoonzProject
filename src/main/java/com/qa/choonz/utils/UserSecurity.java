package com.qa.choonz.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
@Service
public class UserSecurity {

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	
    public static final int SALT_BYTES = 16;
    public static final int HASH_BYTES = 16;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    
    UserRepository userRepository;
    
    
    @Autowired
	public UserSecurity(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public static @NotNull byte[] getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        
		return salt;
	}
	
	public boolean testKey(String key) {
		List<User>users = userRepository.findAll();
		boolean found= false;
		for(User user:users){  
		     if(verifyKey(user, key)) {
		    	 found=true;
		     }
		   }  
		
		return found;

		
	}
	
	private boolean verifyKey(User user, String key) {
		
		byte[] newKey = ByteBuffer.allocate(4).putInt((int) user.getId()).array();
		try {
			return key.equals(encrypt(user.getUsername(), newKey));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return false;
		}
	}
	
	
	public static String encrypt(String password,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		return createHash(password, salt);
	}

    public static String createHash(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
    	if(salt==null) {
    		salt =  ByteBuffer.allocate(4).putInt(1).array();
    	}
            return createHash(password.toCharArray(), salt);
        }
    
    public static String createHash(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {

            byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
            return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
        }
    
    
    public boolean verifyLogin(User user, String password) {
		if(user==null) {
			return false;
		}
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
            return slowEquals(hash,testHash);
        }
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
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
