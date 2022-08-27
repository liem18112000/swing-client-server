package com.swing.server.app.server.impl;

import com.swing.server.app.controller.AppController;
import com.swing.server.app.model.Action;
import com.swing.server.app.model.HistoryRecordModel;
import com.swing.server.app.server.ClientHandler;
import com.swing.server.app.server.NetworkServer;
import com.swing.server.app.util.StringUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Multi client network server.
 */
public class MultiClientNetworkServer implements NetworkServer {

  /**
   * The Server socket.
   */
  private ServerSocket serverSocket;

  /**
   * The Clients.
   */
  private Set<Socket> clients = new HashSet<>();

  /**
   * Gets clients.
   *
   * @return the clients
   */
  public Set<Socket> getClients() {
    return clients;
  }

  /**
   * Start.
   *
   * @param port the port
   */
  @Override
  public void start(final int port) {
    try {
      serverSocket = new ServerSocket(port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        final var clientHandler = new ClientHandler(
            clientSocket, this::onAcceptClientConnection, this::onDisconnectClientConnection);
        clientHandler.start();
        System.out.printf("Client connected: %s%n", clientSocket.toString());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      this.stop();
    }
  }

  /**
   * On accept client connection.
   *
   * @param clientSocket the client socket
   */
  protected void onAcceptClientConnection(final Socket clientSocket) {
    clients.add(clientSocket);
    if (AppController.historyController != null) {
      AppController.historyController.updateClientList();
      AppController.historyController.updateRecordTable(
          HistoryRecordModel.build()
              .setTimestamp(LocalDateTime.now().toString())
              .setAction(Action.CLIENT_CONNECTED)
              .setDescription(
                  String.format("Client connected: %s", StringUtils.toString(clientSocket)))
      );
    }
  }

  /**
   * On disconnect client connection.
   *
   * @param clientSocket the client socket
   */
  protected void onDisconnectClientConnection(final Socket clientSocket) {
    clients.remove(clientSocket);
    if (AppController.historyController != null) {
      AppController.historyController.updateClientList();
      AppController.historyController.updateRecordTable(
          HistoryRecordModel.build()
              .setTimestamp(LocalDateTime.now().toString())
              .setAction(Action.CLIENT_DISCONNECTED)
              .setDescription(
                  String.format("Client disconnected: %s", StringUtils.toString(clientSocket)))
      );
    }
  }

  /**
   * Stop.
   */
  @Override
  public void stop() {
    try {
      this.clients = new HashSet<>();
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * To string string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    try {
      return String.format("IP Address: %s - Port: %s",
          InetAddress.getLocalHost().getHostAddress(), this.serverSocket.getLocalPort());
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return String.format("IP Address: Unknown - Port: %s", this.serverSocket.getLocalPort());
    }
  }
}
