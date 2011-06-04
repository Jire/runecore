package com.gecko.util;

import sun.org.mozilla.javascript.internal.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.gecko.Constants;

/**
 * JavaScriptManager.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas + Lazero
 * 4 Jun 2011
 */
public class JavaScriptManager extends ImporterTopLevel {
	
	/**
	 * Variables
	 */
    private static final long serialVersionUID = 7548502465572769157L;
    private Map<String, Boolean> loadedScriptMap = new HashMap<String, Boolean>();
    private Map<String, Script> scriptMap = new HashMap<String, Script>();
    private Logger logger = Logger.getLogger(this.getClassName());

    /**
     * Constructor
     */
    public JavaScriptManager() {
        Context ctx = Context.enter();
       // ctx.initStandardObjects(this);
        initStandardObjects(ctx, true);
        String[] names = {"print", "invoke", "trace", "allocBytes", "castByte", "castInt", "castLong"};
        defineFunctionProperties(names, JavaScriptManager.class, ScriptableObject.DONTENUM);
        loopDir(ctx, new File(Constants.SCRIPT_DIR));
        logger.info("Loaded " + scriptMap.size() + " javascript script(s)");
        Context.exit();
    }

    /**
     * 
     * @param length
     * @return
     */
    public byte[] allocBytes(int length) {
        return new byte[length];
    }

    /**
     * Calls a script
     * @param call
     * @param loadAutomatically
     * @param args
     * @return
     */
    public Object call(String call, boolean loadAutomatically, Object... args) {
        Context ctx = Context.enter();
        Object results;
        int dotIndex = call.indexOf('.');
        String scriptName = call.substring(0, (dotIndex != -1 ? dotIndex : call.length()));
        String funcName = null;
        if (dotIndex != -1) {
            funcName = call.substring(dotIndex + 1, call.length());
        }
        Script script = scriptMap.get(scriptName);
        if (funcName == null) {
            if (args.length > 0) {
                throw new RuntimeException("Arguments are not supported when calling scripts from a static context!");
            }
            results = script.exec(ctx, this);
            loadedScriptMap.put(scriptName, true);
        } else {
            Boolean loaded = loadedScriptMap.get(scriptName);
            if (loaded == null) {
                loaded = Boolean.FALSE;
            }
            if (!loaded && loadAutomatically) {
                script.exec(ctx, this);
            }
            Scriptable scriptObj = (Scriptable) script;
            Object obj = this.get(funcName, this);
            if (!(obj instanceof Function)) {
                throw new RuntimeException("Requested function is not a function or does not exist");
            }
            Function func = (Function) obj;
            Object[] jsArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof Object) {
                    arg = Context.javaToJS(arg, this);
                }
                jsArgs[i] = arg;
            }
            results = func.call(ctx, this, scriptObj, jsArgs);
        }
        results = Context.jsToJava(results, Object.class);
        Context.exit();
        return results;
    }

    public Object call(String call, Object... args) {
        return call(call, true, args);
    }

    public byte castByte(Object o) {
        return ((Number) o).byteValue();
    }

    public int castInt(Object o) {
        return ((Number) o).intValue();
    }

    public long castLong(Object o) {
        return ((Number) o).longValue();
    }

    @Override
    public String getClassName() {
        return "global";
    }

    public Object invoke(String call) {
        return call(call);
    }

    private void loopDir(Context ctx, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                if(f.getName().endsWith(".js")) {
                    String name = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(File.separatorChar) + 1, f.getAbsolutePath().lastIndexOf(".js")).replace(File.separatorChar, '/');
                    try {
                        Script script = ctx.compileReader(new FileReader(f), name, 1, null);
                        Scriptable scriptObj = (Scriptable) script;
                        scriptObj.setParentScope(this);
                        scriptMap.put(name, script);
                    } catch (IOException e) {
                        System.err.println("Error loading script : " + name);
                        e.printStackTrace();
                    }
                }
            } else {
                loopDir(ctx, f);
            }
        }
    }

    public void print(String msg) {
        System.out.println(msg);
    }

    public void trace(Object msg, Object o) {
        new Throwable(o.toString()).printStackTrace();
    }

    public static Object castArray(Object type, NativeArray array) {
        Object array2 = java.lang.reflect.Array.newInstance(Context.jsToJava(array.get(0, null), (Class<?>) type).getClass(), (int) array.getLength());
        for(int i=0; i<array.getLength(); i++) {
            Array.set(array2, i, array.get(i, null));
        }
        return array2;
    }
}