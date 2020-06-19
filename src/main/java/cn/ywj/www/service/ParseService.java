package cn.ywj.www.service;

import cn.ywj.www.dao.AnswersCRUD;
import cn.ywj.www.dao.QuestionnaireCRUD;
import cn.ywj.www.dao.QuestionnaireModeCRUD;
import cn.ywj.www.dao.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.NoSuchAttributeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析编码好的问卷模板
 * 规则：
 *  最前面的是问卷的标题  端午节大调查§§0
 *  题目与题目之间： §¤§
 *  标记题目类型： §;§radio§;§
 *  标记title:   〒§你喜欢什么口味的粽子?§〒
 *  标记选项： 〒
 *
 *
 * 端午节大调查§§0
 * §;§radio§;§〒§你喜欢什么口味的粽子?§〒甜味粽〒咸味粽〒辣味粽§¤§
 * §;§radio§;§〒§你吃过什么味的粽子？§〒甜味粽〒咸味粽〒辣味粽
 *
 * step1: 解析问卷title
 * step2: 解析题目
 * step3: 解析类型
 * step4: 解析题title
 * step5: 解析选项
 */
@Service
public class ParseService {


    //存储原始字符串
    private StringBuffer sb;

    public ParseService() {};

    public  void setParseStr(String original) {
        sb = new StringBuffer(original);
    }

    //解析问卷标题
    public String parseQnTitle() {
        String str = this.sb.toString();
        int i = str.indexOf("§§0");
        sb.delete(0, i+3);
        return str.substring(0, i);
    }


    //解析小题
    public String[] parseQnItem() {
        String str = this.sb.toString();
        String[] ss =  str.split("§¤§");
        return ss;
    }


    //解析类型
    public String parseItemType(String item) throws NoSuchAttributeException {
        Pattern p = Pattern.compile("§;§(.*?)§;§");
        Matcher m = p.matcher(item);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }


    //解析题的选项
    public String parseItemTitle(String item) throws NoSuchAttributeException {
        Pattern p = Pattern.compile("〒§(.*?)§〒");
        Matcher m = p.matcher(item);
        while (m.find()) {
            return m.group(1);
        }
        throw  new NoSuchAttributeException();
    }

    //解析题的选项
    public String[] parseItemOption(String item) throws NoSuchAttributeException {
        return item.split("〒");
    }


}
