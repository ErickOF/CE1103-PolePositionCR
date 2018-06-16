package ce.itcr.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {
	// CONSTANTS
	private static ClientSocket clientSocket;
	private static final int PORT = 12000;
	private static Socket client;
	private static final String LOCALHOST = "localhost";

	private ClientSocket() {
		try {
			client = new Socket(LOCALHOST, PORT);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host");
		} catch (IOException e) {
			System.out.println("");
		}
	}

	public static ClientSocket getInstance() {
		if (clientSocket == null) {
			clientSocket = new ClientSocket();
		}
		return clientSocket;
	}

	public static boolean send(String msg) {
		try {
			DataOutputStream outToServer = new DataOutputStream(
					client.getOutputStream());
			outToServer.writeBytes(msg);
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			String modifiedSentence = inFromServer.readLine();
			System.out.println(modifiedSentence);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean close() {
		try {
			client.close();
			return true;
		} catch (IOException e1) {
			return false;
		}
	}
}
