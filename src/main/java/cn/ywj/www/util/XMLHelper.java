package cn.ywj.www.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取xml  和写入xml
 * id号的随机生成
 * 生成答卷文件
 *
 */
public class XMLHelper {
    private static final String USERS_CONFIG = "users";
    private static final String QUESTIONNAIRE_CONFIG = "questionnaires";
    private static final String QUESTIONNAIRE_MODEL_CONFIG = "questionnaire_models";
    private static final String ANSWER_CONFIG = "answer_";
    private static final String DB_BASE_PATH = "cn/ywj/www/util/db/";
    private static final SharedPool sharedPool = SharedPool.getInstance();



    /**
     * 读取xml文件
     *
     * @param file  文件的相对路径
     */
    private static synchronized   Document load(String file) throws DocumentException {
        SAXReader reader = new SAXReader();
//        InputStream in = XMLHelper.class.getClassLoader().getResourceAsStream(file);



        if (sharedPool.isExists(file)) {
            Object obj = sharedPool.getObj(file);
            return (Document)obj;
        }


        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);

        Document read = reader.read(in);

        sharedPool.setObj(file, read, file);

        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return read;

    }



    /**
     * 写入xml文件
     *
     * @param file  文件的相对路径
     * @param document  要写入的doc
     * @throws IOException  文件未找到
     */
    private static synchronized   void write(String file, Document document) throws IOException {

        if (sharedPool.isExists(file)) {
                sharedPool.writeObj(file, document);
        }else {
              sharedPool.setObj(file, document, file);
        }


        //格式化
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        //指定字符编码格式
//        format.setEncoding("utf8");
//        XMLWriter writer = null;
//
//        URL url = Thread.currentThread().getContextClassLoader().getResource(file);
//        assert url != null;
//        String key = url.getPath();
//
//        if (key.indexOf("answer") >= 0) {
//            key = "answer_write_" + key.hashCode();
//        }else {
//            key = "write"+ key.hashCode();
//        }


//        try {
//            if (sharedPool.isExists(key)) {
//                writer = (XMLWriter)sharedPool.getObj(key);
//            }else {
//                writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(url.getFile()), StandardCharsets.UTF_8),format);
//                sharedPool.setObj(key, writer);
//            }
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(url.getFile()), StandardCharsets.UTF_8);
//
//            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
//            writer = new XMLWriter(bw,format);
//
//            writer.write(document);
//
//        } catch (FileNotFoundException e) {
//            throw new FileNotFoundException(USERS_CONFIG+ " XML 文件没有找到！");
//        } catch (IOException e) {
//            throw new IOException(USERS_CONFIG+ " 写入失败！");
//        }finally {
//            writer.close();
//        }
    }


    public Document  loadUsersDoc() throws DocumentException {
        return load(DB_BASE_PATH+USERS_CONFIG+".xml");
    }


    public void  writeUsersDoc(Document document) throws IOException {
        write(DB_BASE_PATH+USERS_CONFIG+".xml", document);
    }


    public Document  loadQuestionnairesDoc() throws DocumentException {
        return load(DB_BASE_PATH+QUESTIONNAIRE_CONFIG+".xml");
    }


    public void  writeQuestionnairesDoc(Document document) throws IOException {
        write(DB_BASE_PATH+QUESTIONNAIRE_CONFIG+".xml", document);
    }


    public Document  loadQuestionnaireModelsDoc() throws DocumentException {
        return load(DB_BASE_PATH+QUESTIONNAIRE_MODEL_CONFIG+".xml");
    }


    public void  writeQuestionnaireModelsDoc(Document document) throws IOException {
        write(DB_BASE_PATH+QUESTIONNAIRE_MODEL_CONFIG+".xml", document);
    }


    public Document  loadAnswersDoc(String questionnaireId) throws DocumentException {
        return load(DB_BASE_PATH+ANSWER_CONFIG+questionnaireId+".xml");
    }


    public void  writeAnswersDoc(String questionnaireId, Document document) throws IOException {
        write(DB_BASE_PATH+ANSWER_CONFIG+questionnaireId+".xml", document);
    }


    public static String getNodeElementText(Node node, String nodeName) {
        return ((DefaultElement)node).element(nodeName).getText();
    }


    public static String getNodeAttributeText(Node node, String nodeName) {
        return ((DefaultElement)node).attribute(nodeName).getValue();
    }


    public static  void  createAnswersDoc(String questionnaireId, Document document) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(DB_BASE_PATH+USERS_CONFIG+".xml");
        assert url != null;
        String path = url.getPath().replaceAll(USERS_CONFIG+".xml", "") + ANSWER_CONFIG+questionnaireId+".xml";

        sharedPool.setObj(path, "create_", path);

        write(DB_BASE_PATH+ANSWER_CONFIG+questionnaireId+".xml", document);
    }


    public static  void  deleteAnswerFile(String questionnaireId) throws IOException {

        URL url = Thread.currentThread().getContextClassLoader().getResource(DB_BASE_PATH+ANSWER_CONFIG+questionnaireId+".xml");
        assert url != null;
        String path = url.getPath();
        sharedPool.setObj(path, "delete_", path);

    }


    public static boolean isPhone(String phone){
        String check = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }


}
