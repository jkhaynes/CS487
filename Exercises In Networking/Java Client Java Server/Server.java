import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws Exception {

		String clientSentence;
		byte[] clientSignature;

		// create a myRSA object
		myRSA rsaObject = new myRSA();

		/* create server socket (welcome socket) on port 19642 */
		ServerSocket welcomeSocket = new ServerSocket(19642);

		System.out.println("TCP server is ready ...");

		try {

			while (true) {
				// Server will block here waiting for client request
				Socket connectionSocket = welcomeSocket.accept();

				// create input stream attached to socket
				ObjectInputStream inFromClient = new ObjectInputStream(
						connectionSocket.getInputStream());

				// create output stream attached to socket
				DataOutputStream outToClient = new DataOutputStream(
						connectionSocket.getOutputStream());

				// read object from the socket through input stream
				Message clientObject = (Message) inFromClient
						.readObject();

				clientSentence = clientObject.getSentence();
				// Uncomment below to test program
				// clientSentence = "Test";
				clientSignature = clientObject.getSignature();

				// turn user provided sentence into byte array
				byte[] sentenceBytes = clientSentence.getBytes();

				// hash sentence
				String hashSentence = rsaObject.hash(sentenceBytes);

				// decrypt signature
				byte[] decryptedSig = rsaObject.rsaDecrypt(clientSignature);

				// test if decrypted signature is same as hashed sentence
				if (hashSentence.equals(new String(decryptedSig)))
					// write true to client
					outToClient.writeBytes("True" + '\n');
				else
					// write false to client
					outToClient.writeBytes("False" + '\n');

			} /*
			 * end of while loop, server returns back and waits for another
			 * client's request
			 */

		} finally {
			welcomeSocket.close();
		}
	}
}
