package com.swing.client.app.service;

import static com.swing.client.app.config.ApplicationConfig.DATETIME_PATTERN;

import com.swing.client.app.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * The type History record service.
 */
public class HistoryRecordService {

  public TableModel searchClientRelated(
      final String value, final TableModel tableModel) {

    System.out.printf("Search client relation record by value '%s'%n", value);
    var resultModel = new DefaultTableModel();
    if (StringUtils.notNullOrEmpty(value)) {

      final var columnCount = tableModel.getColumnCount();
      final var rowCount = tableModel.getRowCount();
      List<String> columns = new ArrayList<>();
      for (var i = 0; i < columnCount; i++) {
        final var columnName = tableModel.getColumnName(i);
        resultModel.addColumn(columnName);
        columns.add(String.valueOf(columnName));
      }

      final var searchColumnIndex = columnCount - 1;
      for (var i = 0; i < rowCount; i++) {
        final var comparedValue = String.valueOf(
            tableModel.getValueAt(i, searchColumnIndex));
        if (comparedValue.contains(value)) {
          List<String> values = new ArrayList<>();
          for (var j = 0; j < columnCount; j++) {
            final var copyValue = tableModel.getValueAt(i, j);
            values.add(String.valueOf(copyValue));
          }
          System.out.printf("Found record: %s%n", values);
          resultModel.addRow(values.toArray());
        }
      }
    } else {
      System.err.println("Search value is null or empty");
    }

    return resultModel;
  }

  /**
   * Search log by key and value table model.
   *
   * @param key        the key
   * @param value      the value
   * @param tableModel the table model
   * @return the table model
   */
  public TableModel searchByKeyAndValue(
      final String key, final String value, final TableModel tableModel) {
    System.out.printf("Search key '%s' by value '%s'%n", key, value);
    var resultModel = new DefaultTableModel();
    final var columnCount = tableModel.getColumnCount();
    final var rowCount = tableModel.getRowCount();
    List<String> columns = new ArrayList<>();
    for (var i = 0; i < columnCount; i++) {
      final var columnName = tableModel.getColumnName(i);
      resultModel.addColumn(columnName);
      columns.add(String.valueOf(columnName));
    }

    if (StringUtils.notNullOrEmpty(key)
        && StringUtils.notNullOrEmpty(value)) {

      int searchColumnIndex = findSearchColumnIndex(key, columns);
      if (searchColumnIndex != -1) {
        for (var i = 0; i < rowCount; i++) {
          final var comparedValue = String.valueOf(
              tableModel.getValueAt(i, searchColumnIndex));
          if (comparedValue.contains(value)) {
            List<String> values = new ArrayList<>();
            for (var j = 0; j < columnCount; j++) {
              final var copyValue = tableModel.getValueAt(i, j);
              values.add(String.valueOf(copyValue));
            }
            System.out.printf("Found record: %s%n", values);
            resultModel.addRow(values.toArray());
          }
        }
      }
    } else {
      System.err.println("Search key or search value is null or empty");
    }

    return resultModel;
  }

  /**
   * Find search column index int.
   *
   * @param key     the key
   * @param columns the columns
   * @return the int
   */
  private int findSearchColumnIndex(String key, List<String> columns) {
    int searchColumnIndex = -1;
    for (int i = 0; i < columns.size(); i++) {
      String c = columns.get(i);
      if (c.equals(key)) {
        searchColumnIndex = i;
        break;
      }
    }
    return searchColumnIndex;
  }

  /**
   * Export log boolean.
   *
   * @param logPath    the file path
   * @param tableModel the table model
   * @return the boolean
   */
  public boolean exportLog(String logPath, TableModel tableModel) {
    final var rowCount = tableModel.getRowCount();
    final var columnCount = tableModel.getColumnCount();
    String logFilePath = this.getLogFilePath(logPath);
    System.out.printf("Start export history to file '%s'%n", logFilePath);
    final var path = Path.of(logFilePath);
    File csvOutputFile = new File(path.toString());
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
      List<String> columns = new ArrayList<>();
      for (var i = 0; i < columnCount; i++) {
        columns.add(String.valueOf(tableModel.getColumnName(i)));
      }
      pw.println(this.convertToCSV(columns));

      for (var i = 0; i < rowCount; i++) {
        List<String> values = new ArrayList<>();
        for (var j = 0; j < columnCount; j++) {
          final var value = tableModel.getValueAt(i, j);
          values.add(String.valueOf(value));
        }
        pw.println(this.convertToCSV(values));
      }
      System.out.printf("Success export history to file '%s'%n", logFilePath);
      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Load log table model.
   *
   * @param file the file
   * @return the table model
   * @throws IOException the io exception
   */
  public TableModel loadLog(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = reader.readLine();
    var model = new DefaultTableModel();
    model.setColumnIdentifiers(StringUtils.escapeSpecialCharacters(line).split(","));
    line = reader.readLine();
    while (line != null) {
      model.addRow(StringUtils.escapeSpecialCharacters(line).split(","));
      line = reader.readLine();
    }
    reader.close();
    return model;
  }

  /**
   * Gets log file path.
   *
   * @param path the path
   * @return the log file path
   */
  private String getLogFilePath(String path) {
    return path.concat(String.format("/log-%s.csv",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_PATTERN))));
  }

  /**
   * Convert to csv string.
   *
   * @param data the data
   * @return the string
   */
  private String convertToCSV(List<String> data) {
    return data.stream()
        .map(StringUtils::escapeSpecialCharacters)
        .collect(Collectors.joining(","));
  }

}
