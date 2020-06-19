package cn.ywj.www.dao;

import cn.ywj.www.entiry.Questionnaire;
import cn.ywj.www.exception.UserNoFindException;
import cn.ywj.www.util.Status;
import cn.ywj.www.util.XMLHelper;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;


@Repository
public class QuestionnaireDao implements QuestionnaireCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireDao.class);

    private final XMLHelper xh;

    @Autowired
    public QuestionnaireDao(XMLHelper xh) {
        this.xh = xh;
    }


    @Override
    public boolean addQuestionnaire(cn.ywj.www.entiry.Questionnaire q) {

        Document doc = null;
        try {
            doc = xh.loadQuestionnairesDoc();
        } catch (DocumentException e) {
            LOG.error("问卷文件异常 {}",e.getMessage());
            return false;
        }

        Element rootElement = doc.getRootElement();
        Element questionnaire = rootElement.addElement("questionnaire").addAttribute("question_id", q.getQuestionnaireid())
                .addAttribute("owner_id", q.getUserId()).addAttribute("status", q.getStatus().toString());
        questionnaire.addElement("title").setText(q.getTitle());
        questionnaire.addElement("create_date").setText(q.getCreateDate());
        questionnaire.addElement("status_change_time").setText(q.getStatusChangeTime());

        try {
            xh.writeQuestionnairesDoc(doc);
        } catch (IOException e) {
            LOG.error("问卷写入异常 {}",e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean updateQuestionnaire(Questionnaire q) {
        Document document = null;
        try {
            document = xh.loadQuestionnairesDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qs","http://www.ywj.cn/questionnaires");

        XPath xPath = document.createXPath("//qs:questionnaire[@question_id='"+q.getQuestionnaireid()+"' and @owner_id='"+q.getUserId()+"' ]");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("问卷未找到 id: {}", q.getQuestionnaireid());
                throw new UserNoFindException("问卷不存在");
            } catch (UserNoFindException e) {
                return false;
            }
        }
        if (q.getStatus()!= null)
            ((Element)node).attribute("status").setValue(q.getStatus().toString());
        if (q.getTitle() != null)
            ((Element)node).element("title").setText(q.getTitle());
        if (q.getStatusChangeTime() != null)
            ((Element)node).element("status_change_time").setText(q.getStatusChangeTime());


        try {
            xh.writeQuestionnairesDoc(document);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteQuestionnaireByUserIdAndQuestionnaireId(String uid, String questionnaireId) {
        Document document = null;
        try {
            document = xh.loadQuestionnairesDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return false;
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qs","http://www.ywj.cn/questionnaires");

//        XPath xPath = document.createXPath("//qs:questionnaire[@question_id='"+questionnaireId+"' and @owner_id='"+uid+"' and @status='recycle']");
        XPath xPath = document.createXPath("//qs:questionnaire[@question_id='"+questionnaireId+"']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);

        boolean flag = node.getParent().remove(node);

        try {
            xh.writeQuestionnairesDoc(document);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return flag;
    }

    @Override
    public List<cn.ywj.www.entiry.Questionnaire> searchQuestionnaireByUserId(String uid) {
        return  searchQuestionnaires(uid, "//qs:questionnaire[@owner_id='"+uid+"' and @status!='recycle']");
    }
//
    @Override
    public List<Questionnaire> searchQuestionnaireByUserIdAndQnTitle(String uid, String questionnaireTitle) {
        return searchQuestionnaires(uid, "//qs:questionnaire[@owner_id='"+uid+"' and contains(qs:title,'"+questionnaireTitle+"') and @status!='recycle']" );
    }

    @Override
    public List<Questionnaire> searchQuestionnaireByUserIdAndQnStatus(String uid, Status s) {
        return searchQuestionnaires(uid, "//qs:questionnaire[@owner_id='"+uid+"' and @status='"+s.toString()+"']");
    }


    @Override
    public Questionnaire searchQuestionnaireByQidAndStatus(String qid, String xpath) {
        Document document = null;
        try {
            document = xh.loadQuestionnairesDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return null;
        }
        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qs","http://www.ywj.cn/questionnaires");
        XPath xPath = document.createXPath(xpath);
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("未找到用户相关的问卷 {}", qid);
                throw new UserNoFindException("未找到用户相关的问卷");
            } catch (UserNoFindException e) {
                return null;
            }
        }
        Element e = (Element)node;
        String ol = e.element("status_change_time").getText();
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setCreateDate(e.element("create_date").getText());
        questionnaire.setQuestionnaireid(qid);
        questionnaire.setStatusChangeTime(ol);
        questionnaire.setTitle(e.element("title").getText());
        return questionnaire;
    }


    @Override
    public Questionnaire searchQuestionnaireStatusPublishByQid(String qid) {
        return searchQuestionnaireByQidAndStatus(qid, "//qs:questionnaire[@question_id='"+qid+"' and @status ='publish']");
    }

    @Override
    public Questionnaire searchQuestionnaireStatusExceptRecycleByQid(String qid) {
        return searchQuestionnaireByQidAndStatus(qid, "//qs:questionnaire[@question_id='"+qid+"' and @status !='recycle']");
    }


    public List<Questionnaire> searchQuestionnaires(String uid, String xpath) {
        Document document = null;
        try {
            document = xh.loadQuestionnairesDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return null;
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("qs","http://www.ywj.cn/questionnaires");

        XPath xPath = document.createXPath(xpath);
        xPath.setNamespaceURIs(m);
        List node = xPath.selectNodes(document);

        if (node == null) {
            try {
                LOG.warn("未找到用户相关的问卷 {}", uid);
                throw new UserNoFindException("未找到用户相关的问卷");
            } catch (UserNoFindException e) {
                return null;
            }
        }

        List<cn.ywj.www.entiry.Questionnaire> lq = new ArrayList<>();
        for (Object o : node) {
            Element e = (Element)o;
            String status = e.attributeValue("status");
            String ol = e.element("status_change_time").getText();
            String questionId = e.attributeValue("question_id");

            boolean flag = false;
            if (status.equals("recycle")) {
                flag = judgeDate(ol);
                if (flag)
                    deleteQuestionnaireByUserIdAndQuestionnaireId(uid, questionId);
            }


            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUserId(uid);
            questionnaire.setCreateDate(e.element("create_date").getText());
            questionnaire.setQuestionnaireid(questionId);
            questionnaire.setStatus(Status.valueOf(status));
            questionnaire.setStatusChangeTime(ol);
            questionnaire.setTitle(e.element("title").getText());
            lq.add(questionnaire);
        }

        return lq;
    }


    /**
     * 判断是否过期  60天
     * @param ol  删除时的日期 60天
     * @return
     */
    public boolean judgeDate(String ol) {
        Long nowTime = new Date().getTime();
        long timeDiff = (nowTime - Long.valueOf(ol))/(60 * 60 * 24);
        if (timeDiff > 60) return true;
        return false;
    }



}
