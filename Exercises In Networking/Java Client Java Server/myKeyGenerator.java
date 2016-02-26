import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class myKeyGenerator {

	public static void main(String[] args) throws Exception{
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		//Key publicKey = kp.getPublic();
		//Key privateKey = kp.getPrivate();

		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
		  RSAPublicKeySpec.class);
		RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
		  RSAPrivateKeySpec.class);

		saveToFile("public.key", pub.getModulus(),
		  pub.getPublicExponent());
		saveToFile("private.key", priv.getModulus(),
		  priv.getPrivateExponent());
	}
	
	public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
	  ObjectOutputStream oout = new ObjectOutputStream(
	    new BufferedOutputStream(new FileOutputStream(fileName)));
	  try {
	    oout.writeObject(mod);
	    oout.writeObject(exp);
	  } catch (Exception e) {
	    throw new IOException("Unexpected error", e);
	  } finally {
	    oout.close();
	  }
	}

}