package org.apache.solr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProcessUtils {
	
	/**
	 * Hack from http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
	 * but the lib must not have been loaded yet, better might be:
	 * http://fahdshariff.blogspot.com/2011/08/changing-java-library-path-at-runtime.html
	 * 
	 * @param path
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void setLibraryPath(String path) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		System.setProperty( "java.library.path", path );

		Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		Field[] ff = ClassLoader.class.getDeclaredFields();
		
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );
	}
	
	/**
	 * Prepends value to the existing env variable (or creates one)
	 * 
	 * @param name
	 * @param value
	 */
	public static void addEnv(String name, String value) {
		String old = System.getenv(name);
		if (old!=null) {
			value = value + System.getProperty("path.separator") + old;
		}
		Map vals = new HashMap<String, String>();
		vals.put(name, value);
		setEnv(vals);
	}
	
	/**
	 * Hack to setup environment variables, as presented at:
	 * http://stackoverflow
	 * .com/questions/318239/how-do-i-set-environment-variables-from-java
	 * 
	 * This is needed for unittesting
	 * 
	 * @param newenv
	 */
	public static void setEnv(Map<String, String> newenv) {
		try {
			Class<?> processEnvironmentClass = Class
					.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass
					.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField
					.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
					.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField
					.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			try {
				Class[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for (Class cl : classes) {
					if ("java.util.Collections$UnmodifiableMap".equals(cl
							.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>) obj;
						//map.clear();
						map.putAll(newenv);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Runs Python in the console and retrieves the location of the JCC egg
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String getJCCPath() throws Exception {
		String[] cmd = { "python", "-c",
				"import os, jcc; print os.path.dirname(os.path.dirname(jcc.__file__))" };
		return execCommand(cmd);
	}
	
	public static void loadJCC() throws Exception {
		String base = getJCCPath();
		String library = base + "/libjcc.so";
		System.load(library);
	}
	

	private static String execCommand(String[] cmd) throws Exception {
		Runtime run = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = run.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		String line = null;
		StringBuffer out = new StringBuffer();
		try {
			while ((line = buf.readLine()) != null) {
				out.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return out.toString();
	}
}
