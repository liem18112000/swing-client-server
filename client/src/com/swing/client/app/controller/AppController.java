package com.swing.client.app.controller;

import com.swing.client.app.client.NetworkClient;
import com.swing.client.app.client.impl.StringMessageNetworkClient;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The type App controller.
 */
public class AppController {

  /**
   * The Ip text field.
   */
  private JTextField IPTextField;

  /**
   * The Port text field.
   */
  private JTextField portTextField;

  /**
   * The User text field.
   */
  private JTextField userTextField;

  /**
   * The Connect server button.
   */
  private JButton connectServerButton;

  /**
   * The App panel.
   */
  private JPanel appPanel;

  /**
   * The Network client.
   */
  private final NetworkClient<String> networkClient;

  /**
   * Gets app panel.
   *
   * @return the app panel
   */
  public JPanel getAppPanel() {
    return appPanel;
  }

  /**
   * Instantiates a new App controller.
   */
  public AppController() {
    this.networkClient = new StringMessageNetworkClient();
    connectServerButton.addActionListener(e -> {
      try {
        final String ip = IPTextField.getText();
        final int port = Integer.parseInt(portTextField.getText());
        final String user = userTextField.getText();
        new Thread(() -> networkClient.connect(ip, port)).start();
        JOptionPane.showMessageDialog(appPanel,
            String.format("Connect to server %s:%s with user '%s'", ip, port, user));
      } catch (Exception exception) {
        exception.printStackTrace();
        JOptionPane.showMessageDialog(appPanel, String.format("Error: %s", exception.getMessage()));
      }
    });
  }

  /**
   * Start.
   */
  public static void start() {
    JFrame frame = new JFrame(AppController.class.getCanonicalName());
    AppController appController = new AppController();
    frame.setContentPane(appController.getAppPanel());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

}
