package com.swing.server.app.service;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import com.swing.server.app.controller.AppController;
import com.swing.server.app.model.Action;
import com.swing.server.app.model.HistoryRecordModel;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;

/**
 * The type Directory tracking service.
 */
public class DirectoryTrackingService extends Thread {

  /**
   * The Watch service.
   */
  private final WatchService watchService;

  /**
   * The Path.
   */
  private final Path path;

  /**
   * Instantiates a new Directory tracking service.
   *
   * @param watchService the watch service
   * @param path         the path
   */
  public DirectoryTrackingService(WatchService watchService, Path path) {
    this.watchService = watchService;
    this.path = path;
  }

  /**
   * Track.
   *
   * @param path the resource
   */
  @Override
  @SuppressWarnings("all")
  public void run() {
    try {

      // Register the directory with the watch service
      WatchKey watchKey = path.register(this.watchService,
          ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);

      // Poll for events
      while (true) {
        for (WatchEvent<?> event : watchKey.pollEvents()) {
          WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
          Path fileName = pathEvent.context();
          WatchEvent.Kind<?> kind = event.kind();
          dispatchWatchEvent(path, fileName, kind);
        }

        // Reset the watch key everytime for continuing to use it for further event polling
        if (!watchKey.reset()) {
          break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dispatch watch event.
   *
   * @param path     the path
   * @param fileName the file name
   * @param kind     the kind
   */
  private void dispatchWatchEvent(Path path, Path fileName, Kind<?> kind) {
    Action action = null;
    if (kind == ENTRY_CREATE) {
      System.out.println("A new folder or new file is created : " + fileName);
      action = Action.CREATE_ITEM;
    }

    if (kind == ENTRY_DELETE) {
      System.out.println("A folder or file has been deleted: " + fileName);
      action = Action.DELETE_ITEM;
    }

    if (kind == ENTRY_MODIFY) {
      System.out.println("A folder or file has been modified: " + fileName);
      action = Action.UPDATE_ITEM;
    }

    if (kind == OVERFLOW) {
      System.out.println("A folder or file has been overflowed: " + fileName);
      action = Action.OVERFLOW_ITEM;
    }

    if (action == null) {
      System.err.printf("Action is not recognized by event kind: %s%n", kind.name());
    } else {
      if (AppController.historyController != null) {
        AppController.historyController.updateRecordTable(
            HistoryRecordModel.build()
                .setTimestamp(LocalDateTime.now().toString())
                .setDirectory(path.toString().concat("/").concat(fileName.toString()))
                .setAction(action)
                .setDescription(kind.name())
        );
      } else {
        System.err.println("App history is not initialized");
      }
    }


  }
}
