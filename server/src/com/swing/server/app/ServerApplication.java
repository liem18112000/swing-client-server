package com.swing.server.app;

import com.swing.server.app.controller.AppController;
import java.io.IOException;

/**
 * The type Server application.
 */
public class ServerApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException the io exception
   */
  public static void main(String[] args) throws IOException {
    AppController.start();
  }

}
