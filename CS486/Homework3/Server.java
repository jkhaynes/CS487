import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.io.IOException;

public class Server implements Runnable{

	private static int MAX_SIZE = 1024;
	private static int HASH_SIZE = 256;
	private static int KEY = 3;

	Socket clientSocket;
	Server(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		
		serverSocket = new ServerSocket(4447); // 4447 is port number
		System.out.println("Listening");
		while (true) {
			clientSocket = serverSocket.accept(); 
			System.out.println("Connected");
			new Thread(new Server(clientSocket)).start();
		}
	}

	public void run(){

		try{

			Util util = new Util();

			char[] message = new char[MAX_SIZE];
			char[] signature = new char[HASH_SIZE];
			char[] hash = new char[HASH_SIZE];
			char[] compare = new char[HASH_SIZE];

			BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

			in.read(message, 0, MAX_SIZE);

			in.read(signature, 0, HASH_SIZE);

			hash = util.hash(message);
			compare = util.decryption(signature, KEY);

			String toSend;
			if(Arrays.equals(hash, compare))
				toSend = "True";
			else
				toSend = "False";

			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); 
			wr.write(toSend);
			wr.flush(); // flushes the stream
		
			clientSocket.close();
		}

		catch (IOException e){
			System.out.println(e);
		}
	}
}