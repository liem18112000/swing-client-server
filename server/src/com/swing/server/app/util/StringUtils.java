package com.swing.server.app.util;

import java.net.Socket;

/**
 * The type String utils.
 */
public class StringUtils {

  /**
   * To string string.
   *
   * @param socket the socket
   * @return the string
   */
  static public String toString(Socket socket) {
    return socket.getRemoteSocketAddress().toString();
  }

  /**
   * Escape special characters string.
   *
   * @param data the data
   * @return the string
   */
  static public String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
      data = data.replace("\"", "\"\"");
      escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }

  /**
   * Not null or empty boolean.
   *
   * @param string the string
   * @return the boolean
   */
  static public boolean notNullOrEmpty(String string) {
    return string != null && !string.isEmpty();
  }

}
