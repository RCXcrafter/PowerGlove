package com.rcx.powerglove;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Webhook {
	public static void listen() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket m_ServerSocket = new ServerSocket(12876);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
			cliThread.start();
		}
	}
}

class ClientServiceThread extends Thread {
	Socket clientSocket;
	int clientID = -1;
	boolean running = true;

	ClientServiceThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	public void run() {
		String ip = clientSocket.getInetAddress().getHostName();
		System.out.println("Accepted Client : ID - " + clientID + " : Address - " + ip);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			String content = "";
			String sender = "";
			String authorization = "";
			boolean done = false;
			boolean contentComesNext = false;
			out.println("GET / HTTP/1.1 200 ");
			out.flush();
			while (running) {
				String clientCommand = in.readLine();
				System.out.println("Client Says :" + clientCommand);
				if (clientCommand == null || clientCommand.equalsIgnoreCase("quit")) {
					running = false;
					System.out.print("Stopping client thread for client : " + clientID);
			        out.println("");
			        out.println("quit");
					out.flush();
					out.close();
					in.close();
					clientSocket.close();
					return;
				} else {
					if (!clientCommand.equals("POST / HTTP/1.1")) {
						out.println(clientCommand);
						out.flush();
					}
				}

				if (contentComesNext)
					content = clientCommand;
				if (clientCommand.equals(""))
					contentComesNext = true;
				if (clientCommand.startsWith("User-Agent: ") || clientCommand.startsWith("user-agent: "))
					sender = clientCommand.substring(12);
				if (clientCommand.startsWith("X-DBL-Signature: ")) {
					sender = "DBL2";
					authorization = clientCommand.substring(17, clientCommand.substring(17).lastIndexOf(" "));
				}
				if (clientCommand.startsWith("Authorization: ") || clientCommand.startsWith("authorization: "))
					authorization = clientCommand.substring(15);

				if (!done && !content.equals("")) {
					VoteHandler.recieveVote(content, sender, authorization, ip);
					done = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}