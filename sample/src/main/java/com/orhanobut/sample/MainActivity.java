package com.orhanobut.sample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.orhanobut.logger.expand.MyDiskCsvFormatStrategy;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

  private int REQUEST_WRITE_EXTERNAL_STORAGE=1011;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    checkPermission();
    Log.d("Tag", "I'm a log which you don't see easily, hehe");
    Log.d("json content", "{ \"key\": 3, \n \"value\": something}");
    Log.d("error", "There is a crash somewhere or any warning");

    Logger.addLogAdapter(new AndroidLogAdapter());
    Logger.d("message");

    Logger.clearLogAdapters();


    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
        .methodCount(0)         // (Optional) How many method line to show. Default 2
        .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
        .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
        .build();

    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    Logger.addLogAdapter(new AndroidLogAdapter() {
      @Override public boolean isLoggable(int priority, String tag) {
        return BuildConfig.DEBUG;
      }
    });

    Logger.addLogAdapter(new DiskLogAdapter());


    Logger.w("no thread info and only 1 method");

    Logger.clearLogAdapters();
    formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .build();

    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    Logger.i("no thread info and method info");

    Logger.t("tag").e("Custom tag for only one use");

    Logger.json("{ \"key\": 3, \"value\": something}");

    Logger.d(Arrays.asList("foo", "bar"));

    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map.put("key1", "value2");

    Logger.d(map);

    Logger.clearLogAdapters();
    formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .tag("MyTag")
        .build();
    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    Logger.w("my log message with my tag");



    //自定义的日志存储,默认储存在/sdcard/JYT/Log文件夹,默认最大储存60个日志文件
    MyDiskCsvFormatStrategy myDiskLogStrategy = MyDiskCsvFormatStrategy.newBuilder()
            .tag("APP")
            .setLogPath(Environment.getExternalStorageDirectory().getPath()+File.separator+"11")
            .setLogFileMax(30)
            .build();
    Logger.addLogAdapter(new DiskLogAdapter(myDiskLogStrategy));


    Logger.e("写入sdcard");
    Logger.e("测试-------------------");
  }

  private static final int REQUEST_EXTERNAL_STORAGE = 1;

  private static String[] PERMISSIONS_STORAGE = {
          "android.permission.READ_EXTERNAL_STORAGE",
          "android.permission.WRITE_EXTERNAL_STORAGE" };

  private void checkPermission() {
    //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
      //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
              .WRITE_EXTERNAL_STORAGE)) {
        Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
      }
      //申请权限
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);

    } else {
      Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();

    }
  }



}
