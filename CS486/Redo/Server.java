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

			char[] message = new char[1024];
			char[] signature = new char[256];
			char[] hash = new char[256];
			char[] compare = new char[256];

			BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

			in.read(message, 0, 1024);
			//message = messageLine.toCharArray();
			System.out.println(message);

			in.read(signature, 0, 256);
			//signature = signatureLine.toCharArray();
			System.out.println(signature);

			hash = util.hash(message);
			compare = util.decryption(signature, 3);

			System.out.println("Test: " + Arrays.toString(hash));
			System.out.println("Test: " + Arrays.toString(compare));
		
			String toSend;
			if(Arrays.equals(hash, compare))
				toSend = "True";
			else
				toSend = "False";

			System.out.println(toSend);

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