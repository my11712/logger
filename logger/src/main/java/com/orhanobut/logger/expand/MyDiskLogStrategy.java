package com.orhanobut.logger.expand;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.orhanobut.logger.LogStrategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by  Hongqizhi on 2018/4/24.
 * modify by  Hongqizhi on 2019/08/08.
 */

public class MyDiskLogStrategy implements LogStrategy {

    private final Handler handler;

    public MyDiskLogStrategy(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void log(int level, String tag, @NonNull String message) {
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, message));
    }

    static class WriteHandler extends Handler {

        private final String folder;
        private final int maxFileSize;

        WriteHandler(Looper looper, String folder, int maxFileSize) {
            super(looper);
            this.folder = folder;
            this.maxFileSize = maxFileSize;
        }

        private String getFileName() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
            return simpleDateFormat.format(new Date());
        }




        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;
            BufferedWriter bw = null;
            OutputStreamWriter outputStreamWriter = null;
            FileOutputStream fileOutputStream = null;
            try {
                File logFile = getLogFile(folder, getFileName());
                fileOutputStream = new FileOutputStream(logFile, true);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));
                // 追记模式
                bw = new BufferedWriter(outputStreamWriter);
                bw.newLine();
                bw.append(content);
                bw.flush();
            } catch (IOException e) {

            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (outputStreamWriter != null) {
                        fileOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e1) { /* fail silently */ }
            }
        }




        private File getLogFile(String folderName, String fileName) {
            //获取日志文件,如果文件不存在,就创建,如果文件的大小大于maxFileSize,则创建新的日志文件
            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            int newFileCount = 0;
            File newFile;
            File existingFile = null;

            newFile = new File(folder, String.format(Locale.getDefault(),"%s_%d.csv", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format(Locale.getDefault(),"%s_%d.csv", fileName, newFileCount));
            }

            if (existingFile != null) {
                //如果当前的日志文件大小大于最大的size，则返回创建的文件
                if (existingFile.length() >= maxFileSize) {
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }
    }
}
