package cn.ywj.www.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class MaintainPool implements Runnable {
    @Override
    public void run() {

        SharedPool sharedPool = SharedPool.getInstance();
        Hashtable<String, Object> back = new Hashtable<>();


        while (true) {
            try {
                for (Object o : sharedPool.getHt().values().toArray()) {

                    Hashtable<String, Object> mso = (Hashtable<String, Object>) o;


                    String file = String.valueOf(mso.get("path"));
                    String name = String.valueOf(mso.get("name"));
                    long old = Long.valueOf(String.valueOf(mso.get("status")));
                    long now = new Date().getTime();


                    if (name.contains("create")) {
                        File f = new File(file);
                        if (!f.exists())  f.createNewFile();
                        sharedPool.getHt().remove(file, o);
                        continue;
                    }

                    if (name.contains("delete")) {
                        File f = new File(file);
                        f.deleteOnExit();
                        sharedPool.getHt().remove(file, o);
                        continue;
                    }


                    Document obj = (Document)mso.get("obj");
                    if ((now - old)/1000 > 120) { //移除没有再使用的document
                        sharedPool.getHt().remove(file, o);
                        back.remove(file, obj.asXML());
                        continue;
                    }


                    if (back.containsKey(file)) {
                        String o1 = (String)back.get(file);

                        if (o1.equals(obj.asXML())) continue;

                        back.put(file, obj.asXML());

                    }else {
                        back.put(file, obj.asXML());
                    }




                    Document document = obj;

                    //格式化
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    //指定字符编码格式
                    format.setEncoding("utf8");
                    XMLWriter writer = null;

                    URL url = Thread.currentThread().getContextClassLoader().getResource(file);

                    if (url == null) continue;

                    try {
                        OutputStreamWriter outputStreamWriter =
                                new OutputStreamWriter(new FileOutputStream(url.getFile()), StandardCharsets.UTF_8);

                        BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                        writer = new XMLWriter(bw, format);

                        writer.write(document);




                    } catch (FileNotFoundException e) {
                        System.err.println(file + "：未找到错误");
                    } catch (IOException e) {
                        System.err.println(file + "：写入错误");
                    } finally {
                        if (writer != null)
                            writer.close();
                    }
                }

                Thread.sleep(3000);

            } catch (InterruptedException | IOException e) {
                System.err.println( "sleep 错误！:" + e);
            }
        }

    }
}
