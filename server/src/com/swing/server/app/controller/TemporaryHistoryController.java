package com.swing.server.app.controller;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * The type Temporary history.
 */
public class TemporaryHistoryController {

  /**
   * The Record table.
   */
  private JTable recordTable;

  /**
   * The Back button.
   */
  private JButton backButton;

  /**
   * The App panel.
   */
  private JPanel appPanel;

  /**
   * The constant mainFrame.
   */
  public static JFrame mainFrame;

  /**
   * Instantiates a new Temporary history.
   */
  public TemporaryHistoryController() {
    TemporaryHistoryController.mainFrame = new JFrame(TemporaryHistoryController.class.getCanonicalName());
    TemporaryHistoryController.mainFrame.setContentPane(appPanel);
    TemporaryHistoryController.mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    TemporaryHistoryController.mainFrame.pack();
    backButton.addActionListener(e -> {
      if (TemporaryHistoryController.mainFrame != null) {
        mainFrame.setVisible(false);
      }
    });
  }

  /**
   * Update record table.
   *
   * @param model the model
   */
  public void showTable(final TableModel model) {
    if (model != null) {
      this.recordTable.setModel(model);
      TemporaryHistoryController.mainFrame.setVisible(true);
    }
  }
}
