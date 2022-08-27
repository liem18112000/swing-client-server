package com.swing.client.app.util;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;

/**
 * The type Close application handler.
 */
public class DefaultCloseApplicationHandler
    extends WindowAdapter implements WindowListener {

  /**
   * The Parent component.
   */
  protected final Component parentComponent;

  /**
   * Instantiates a new Close application handler.
   *
   * @param parentComponent the parent component
   */
  public DefaultCloseApplicationHandler(Component parentComponent) {
    this.parentComponent = parentComponent;
  }

  /**
   * Window closing.
   *
   * @param e the e
   */
  @Override
  public void windowClosing(WindowEvent e) {
    JOptionPane.showMessageDialog(parentComponent, "Close application");
  }
}
