package br.com.markenson.monitor.java;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javassist.*;

public class Transformator implements ClassFileTransformer {

	private String clazz;

	public Transformator(String clazz) {
		this.clazz = clazz;
	}

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;

		if (className.replace("/", ".").matches(clazz)) {
			System.out.println("Instrumenting " + className);
			try {

				ClassPool cp = ClassPool.getDefault();
				CtClass cc = null;
				try {
					cc = cp.get(className.replace("/", "."));
				}catch (javassist.NotFoundException nfe){
					cp.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
					cc = cp.get(className.replace("/", "."));
				}

				if (!cc.isInterface()){
					for (CtMethod m : cc.getDeclaredMethods()) {
						if((m.getModifiers() & Modifier.ABSTRACT) != Modifier.ABSTRACT){
							System.out.println("              ." + m.getMethodInfo().getName());
							m.addLocalVariable("elapsedTime", CtClass.longType);
							m.addLocalVariable("timestamp", CtClass.longType);
							m.addLocalVariable("msg", ClassPool.getDefault().get("java.lang.String"));
							m.insertBefore("elapsedTime = System.currentTimeMillis();timestamp = System.currentTimeMillis();");
							m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
									+ "msg= timestamp + \"," + cc.getName() + "." + m.getName() + m.getSignature() + ", \" + elapsedTime + \" ms\";"
									+ "java.util.logging.Logger.getLogger(\"br.com.markenson.monitor.java\").info(msg);}");
						}
					}
					byteCode = cc.toBytecode();
					cc.detach();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}else{
			//System.out.println("Avoiding " + className);
		}
		
		return byteCode;

	}

}
