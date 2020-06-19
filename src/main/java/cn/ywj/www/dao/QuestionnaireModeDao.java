package cn.ywj.www.dao;

import cn.ywj.www.entiry.Item;
import cn.ywj.www.entiry.Question;
import cn.ywj.www.entiry.QuestionnaireModel;
import cn.ywj.www.exception.UserNoFindException;
import cn.ywj.www.util.QuestionType;
import cn.ywj.www.util.XMLHelper;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class QuestionnaireModeDao implements QuestionnaireModeCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireModeDao.class);

    private final XMLHelper xh;

    @Autowired
    public QuestionnaireModeDao(XMLHelper xh) {
        this.xh = xh;
    }

    @Override
    public boolean addQuestionnaireMode(QuestionnaireModel questionnaireModel) {
        Document document = null;
        try {
            document = xh.loadQuestionnaireModelsDoc();
        } catch (DocumentException e) {
            LOG.error("文档模型异常 {}",e.getMessage());
            return false;
        }

        Element root = document.getRootElement();
        Element questionnaire_model = root.addElement("questionnaire_model");
        questionnaire_model.addAttribute("questionnaire_id", questionnaireModel.getQuestionnaireid());
        questionnaire_model.addElement("question_num").addText(String.valueOf(questionnaireModel.getNum()));
        Element questions = questionnaire_model.addElement("questions");

        for(Question q :  questionnaireModel.getQuestions() ) {
            Element question = questions.addElement("question");
            question.addAttribute("type",q.getType().toString());
            question.addAttribute("value", q.getValue());
            if (!q.isUse())
                question.addAttribute("use", String.valueOf(q.isUse()));
            if (q.getLength() != 0)
                 question.addAttribute("length", String.valueOf(q.getLength()));

            if (q.getItems() == null) continue; //当是text控件时，不循环item

            for(Item i : q.getItems()) {
                question.addElement("item").addAttribute("option", i.getOption()).addText(i.getValue());
            }
        }

        try {
            xh.writeQuestionnaireModelsDoc(document);
        } catch (IOException e) {
            LOG.error("文档模型写入异常 {}",e.getMessage());
            return false;
        }


        return false;
    }

    @Override
    public boolean updateQuestionnaireMode(QuestionnaireModel qm) {
        Document document = null;
        try {
            document = xh.loadQuestionnaireModelsDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qm","http://www.ywj.cn/questionnaire-model");

        XPath xPath = document.createXPath("//qm:questionnaire_model[@questionnaire_id='"+qm.getQuestionnaireid()+"']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("文档模型未找到 {}", qm.getQuestionnaireid());
                throw new UserNoFindException("文档模型未找到");
            } catch (UserNoFindException e) {
                return false;
            }
        }


        Element questions = ((Element)node).element("questions");
        List question = questions.elements("question");
        List<Question> q = qm.getQuestions();


        for (int j = 0; j < question.size(); j++) {
            Element qe = (Element)question.get(j);
           qe.attribute("value").setValue(q.get(j).getValue());


            List elements = qe.elements();
            List<Item> items = q.get(j).getItems();
            for (int i = 0; i < elements.size(); i++) {
                Element el = (Element)elements.get(i);
                el.setText(items.get(i).getValue());
            }
        }




        try {
            xh.writeQuestionnaireModelsDoc(document);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteQuestionnaireModeByQuestionnaireId(String questionnaireId) {
        Document document = null;
        try {
            document = xh.loadQuestionnaireModelsDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return false;
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qm","http://www.ywj.cn/questionnaire-model");

        XPath xPath = document.createXPath("//qm:questionnaire_model[@questionnaire_id='"+questionnaireId+"']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);


        boolean flag = node.getParent().remove(node);

        try {
            xh.writeQuestionnaireModelsDoc(document);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return flag;
    }

    @Override
    public QuestionnaireModel seachQuestionnaireModelByQuestionnaireId(String questionnaireId) {
        Document document = null;
        try {
            document = xh.loadQuestionnaireModelsDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return null;
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qm","http://www.ywj.cn/questionnaire-model");

        XPath xPath = document.createXPath("//qm:questionnaire_model[@questionnaire_id='"+questionnaireId+"']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);

        if (node == null) {
            try {
                LOG.warn("问卷模型未找到 {}", questionnaireId);
                throw new UserNoFindException("问卷模型不存在");
            } catch (UserNoFindException e) {
                return null;
            }
        }

        QuestionnaireModel qm = new QuestionnaireModel();
        ArrayList<Question> qlist = new ArrayList<>();
        qm.setNum(Long.valueOf(XMLHelper.getNodeElementText(node, "question_num")));
        qm.setQuestions(qlist);
        qm.setQuestionnaireId(questionnaireId);
        List questions = ((Element) node).element("questions").elements();
        for (Object o : questions) {
            Element e = (Element)o;
            Question q = new Question();
            q.setType(QuestionType.valueOf(e.attribute("type").getValue()));
            q.setValue(e.attributeValue("value"));
            if (e.attributeValue("length") != null)
                q.setLength(Integer.valueOf(e.attributeValue("length")));
            q.setUse(Boolean.valueOf(e.attributeValue("use")));
            ArrayList<Item> itemList = new ArrayList<>();
            for (Object oj : e.elements()) {
                Element el = (Element)oj;
                Item i = new Item();
                i.setOption(el.attributeValue("option"));
                i.setValue(el.getText());
                itemList.add(i);
            }
            q.setItems(itemList);
            qlist.add(q);
        }


        return qm;

    }
}
