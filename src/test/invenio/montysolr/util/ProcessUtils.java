package invenio.montysolr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
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
	* Adds the specified path to the java library path
	*
	* @param pathToAdd the path to add
	* @throws Exception
	* 
	* http://fahdshariff.blogspot.com/2011/08/changing-java-library-path-at-runtime.html
	*/
	public static void addLibraryPath(String pathToAdd) throws Exception{
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);
	    
	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);
	    
		if (! hasLibraryPath(pathToAdd)) {
		    //add the new path
		    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		    newPaths[newPaths.length-1] = pathToAdd;
		    usrPathsField.set(null, newPaths);
		}
	}
	
	
	public static boolean hasLibraryPath(String pathToAdd) throws Exception {
		
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);
	 
	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);
	 
	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return true;
	        }
	    }
		return false;
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
	 * Checks whether java.library.path is set and contains path to the
	 * JCC, if not, it will try to find it (but it is executing python,
	 * so it assumes that you are not running a different interpreter)
	 * 
	 * @throws Exception
	 */
	public static void checkJCCPath() throws Exception {
		
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);
		
		//get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);
	    
	    //check if the path to add is already present
	    for(String path : paths) {
	        if( (new File(path + "/libjcc.so")).canRead() ||  (new File(path + "/libjcc.a")).canRead()
	        		|| (new File(path + "/jcc.dll")).canRead()) {
	            return;
	        }
	    }
	    
	    
	    
	    String jlp = System.getProperty("java.library.path.ignore.montysolr");
	    if (jlp == null || jlp.trim().equals("")) {
	    	System.err.println("Warning: MontySolr thinks that JCC is not available. You should set -Djava.library.path");
	    	System.err.println("Warning: MontySolr will try to find JCC and add it to java.library.path");
	    	System.err.println("Warning: This is not guaranteed to work (we execute default 'python')");
	    	System.err.println("Warning: Alternatively, you can deactivate this by setting -Djava.library.path.ignore.montysolr=1");
	    	
	    	String jPath = getJCCPath();
	    	if (jPath != null && jPath.length() > 0) {
	    		System.err.println("Warning: Adding java.library.path=" + jPath);
	    		addLibraryPath(jPath.trim());
	    	}
	    	else {
	    		System.err.println("Warning: We were not successful in finding JCC. To help you debug...\n\n");
	    		System.err.println("python -c \"import sys;print sys.path\"");
	    		System.err.println(execCommand("python|-c|import sys;print \'\\n\'.join(sys.path)".split("\\|")));
	    		System.err.println("python -c \"import os;print os.environ\"");
	    		System.err.println(execCommand("python|-c|import os;print str(os.environ).replace(',',',\\n')".split("\\|")));
	    	}
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
	

	public static String execCommand(String[] cmd) throws Exception {
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
				out.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return out.toString();
	}
}
