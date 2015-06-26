package br.com.markenson.monitor.java;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

/**
 * Usage:
 * -javaagent:D:\Trabalhos\Java\Repositorios\_markenson\java-monitor\target
 * \java-monitor-1.0-SNAPSHOT.jar=-class=br.com.markenson.monitor.java.App
 * -Djava.util.logging.config.file=D:\Publica\logging.properties
 * 
 * @author markenson
 * 
 */
public class Agent {

	private static final Logger log;

	static{
		log = java.util.logging.Logger.getLogger("br.com.markenson.monitor.java");

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                Agent.onExit();
            }
        });
	}

	private static String clazz;
	
	public static void premain(String agentArgs, Instrumentation inst) {
		processParams(agentArgs);
		inst.addTransformer(new Transformator(clazz));
	}

	private static void processParams(String agentArgs) {
		if (agentArgs.startsWith("-class=")){
            clazz = agentArgs.split("=")[1];

            if (clazz.replace("/", ".").matches("(java\\..*|sun\\..*)")){
                System.out.println("********************    WARNING!  **********************");
                System.out.println("The -class= param includes java.* and sun.* classes!");
                System.out.println("                Your system MAY CRASH!");
                System.out.println("********************    WARNING!  **********************");
            }

        }
	}

    private static void onExit() {
        System.out.println("Java Monitor exited!");
    }

}