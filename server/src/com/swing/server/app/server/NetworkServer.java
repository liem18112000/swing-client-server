package com.swing.server.app.server;

import java.util.Set;

/**
 * The interface Network server.
 */
public interface NetworkServer {

  /**
   * Start.
   *
   * @param port the port
   */
  void start(int port);

  /**
   * Stop.
   */
  void stop();

  /**
   * Gets clients.
   *
   * @param <T> the type parameter
   * @return the clients
   */
  <T> Set<T> getClients();

}
