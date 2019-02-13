
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

打印字符串
```java
Logger.d("hello %s", "world");
```

打印集合
```java
Logger.d(MAP);
Logger.d(SET);
Logger.d(LIST);
Logger.d(ARRAY);
```

打印Json和XML (output will be in debug level)
```java
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
```

### Advanced
```java
FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
  .showThreadInfo(false)  // (可选)是否打印线程信息,默认打印
  .methodCount(0)         // (可选) How many method line to show. Default 2
  .methodOffset(7)        // (可选) Hides internal method calls up to offset. Default 5
  .logStrategy(customLog) // (可选) Changes the log strategy to print out. Default LogCat
  .tag("My custom tag")   // (可选) Global tag for every log. Default PRETTY_LOGGER
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

### 保存日志到文件
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
  
 
