package com.swing.server.app.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * The type Client handler.
 */
public class ClientHandler extends Thread {

  /**
   * The Client socket.
   */
  private final Socket clientSocket;

  /**
   * The On disconnect client connection.
   */
  private final Consumer<Socket> onDisconnectClientConnection;

  /**
   * The Out.
   */
  private PrintWriter out;

  /**
   * The In.
   */
  private BufferedReader in;

  /**
   * Instantiates a new Client handler.
   *
   * @param clientSocket                 the client socket
   * @param onAcceptClientConnection     the on accept client connection
   * @param onDisconnectClientConnection the on disconnect client connection
   */
  public ClientHandler(Socket clientSocket,
      Consumer<Socket> onAcceptClientConnection,
      Consumer<Socket> onDisconnectClientConnection) {
    this.clientSocket = clientSocket;
    this.onDisconnectClientConnection = onDisconnectClientConnection;
    onAcceptClientConnection.accept(clientSocket);
  }

  /**
   * If this thread was constructed using a separate {@code Runnable} run object, then that {@code
   * Runnable}************ object's {@code run} method is called; otherwise, this method does
   * nothing and returns.
   * <p>
   * Subclasses of {@code Thread} should override this method.
   *
   * @see #start() #start()#start()#start()
   */
  @Override
  public void run() {
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        if (".".equals(inputLine)) {
          out.println("bye");
          break;
        }
        out.println(inputLine);
      }
      in.close();
      out.close();
      clientSocket.close();
    } catch (Exception exception) {
      this.onDisconnectClientConnection.accept(clientSocket);
      System.out.printf("Client disconnected : %s => %s%n",
          clientSocket.toString(), exception.getMessage());
    }
  }


}
