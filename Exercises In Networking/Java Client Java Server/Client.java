import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws Exception {

		String sentence;
		String result;

		// Create myRSA Object
		myRSA rsaObject = new myRSA();

		// Create input stream
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));

		// Create client socket, connect to server
		Socket clientSocket = new Socket("localhost", 19642);

		/*
		 * create output stream attached to socket such that the client can send
		 * message through the socket
		 */
		ObjectOutputStream outToServer = new ObjectOutputStream(
				clientSocket.getOutputStream());

		/*
		 * create input steam attached to socket so that the client can read
		 * message from the socket
		 */
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		
		System.out.println("Enter Sentence: ");

		// read from the keyboard
		sentence = inFromUser.readLine();

		// Change user provided sentence into byte array
		byte[] sentenceBytes = sentence.getBytes();

		// Hash user provided sentence
		String hashSentence = rsaObject.hash(sentenceBytes);

		// Change hash into byte array
		byte[] hashBytes = hashSentence.getBytes();

		// Encrypt hash
		byte[] signature = rsaObject.rsaEncrypt(hashBytes);

		// Create object to send to server with sentence and signature
		Message toSend = new Message(sentence, signature);

		/* Send object to the server */
		outToServer.writeObject(toSend);

		/* Read line from the server */
		result = inFromServer.readLine();

		System.out.println("FROM SERVER: " + result);

		try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Couldn't close a socket");
		}

	}

}
