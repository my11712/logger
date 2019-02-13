package com.orhanobut.logger.expand;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.orhanobut.logger.LogStrategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by  Hongqizhi on 2018/4/24.
 */

public class MyDiskLogStrategy implements LogStrategy {

    private final Handler handler;

    public MyDiskLogStrategy(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void log(int level, String tag, String message) {
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

        private void writeLog() {

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
                outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
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


        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(BufferedWriter fileWriter, String content) throws IOException {
            fileWriter.newLine();
            fileWriter.append(content);
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

            newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            }

            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }
    }
}
