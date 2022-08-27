package com.swing.server.app.controller;

import static com.swing.server.app.config.ApplicationConfig.HISTORY_RECORD_COLUMN_IDENTIFIERS;
import static com.swing.server.app.config.ApplicationConfig.LOG_PATH;
import static com.swing.server.app.config.ApplicationConfig.TRACK_PATH;

import com.swing.server.app.model.Action;
import com.swing.server.app.model.HistoryRecordModel;
import com.swing.server.app.server.NetworkServer;
import com.swing.server.app.service.HistoryRecordService;
import com.swing.server.app.util.StringUtils;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * The type History.
 */
public class HistoryController {

  /**
   * The Client list.
   */
  private JList clientList;

  /**
   * The Record history.
   */
  private JTable recordHistory;

  /**
   * The App panel.
   */
  private JPanel appPanel;

  /**
   * The Information text field.
   */
  private JTextField informationTextField;

  /**
   * The Search criteria.
   */
  private JComboBox searchCriteria;

  /**
   * The Search value.
   */
  private JTextField searchValue;

  /**
   * The Search button.
   */
  private JButton searchButton;

  /**
   * The Log button.
   */
  private JButton logButton;

  /**
   * The Load log button.
   */
  private JButton loadLogButton;

  /**
   * The Quit button.
   */
  private JButton quitButton;

  /**
   * The Help button.
   */
  private JButton helpButton;

  /**
   * The constant mainFrame.
   */
  public static JFrame mainFrame;

  /**
   * The Network server.
   */
  private final NetworkServer networkServer;

  /**
   * The History record service.
   */
  private final HistoryRecordService historyRecordService;

  /**
   * The Temporary history.
   */
  private final TemporaryHistoryController temporaryHistoryController;

  /**
   * The Record data model.
   */
  private final DefaultTableModel recordDataModel = new DefaultTableModel(0, 0);

  /**
   * Gets app panel.
   *
   * @return the app panel
   */
  public JPanel getAppPanel() {
    return appPanel;
  }

  /**
   * Instantiates a new History.
   *
   * @param networkServer the network server
   */
  public HistoryController(NetworkServer networkServer) {

    this.networkServer = networkServer;

    this.historyRecordService = new HistoryRecordService();

    this.temporaryHistoryController = new TemporaryHistoryController();

    this.initializedConfiguration();

    this.addEventListenerToComponents();
  }

  /**
   * Initialized configuration.
   */
  private void initializedConfiguration() {
    this.recordDataModel.setColumnIdentifiers(HISTORY_RECORD_COLUMN_IDENTIFIERS);

    this.informationTextField.setText(this.networkServer.toString());
  }

  /**
   * Add event listener to components.
   */
  private void addEventListenerToComponents() {

    searchButton.addActionListener(e -> {
      final var searchCriteria = String.valueOf(this.searchCriteria.getSelectedItem());
      final var searchValue = this.searchValue.getText();
      final var model = "Client".equals(searchCriteria) ?
          this.historyRecordService.searchClientRelated(
              searchValue, this.recordDataModel) :
          this.historyRecordService.searchByKeyAndValue(
              searchCriteria, searchValue, this.recordDataModel);
      if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(appPanel, String.format(
            "There is not record with key '%s' and value '%s'", searchCriteria, searchValue));
      } else {
        this.temporaryHistoryController.showTable(model);
      }
      this.updateRecordTable(
          HistoryRecordModel.build()
              .setTimestamp(LocalDateTime.now().toString())
              .setDirectory(TRACK_PATH)
              .setAction(Action.SEARCH_HISTORY)
              .setDescription(String.format("Search record history by key '%s' by value '%s'"
                  , searchCriteria, searchValue))
      );
    });

    logButton.addActionListener(e -> {
      if (this.historyRecordService.exportLog(LOG_PATH, this.recordDataModel)) {
        JOptionPane.showMessageDialog(appPanel, "Log all data to file");
        this.updateRecordTable(
            HistoryRecordModel.build()
                .setTimestamp(LocalDateTime.now().toString())
                .setDirectory(LOG_PATH)
                .setAction(Action.LOG_HISTORY_FILE)
                .setDescription(String.format("Log record history to log directory '%s'", LOG_PATH))
        );
      } else {
        JOptionPane.showMessageDialog(appPanel, "Error: failed to log history");
      }
    });

    loadLogButton.addActionListener(e -> {
      final JFileChooser fileDialog = new JFileChooser();
      int returnVal = fileDialog.showOpenDialog(mainFrame);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        final var file = fileDialog.getSelectedFile();
        try {
          final var model = this.historyRecordService.loadLog(file);
          this.temporaryHistoryController.showTable(model);
          this.updateRecordTable(
              HistoryRecordModel.build()
                  .setTimestamp(LocalDateTime.now().toString())
                  .setDirectory(file.getAbsolutePath())
                  .setAction(Action.LOAD_HISTORY_FILE)
                  .setDescription(String.format(
                      "Load record history from file '%s'", file.getAbsolutePath()))
          );
        } catch (IOException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(appPanel, String.format(
              "Error: cannot load file due to %s.", ex.getMessage()));
        }
      } else {
        JOptionPane.showMessageDialog(appPanel, "Error: open command cancelled by user.");
      }
    });

    quitButton.addActionListener(e -> {
      int confirm = JOptionPane.showOptionDialog(appPanel,
          "Are You Sure to Close Application?",
          "Exit Confirmation", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, null, null);
      if (confirm == JOptionPane.YES_OPTION) {
        System.exit(0);
      }
    });

    helpButton.addActionListener(e -> {
      JOptionPane.showMessageDialog(appPanel, "Coming soon");
    });
  }

  /**
   * Update client list.
   */
  @SuppressWarnings("all")
  public void updateClientList() {
    final var clients = this.networkServer.<Socket>getClients();
    this.clientList.setListData(clients.stream().map(StringUtils::toString).toArray());
  }

  /**
   * Update record table.
   *
   * @param model the model
   */
  public void updateRecordTable(final HistoryRecordModel model) {
    if (model != null) {
      final var currentId = recordDataModel.getRowCount();
      recordDataModel.addRow(model.setId(String.valueOf(currentId + 1)).toRow());
      this.recordHistory.setModel(this.recordDataModel);
    }
  }
}
