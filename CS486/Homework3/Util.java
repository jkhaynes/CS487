
public class Util {
	
	private static int HASH_SIZE = 256;

	public char[] hash(char[] message){
		
		char[] output = new char[HASH_SIZE];
		int i = 0;
	    while (i < message.length){
	  		output[i % HASH_SIZE] += message[i++];
	  	}
	  	output[HASH_SIZE-1] = '\0';
		return output;	
		
	}
	
	public char[] encryption(char[] message, int key){
		
		char[] output = new char[HASH_SIZE];
		int i = 0;
		while (i < message.length){
			output[i] = (char) (message[i] + key);
			i++;
		}
		output[HASH_SIZE-1] = '\0';
		return output;
	}
	
	public char[] decryption(char[] message, int key){
		return encryption(message, 0-key);
	}
	
}
