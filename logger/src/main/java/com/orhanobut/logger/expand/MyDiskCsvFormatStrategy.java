package com.orhanobut.logger.expand;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;

/**
 * Created by Hongqizhi on 2018/4/24.
 */

public class MyDiskCsvFormatStrategy implements FormatStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";

    private final Date date;
    private final SimpleDateFormat dateFormat;
    private final LogStrategy logStrategy;
    private final String tag;

    private MyDiskCsvFormatStrategy(Builder builder) {
        date = builder.date;
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    static String logLevel(int value) {
        switch (value) {
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public void log(int priority, String onceOnlyTag, String message) {
        String tag = formatTag(onceOnlyTag);

        date.setTime(System.currentTimeMillis());

        StringBuilder builder = new StringBuilder();

        // human-readable date/time
        builder.append(dateFormat.format(date));

        // level
        builder.append(SEPARATOR);
        builder.append(logLevel(priority));

        // tag
        builder.append(SEPARATOR);
        builder.append(tag);

        // message
        if (message.contains(NEW_LINE)) {
            // a new line would break the CSV format, so we replace it here
            message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        if (message.contains(",")) {
            message = message.replaceAll(",", "，");
        }
        builder.append(SEPARATOR);
        builder.append(message);


        logStrategy.log(priority, tag, builder.toString());
    }

    private String formatTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            return tag;
        }
        return this.tag;
    }

    public static final class Builder {
        private static final int MAX_BYTES = 2 * 1024 * 1024; // 2M  averages to a 4000 lines per file

        Date date;
        SimpleDateFormat dateFormat;
        LogStrategy logStrategy;
        String tag = "PRETTY_LOGGER";
        int maxFileNum = 60;//默认日志文件保存60天
        private String diskPath =  Environment.getExternalStorageDirectory().getPath()+File.separator+ "Log";//默认日志文件保存路径

        private Builder() {
        }

        public Builder date(Date val) {
            date = val;
            return this;
        }

        public Builder setLogPath(String diskPath) {
            this.diskPath = diskPath;
            return this;
        }

        /**
         * 设置日志文件最多保存多少个文件
         *
         * @param maxFileNum
         */
        public Builder setLogFileMax(int maxFileNum) {
            this.maxFileNum = maxFileNum;
            return this;
        }

        public Builder dateFormat(SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }

        public Builder logStrategy(LogStrategy val) {
            logStrategy = val;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public MyDiskCsvFormatStrategy build() {
            if (date == null) {
                date = new Date();
            }
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
            }
            if (logStrategy == null) {
                HandlerThread ht = new HandlerThread("AndroidFileLogger." + diskPath);
                ht.start();
                Handler handler = new MyDiskLogStrategy.WriteHandler(ht.getLooper(), diskPath, MAX_BYTES);
                logStrategy = new DiskLogStrategy(handler);
            }
            //删除多余的日志文件
            File file = new File(diskPath);
            if (file.exists() && file.list().length > maxFileNum) {
                String[] fileNames = file.list();
                Arrays.sort(fileNames, new CompratorByLastModified());
                for (int i = 0; i < fileNames.length - maxFileNum; i++) {
                    File mFile = new File(file, fileNames[i]);
                    mFile.delete();
                }
            }
            return new MyDiskCsvFormatStrategy(this);
        }
    }
}
