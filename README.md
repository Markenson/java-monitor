java-monitor
============

Simple java agent monitor that collect information about classes at runtime


How build it?
=============

After clone it, install maven and type the following command:

```
mvn clean install
```

How use it?
===========

Add the following parameters to your java application at startup

```
-javaagent:[JAVA_MONITOR_DIR]/java-monitor-1.0-SNAPSHOT.jar=-class=[REGEX_CLASS] -Djava.util.logging.config.file=[LOG_DIR]/logging.properties
```
where

```
-javaagent - location of java-monitor.jar.
-class - regular expression to match with class name that will be monitored.
-Djava.util.logging.config.file - Optional. location of logging.properties with log configuration (destination of log [file, stream, etc] and others configurations). If not specified, [JRE_HOME]/lib/logging.properties will be used.
```

loggin.properties sample

```
handlers= java.util.logging.FileHandler,java.util.logging.ConsoleHandler

.level= INFO

java.util.logging.FileHandler.pattern = %h/java%u.log
java.util.logging.FileHandler.limit = 5000000
java.util.logging.FileHandler.count = 10
java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
java.util.logging.SimpleFormatter.format=[%1$tc] %5$s%n 

java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter

```

Get results on 
```
{user.home}\java.log
```

Usage example

```
java -jar some-app.jar -javaagent:C:\repo\java-monitor\target\java-monitor-1.0-SNAPSHOT.jar=-class=.* -Djava.util.logging.config.file=c:\logs\logging.properties
```
