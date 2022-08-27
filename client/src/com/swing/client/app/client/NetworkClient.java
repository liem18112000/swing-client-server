package com.swing.client.app.client;

import java.io.Serializable;

/**
 * The interface Network client.
 *
 * @param <MESSAGE> the type parameter
 */
public interface NetworkClient<MESSAGE extends Serializable> {

  /**
   * Connect.
   *
   * @param ip   the ip
   * @param port the port
   */
  void connect(String ip, int port);

  /**
   * Send message.
   *
   * @param message the message
   * @return the message
   */
  MESSAGE sendMessage(MESSAGE message);

  /**
   * Disconnect.
   */
  void disconnect();
}
