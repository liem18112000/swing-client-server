package com.swing.server.app.controller;

import static com.swing.server.app.config.ApplicationConfig.TRACK_PATH;

import com.swing.server.app.model.Action;
import com.swing.server.app.model.HistoryRecordModel;
import com.swing.server.app.server.NetworkServer;
import com.swing.server.app.server.impl.MultiClientNetworkServer;
import com.swing.server.app.service.DirectoryTrackingService;
import com.swing.server.app.util.DefaultCloseApplicationHandler;
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
 * The type App.
 */
public class AppController {

  /**
   * The Start server button.
   */
  private JButton startServerButton;

  /**
   * The App panel.
   */
  private JPanel appPanel;

  /**
   * The Server port text field.
   */
  private JTextField serverPortTextField;

  /**
   * The Network server.
   */
  private final NetworkServer networkServer;

  /**
   * The Tracking service.
   */
  private final DirectoryTrackingService trackingService;

  /**
   * The constant history.
   */
  public static HistoryController historyController;

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
   * Instantiates a new App.
   *
   * @throws IOException the io exception
   */
  public AppController() throws IOException {
    this.trackingPath = Path.of(TRACK_PATH);
    this.networkServer = new MultiClientNetworkServer();
    this.trackingService = new DirectoryTrackingService(
        FileSystems.getDefault().newWatchService(), this.trackingPath);
    startServerButton.addActionListener((e) -> {
      if(startServer()) {
        startHistoryFrame();
        historyController.updateRecordTable(
            HistoryRecordModel.build()
                .setTimestamp(LocalDateTime.now().toString())
                .setDirectory(this.trackingPath.toString())
                .setAction(Action.START_SERVER)
                .setDescription("Start Server")
        );
        this.trackingService.start();
      }
    });
  }

  /**
   * Start history frame.
   */
  private void startHistoryFrame() {
    historyController = new HistoryController(networkServer);
    HistoryController.mainFrame = new JFrame(historyController.getClass().getCanonicalName());
    HistoryController.mainFrame.setContentPane(historyController.getAppPanel());
    HistoryController.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    HistoryController.mainFrame.pack();
    HistoryController.mainFrame.setVisible(true);
    HistoryController.mainFrame.addWindowListener(new DefaultCloseApplicationHandler(
        historyController.getAppPanel()));
    mainFrame.setVisible(false);
  }

  /**
   * Start server boolean.
   *
   * @return the boolean
   */
  private boolean startServer() {
    try {
      final int serverPort = Integer.parseInt(serverPortTextField.getText());
      new Thread(() -> networkServer.start(serverPort)).start();
    } catch (NullPointerException exception) {
      JOptionPane.showMessageDialog(appPanel, "Error: server port is null");
      return false;
    } catch (NumberFormatException exception) {
      JOptionPane.showMessageDialog(appPanel, "Error: server port is not an integer");
      return false;
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(appPanel, String.format("Error: %s", exception.getMessage()));
      return false;
    }
    JOptionPane.showMessageDialog(appPanel, "Start server");
    return true;
  }

  /**
   * Start.
   *
   * @throws IOException the io exception
   */
  public static void start() throws IOException {
    AppController appController = new AppController();
    mainFrame = new JFrame(AppController.class.getCanonicalName());
    mainFrame.setContentPane(appController.getAppPanel());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.pack();
    mainFrame.addWindowListener(new DefaultCloseApplicationHandler(appController.getAppPanel()));
    mainFrame.setVisible(true);
  }
}
