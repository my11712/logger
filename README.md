[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Logger-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1658) [![](https://img.shields.io/badge/AndroidWeekly-%23147-blue.svg)](http://androidweekly.net/issues/issue-147)
[![Join the chat at https://gitter.im/orhanobut/logger](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/orhanobut/logger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) <a href="http://www.methodscount.com/?lib=com.orhanobut%3Alogger%3A2.0.0"><img src="https://img.shields.io/badge/Methods and size-198 | 18 KB-e91e63.svg"/></a> [![Build Status](https://travis-ci.org/orhanobut/logger.svg?branch=master)](https://travis-ci.org/orhanobut/logger)

<img align="right" src='https://github.com/orhanobut/logger/blob/master/art/logger-logo.png' width='128' height='128'/>

### 3.0.0 新增设置日志按照日期自动存储
##### setLogPath 设置日志存储的文件夹
##### setLogFileMax 设置日志文件最大存储的个数
#
```java
 MyDiskCsvFormatStrategy myDiskLogStrategy = MyDiskCsvFormatStrategy.newBuilder()
            .tag("APP")
            .setLogPath(Environment.getExternalStorageDirectory().getPath()+File.separator+"11")//设置日志存储的文件夹
            .setLogFileMax(30)//设置日志文件最大存储的个数
            .build();
    Logger.addLogAdapter(new DiskLogAdapter(myDiskLogStrategy));
```

### Logger
Simple, pretty and powerful logger for android

### Setup
Download
```groovy

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
implementation 'com.github.my11712:logger:3.0.0'
```

Initialize
```java
Logger.addLogAdapter(new AndroidLogAdapter());
```
And use
```java
Logger.d("hello");
```

### Output
<img src='https://github.com/orhanobut/logger/blob/master/art/logger_output.png'/>


### Options
```java
Logger.d("debug");
Logger.e("error");
Logger.w("warning");
Logger.v("verbose");
Logger.i("information");
Logger.wtf("What a Terrible Failure");
```

String format arguments are supported
```java
Logger.d("hello %s", "world");
```

Collections are supported (only available for debug logs)
```java
Logger.d(MAP);
Logger.d(SET);
Logger.d(LIST);
Logger.d(ARRAY);
```

Json and Xml support (output will be in debug level)
```java
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
```

### Advanced
```java
FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
  .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
  .methodCount(0)         // (Optional) How many method line to show. Default 2
  .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
  .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
  .tag("My custom tag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
  .build();

Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
```

### Loggable
检查是否应打印日志
如果要隐藏或者不打印日志, override `isLoggable` 方法. 
返回 `true` 表示打印日志, 返回`false` 不打印日志.
```java
Logger.addLogAdapter(new AndroidLogAdapter() {
  @Override public boolean isLoggable(int priority, String tag) {
    return BuildConfig.DEBUG;
  }
});
```

### Save logs to the file
//TODO: More information will be added later
```java
//普通的日志打印,只保存在一个日志文件里
Logger.addLogAdapter(new DiskLogAdapter());

//按照日期保存日志文件
 MyDiskCsvFormatStrategy myDiskLogStrategy = MyDiskCsvFormatStrategy.newBuilder()
            .tag("APP")
            .setLogPath(Environment.getExternalStorageDirectory().getPath()+File.separator+"11")//设置日志存储的文件夹
            .setLogFileMax(30)//设置日志文件最大存储的个数
            .build();
    Logger.addLogAdapter(new DiskLogAdapter(myDiskLogStrategy));
```
  
</pre>
