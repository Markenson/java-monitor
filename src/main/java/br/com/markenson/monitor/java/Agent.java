package br.com.markenson.monitor.java;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Usage:
 * -javaagent:D:\Trabalhos\Java\Repositorios\_markenson\java-monitor\target
 * \java-monitor-1.0-SNAPSHOT.jar=-class=br.com.markenson.monitor.java.App
 * -Djava.util.logging.config.file=D:\Publica\logging.properties
 * 
 * @author samsung
 * 
 */
public class Agent {

	private static final ScheduledExecutorService executor;
	private static final Logger log;
	private static StringBuffer toLog = new StringBuffer();
	
	static{
		log = java.util.logging.Logger.getLogger("br.com.markenson.monitor.java");
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new SendToLogTask(log, toLog), 0, 1, TimeUnit.SECONDS);
	}

	private static String clazz;
	
	public static void premain(String agentArgs, Instrumentation inst) {
		processParams(agentArgs);
		inst.addTransformer(new Transformator(clazz));
	}

	private static void processParams(String agentArgs) {
		if (agentArgs.startsWith("-class=")){
			clazz = agentArgs.split("=")[1];
		}
	}

	public static void log(String msg) {
		toLog.append(msg);
		toLog.append("\n");
	}
	
}

class SendToLogTask implements Runnable{

	private Logger log;
	private StringBuffer toLog;
	
	public SendToLogTask(Logger log, StringBuffer toLog) {
		this.log = log;
		this.toLog = toLog;
	}
	
	public void run() {
		if (toLog.length()>0){
			log.info(toLog.toString());
			toLog=new StringBuffer();
		}
	}
	
}