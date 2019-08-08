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
### 初始化
#### 1、配置Logcat打印日志
```java
FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
  .showThreadInfo(false)  // (可选)是否打印线程信息,默认打印
  .methodCount(0)         // (可选)显示的方法行数，默认为2
  .methodOffset(7)        // (可选)隐藏内部方法调用到偏移量，默认为5
  .logStrategy(customLog) // (可选) 更改要打印的日志策略。
  .tag("My custom tag")   // (可选) 每个日志的全局标记。默认PRETTY_LOGGER
  .build();

Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
```

 

#### 2、配置保存日志到文件
```java
 MyDiskCsvFormatStrategy myDiskLogStrategy = MyDiskCsvFormatStrategy.newBuilder()
            .tag("APP")
            .setLogPath(Environment.getExternalStorageDirectory().getPath()+File.separator+"11")//设置日志存储的文件夹
            .setLogFileMax(30)//设置日志文件最大存储的个数
	    .setLogFileMax(20*1024*1024)//设置单个文件的最大大小，大于该值切换新的日志文件，默认20兆
            .build();
    Logger.addLogAdapter(new DiskLogAdapter(myDiskLogStrategy));
```
  
 ####   3、设置打印日志打印级别
 
如果要隐藏或者不打印日志, override `isLoggable` 方法. 
返回 `true` 表示打印日志, 返回`false` 不打印日志.
```java
Logger.addLogAdapter(new AndroidLogAdapter() {
  @Override public boolean isLoggable(int priority, String tag) {
    return BuildConfig.DEBUG;
  }
});
```

#### 4、打印日志
 
 
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

### Output
<img src='https://github.com/my11712/logger/blob/master/art/logger_output.png'/>

<img src='https://github.com/my11712/logger/blob/master/art/20190808161403.png'/>
