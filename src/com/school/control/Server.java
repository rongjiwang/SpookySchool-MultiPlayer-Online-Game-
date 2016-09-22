package com.school.control;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Server should be a clock runner
 * @author rongjiwang
 *
 */
public class Server extends Thread {
	// port
	private InetAddress address;
	private ServerSocket server;
	private Socket[] sockets = new Socket[5];
	private boolean exit;
	// private Game game;

	public Server(Object board, int port) {
		// active the game
		// game = new Game(... ,... ,...);

		try {
			server = new ServerSocket(port, 50, InetAddress.getLocalHost());
			address = server.getInetAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the following always accept sockets connected from clients. New callable
	 * tasks based on the connection will be submitted in the thread pool.
	 */
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(50);
		while (!exit) {
			if (server.isClosed())
				break;
			try {
				Socket connection = server.accept();
				Callable<Void> task = new Task(connection);
				pool.submit(task);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * The following creates a thread that is always listening to the client
	 * that establishes the socket connection
	 * 
	 * @author kevin
	 *
	 */
	private class Task implements Callable<Void> {
		private Socket connection;

		public Task(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() throws Exception {

			InputStreamReader in = new InputStreamReader(connection.getInputStream());
			// could be a message or object
			int receive = in.read();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			// player = out;
			sockets[1] = connection;

			out.write("something");
			out.flush();

			return null;
		}
	}
}
