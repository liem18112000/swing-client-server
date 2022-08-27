package com.swing.client.app.model;

import java.io.Serializable;

/**
 * The type History record model.
 */
public class HistoryRecordModel implements Serializable {

  /**
   * The Id.
   */
  private String id;

  /**
   * The Timestamp.
   */
  private String timestamp;

  /**
   * The Directory.
   */
  private String directory;

  /**
   * The Action.
   */
  private Action action;

  /**
   * The Description.
   */
  private String description;

  /**
   * Build history record model.
   *
   * @return the history record model
   */
  public static HistoryRecordModel build() {
    return new HistoryRecordModel();
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   * @return the id
   */
  public HistoryRecordModel setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   * @return the timestamp
   */
  public HistoryRecordModel setTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public Action getAction() {
    return action;
  }

  /**
   * Sets action.
   *
   * @param action the action
   * @return the action
   */
  public HistoryRecordModel setAction(Action action) {
    this.action = action;
    return this;
  }

  /**
   * Gets directory.
   *
   * @return the directory
   */
  public String getDirectory() {
    return directory;
  }

  /**
   * Sets directory.
   *
   * @param directory the directory
   * @return the directory
   */
  public HistoryRecordModel setDirectory(String directory) {
    this.directory = directory;
    return this;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description.
   *
   * @param description the description
   * @return the description
   */
  public HistoryRecordModel setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * To row object [ ].
   *
   * @return the object [ ]
   */
  public Object[] toRow() {
    return new Object[] {
        this.getId(),
        this.getDirectory(),
        this.getTimestamp(),
        this.getAction().name(),
        this.getDescription()
    };
  }
}
