package com.orhanobut.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.orhanobut.logger.Utils.checkNotNull;

/**
 * This is used to saves log messages to the disk.
 * By default it uses {@link CsvFormatStrategy} to translates text message into CSV format.
 */
public class DiskLogAdapter implements LogAdapter {

   private final FormatStrategy formatStrategy;

  public DiskLogAdapter() {
    formatStrategy = CsvFormatStrategy.newBuilder().build();
  }

  public DiskLogAdapter( FormatStrategy formatStrategy) {
    this.formatStrategy =formatStrategy;
  }

  @Override public boolean isLoggable(int priority, @Nullable String tag) {
    return true;
  }

  @Override public void log(int priority, @Nullable String tag, @NonNull String message) {
    if(formatStrategy!=null)formatStrategy.log(priority, tag, message);
  }
}
