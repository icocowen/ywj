package cn.ywj.www.dao;

import cn.ywj.www.entiry.Answer;
import cn.ywj.www.entiry.Answers;
import cn.ywj.www.util.Source;
import cn.ywj.www.util.XMLHelper;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnswersDao implements AnswersCRUD {
    private static final Logger LOG = LoggerFactory.getLogger(AnswersDao.class);

    private final XMLHelper xh;

    @Autowired
    public AnswersDao(XMLHelper xh) {
        this.xh = xh;
    }



    @Override
    public boolean addAnswer(Answers ans) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element answers = document.addElement("answers","http://www.ywj.cn/answer");
        answers.addAttribute("questionnaire_id", ans.getQuestionnaireid());
        answers.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        answers.addAttribute("xsi:schemaLocation", "http://www.ywj.cn/answer answer.xsd");
        answers.addAttribute("answer_num", ans.getAnswerNum());
        if (ans.getAnswerList() != null){
            for(Answer an : ans.getAnswerList()) {
                answers.addElement("answer").addAttribute("source", an.getSource().toString()).setText(an.getValue());
            }
        }

        try {
            XMLHelper.createAnswersDoc(ans.getQuestionnaireid(), document);
        } catch (IOException e) {
            LOG.error("创建答卷失败 {}",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveAnswer(Answers ans) {

        Document document = null;
        try {
            document = xh.loadAnswersDoc(ans.getQuestionnaireid());
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }
        assert document != null;
        Element answers = document.getRootElement();
        long num = Long.valueOf(answers.attributeValue("answer_num"));
        answers.attribute("answer_num").setValue(String.valueOf(num + 1));
        if (ans.getAnswerList() != null){
            for(Answer an : ans.getAnswerList()) {
                answers.addElement("answer").addAttribute("source", an.getSource().toString()).setText(an.getValue());
            }
        }

        try {
            xh.writeAnswersDoc(ans.getQuestionnaireid(), document);
        } catch (IOException e) {
            LOG.error("答案写入错误{}", ans.getQuestionnaireid());
            return false;
        }

        return true;
    }

    @Override
    public Answers searchAnsByQuestionnaireId(String questionnaireId) {

        Document document = null;
        try {
            document = xh.loadAnswersDoc(questionnaireId);
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Element root = document.getRootElement();
        Answers ans = new Answers();
        ans.setQuestionnaireid(questionnaireId);
        ans.setAnswerNum(root.attributeValue("answer_num"));
        List<Answer> answerList = new ArrayList<>();
        ans.setAnswerList(answerList);
        for (Object o : root.elements()) {
            Element e = (Element)o;
            Answer an = new Answer();
            an.setSource(Source.valueOf(e.attributeValue("source")));
            an.setValue(e.getText());
            answerList.add(an);
        }

        return ans;
    }

    @Override
    public boolean deleteAnsByQuestionnaireId(String questionnaireId) {
        try {
            XMLHelper.deleteAnswerFile(questionnaireId);
        } catch (IOException e) {
            LOG.error("答卷文件删除失败 {}",e.getMessage());
            return false;
        }
        return true;
    }
}
