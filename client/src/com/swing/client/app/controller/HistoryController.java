package com.swing.client.app.controller;

import static com.swing.client.app.config.ApplicationConfig.HISTORY_RECORD_COLUMN_IDENTIFIERS;

import com.swing.client.app.model.HistoryRecordModel;
import com.swing.client.app.service.HistoryRecordService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * The type History controller.
 */
public class HistoryController {

  /**
   * The App panel.
   */
  private JPanel appPanel;

  /**
   * The Quit button.
   */
  private JButton quitButton;

  /**
   * The Disconnect button.
   */
  private JButton disconnectButton;

  /**
   * The Help button.
   */
  private JButton helpButton;

  /**
   * The History record table.
   */
  private JTable historyRecordTable;

  /**
   * The Connection information.
   */
  private JTextField connectionInformation;

  /**
   * The constant mainFrame.
   */
  public static JFrame mainFrame;

  /**
   * The Record data model.
   */
  private final DefaultTableModel recordDataModel = new DefaultTableModel(0, 0);

  private final HistoryRecordService historyRecordService;

  /**
   * Instantiates a new History controller.
   */
  public HistoryController() {

    this.historyRecordService = new HistoryRecordService();

    this.initializedConfiguration();

    this.addEventListenerToComponents();
  }

  /**
   * Initialized configuration.
   */
  private void initializedConfiguration() {
    this.recordDataModel.setColumnIdentifiers(HISTORY_RECORD_COLUMN_IDENTIFIERS);
  }

  private void addEventListenerToComponents() {
    quitButton.addActionListener(e -> {
      int confirm = JOptionPane.showOptionDialog(appPanel,
          "Are You Sure to Close Application?",
          "Exit Confirmation", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, null, null);
      if (confirm == JOptionPane.YES_OPTION) {
        System.exit(0);
      }
    });

    disconnectButton.addActionListener(e -> {
      int confirm = JOptionPane.showOptionDialog(appPanel,
          "Are You Sure to Close Connection?",
          "Close Connection Confirmation", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, null, null);
      if (confirm == JOptionPane.YES_OPTION) {
        this.recordDataModel.setDataVector(new Object[][]{}, new Object[]{});
        mainFrame.setVisible(false);
        AppController.mainFrame.setVisible(true);
      }
    });
  }

  /**
   * Gets app panel.
   *
   * @return the app panel
   */
  public JPanel getAppPanel() {
    return appPanel;
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
      this.historyRecordTable.setModel(this.recordDataModel);
    }
  }
}
