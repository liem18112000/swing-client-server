package com.swing.client.app.client.impl;

import com.swing.client.app.client.NetworkClient;
import com.swing.client.app.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

/**
 * The type String message network client.
 */
public class StringMessageNetworkClient implements NetworkClient<String> {

  /**
   * The Socket.
   */
  private Socket socket;

  /**
   * The Out.
   */
  private PrintWriter out;

  /**
   * The In.
   */
  private BufferedReader in;

  /**
   * Connect.
   *
   * @param ip   the ip
   * @param port the port
   */
  @Override
  public void connect(String ip, int port) {
    try {
      socket = new Socket(ip, port);
      System.out.printf("Connect to server %s:%s%n", ip, port);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Send message.
   *
   * @param message the message
   * @return the message
   */
  @Override
  public String sendMessage(String message) {
    out.println(message);
    String resp = null;
    try {
      resp = in.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return resp;
  }

  /**
   * Disconnect.
   */
  @Override
  public void disconnect() {
    try {
      in.close();
      out.close();
      socket.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return StringUtils.toString(this.socket);
  }
}
