import java.io.Serializable;

/**
 * An object to wrap together the original string and it's signature to send to
 * the server
 * 
 * @author Jess
 */

public class Message implements Serializable {

	private String sentence;
	private byte[] signature;

	Message(String _sentence, byte[] _signature) {
		sentence = _sentence;
		signature = _signature;
	}

	public String getSentence() {
		return sentence;
	}

	public byte[] getSignature() {
		return signature;
	}
}