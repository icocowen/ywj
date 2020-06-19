package cn.ywj.www.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SharedPool {



    private  final Hashtable<String, Object> ht = new Hashtable<>();
    private static volatile SharedPool sharedPool;

    public Hashtable<String, Object> getHt(){
        return this.ht;
    }

    private SharedPool() {
        Thread thread = new Thread(new MaintainPool(), "维护线程池");
        thread.setPriority(2);
        thread.start();
    }

    public static synchronized SharedPool getInstance() {
        if (sharedPool == null) {
            sharedPool = new SharedPool();
        }
        return sharedPool;
    }

    public  boolean isExists(String key) {
        return ht.containsKey(key);
    }

    public synchronized Object getObj(String key) {
        Map<String, Object> h = (Hashtable<String, Object>)ht.get(key);
        h.put("status", String.valueOf(new Date().getTime()));
        return h.get("obj");
    }

    public synchronized void setObj(String key, Object o, String path) {
        Map<String, Object> h = new Hashtable<>(3);
        h.put("status", String.valueOf(new Date().getTime()));
        h.put("name", key);
        if (o.equals("create_") || o.equals("delete_")) {
            h.put("name", o);
        }else {
            h.put("obj", o);
        }

        h.put("path", path);
        ht.put(key, h);
    }


    public synchronized void writeObj(String key, Object o) {
        Map<String, Object> h = (Hashtable<String, Object>)ht.get(key);
        h.put("status", String.valueOf(new Date().getTime()));
        h.put("obj", o);
    }



}
