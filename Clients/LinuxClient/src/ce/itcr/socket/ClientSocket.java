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
	private static final int PORT = 9003;
	private static Socket client;
	private static final String LOCALHOST = "127.0.0.1";

	private ClientSocket() throws UnknownHostException, IOException {
		client = new Socket(LOCALHOST, PORT);
	}

	public static ClientSocket getInstance() {
		if (clientSocket == null) {
			try {
				clientSocket = new ClientSocket();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return clientSocket;
	}

	public static String send(String msg) {
		String instruction = "";
		try {
			DataOutputStream outToServer = new DataOutputStream(
					client.getOutputStream());
			outToServer.writeBytes(msg);
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			Thread.sleep(100);
			if (inFromServer.ready()) {
				char[] serverMsg = new char[1024];
				inFromServer.read(serverMsg);
				for (int i = 0; i < serverMsg.length; i++) {
					instruction = instruction + serverMsg[i];
				}
			} else {
				System.out.println("Not response");
			}
			return instruction;
		} catch (IOException e) {
			System.out.println("IO");
		} catch (InterruptedException e2) {
			System.out.println("Interrupted");
		}
		return instruction;
	}

	public static boolean close() {
		try {
			client.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
