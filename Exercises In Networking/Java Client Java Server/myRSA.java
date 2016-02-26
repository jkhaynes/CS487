import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class myRSA {

	PublicKey getPubKey(String keyFileName) throws IOException {
		 File f = new File(keyFileName);
		 InputStream in = new FileInputStream(f);
		 
		  ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
		  try {
		    BigInteger m = (BigInteger) oin.readObject();
		    BigInteger e = (BigInteger) oin.readObject();
		    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		    KeyFactory fact = KeyFactory.getInstance("RSA");
		    PublicKey pubKey = fact.generatePublic(keySpec);
		    return pubKey;
		  } catch (Exception e) {
		    throw new RuntimeException("serialisation error", e);
		  } finally {
		    oin.close();
		  }
	}
	 
	 public byte[] rsaEncrypt(byte[] data) throws Exception{
		  PublicKey pubKey = getPubKey("./public.key");
		  Cipher cipher = Cipher.getInstance("RSA");
		  cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		  byte[] cipherData = cipher.doFinal(data);
		  return cipherData;
	}
	 
	 PrivateKey getPrivKey(String keyFileName) throws IOException {
		 File f = new File(keyFileName);
		 InputStream in = new FileInputStream(f);
		 
		  ObjectInputStream oin =
		    new ObjectInputStream(new BufferedInputStream(in));
		  try {
		    BigInteger m = (BigInteger) oin.readObject();
		    BigInteger e = (BigInteger) oin.readObject();
		    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
		    KeyFactory fact = KeyFactory.getInstance("RSA");
		    PrivateKey privKey = fact.generatePrivate(keySpec);
		    return privKey;
		  } catch (Exception e) {
		    throw new RuntimeException("Spurious serialisation error", e);
		  } finally {
		    oin.close();
		  }
	}
	 
	 public byte[] rsaDecrypt(byte[] data) throws Exception{
		 PrivateKey privKey = getPrivKey("./private.key");
		  Cipher cipher = Cipher.getInstance("RSA");
		  cipher.init(Cipher.DECRYPT_MODE, privKey);
		  byte[] decryptedData = cipher.doFinal(data);
		  return decryptedData;
	}
	
	 public String hash(byte[] data) throws Exception{
		 MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		 byte[] hashBytes = messageDigest.digest(data);
		 return this.convertByteArrayToHexString(hashBytes); 
	 }
	 
	 
	 private String convertByteArrayToHexString(byte[] arrayBytes) {
	    StringBuffer stringBuffer = new StringBuffer();
	    for (int i = 0; i < arrayBytes.length; i++) {
	        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
	                .substring(1));
	    }
	    return stringBuffer.toString();
	}
	
	
}