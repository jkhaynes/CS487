import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

	public static void main(String[] args) throws Exception {

		Util util = new Util();

		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		char[] message = new char[1024];
		char[] signature = new char[256];
		char[] hash = new char[256];
		char[] compare = new char[256];

		serverSocket = new ServerSocket(4447); // 4447 is port number
		clientSocket = serverSocket.accept(); // blocks and listen until a
												// connection is made.

		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		String messageLine = in.readLine();
		message = messageLine.toCharArray();
		System.out.println(message);

		String signatureLine = in.readLine();
		signature = signatureLine.toCharArray();
		System.out.println(signature);

		hash = util.hash(message);
		compare = util.decryption(signature, 3);
		
		if(Arrays.equals(hash, compare)){
			System.out.println("True");
		}
		else
			System.out.println("False");

		//BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); 
		//wr.write(signature);
		//wr.flush(); // flushes the stream
		
		serverSocket.close();
		clientSocket.close();
	}

}