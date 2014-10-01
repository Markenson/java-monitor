package br.com.markenson.monitor.java;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final int MAX_LOOP = 10000;

	public static void main( String[] args )
    {
    	long initTime = System.currentTimeMillis();
    	for (int i = 0; i < MAX_LOOP; i++) {
        	exibirTexto1();
        	exibirTexto2();
//        	System.out.println(i);
		}
    	long endTime = System.currentTimeMillis();
    	System.out.println("Total time (ms):" + (endTime-initTime));
    }

	private static void exibirTexto1() {
   		int value = 1;
	}

	private static void exibirTexto2() {
		int value = 1;
	}

}
