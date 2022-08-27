package com.swing.client.app.controller;

import static com.swing.client.app.config.ApplicationConfig.TRACK_PATH;

import com.swing.client.app.client.NetworkClient;
import com.swing.client.app.client.impl.StringMessageNetworkClient;
import com.swing.client.app.model.Action;
import com.swing.client.app.model.HistoryRecordModel;
import com.swing.client.app.service.DirectoryTrackingService;
import com.swing.client.app.util.DefaultCloseApplicationHandler;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
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
   * The constant history.
   */
  public static HistoryController historyController;

  /**
   * The Tracking service.
   */
  private final DirectoryTrackingService trackingService;

  /**
   * The constant mainFrame.
   */
  public static JFrame mainFrame;

  /**
   * The Tracking path.
   */
  private final Path trackingPath;

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
  public AppController() throws IOException {
    this.trackingPath = Path.of(TRACK_PATH);
    this.networkClient = new StringMessageNetworkClient();
    this.trackingService = new DirectoryTrackingService(
        FileSystems.getDefault().newWatchService(), this.trackingPath);
    connectServerButton.addActionListener(e -> {
      if (startClient()) {
        startHistory();
        historyController.updateRecordTable(
            HistoryRecordModel.build()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction(Action.CLIENT_CONNECTED)
                .setDescription(String.format("Client connected: %s", networkClient))
        );
      }
    });
  }

  private void startHistory() {
    historyController = new HistoryController();
    HistoryController.mainFrame = new JFrame(AppController.class.getCanonicalName());
    HistoryController.mainFrame.setContentPane(historyController.getAppPanel());
    HistoryController.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    HistoryController.mainFrame.pack();
    HistoryController.mainFrame.setVisible(true);
    HistoryController.mainFrame.addWindowListener(new DefaultCloseApplicationHandler(
        historyController.getAppPanel()));
    mainFrame.setVisible(false);
  }

  private boolean startClient() {
    try {
      final String ip = IPTextField.getText();
      final int port = Integer.parseInt(portTextField.getText());
      final String user = userTextField.getText();
      new Thread(() -> networkClient.connect(ip, port)).start();
      JOptionPane.showMessageDialog(appPanel,
          String.format("Connect to server %s:%s with user '%s'", ip, port, user));
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
      JOptionPane.showMessageDialog(appPanel, String.format("Error: %s", exception.getMessage()));
      return false;
    }
  }

  /**
   * Start.
   */
  public static void start() throws IOException {
    mainFrame = new JFrame(AppController.class.getCanonicalName());
    AppController appController = new AppController();
    mainFrame.setContentPane(appController.getAppPanel());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.pack();
    mainFrame.addWindowListener(new DefaultCloseApplicationHandler(appController.getAppPanel()));
    mainFrame.setVisible(true);
  }

}
