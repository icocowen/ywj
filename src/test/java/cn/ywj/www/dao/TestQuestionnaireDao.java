package cn.ywj.www.dao;

import cn.ywj.www.entiry.Questionnaire;
import cn.ywj.www.util.Status;
import cn.ywj.www.util.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestQuestionnaireDao {
    @Test
    public void shouldAddQuestionnaire() {
        QuestionnaireDao qd = new QuestionnaireDao(new XMLHelper());
        Questionnaire q  = new Questionnaire();
        q.setTitle("怎么看待LOL赛事？");
        q.setStatus(Status.publish);
        q.setStatusChangeTime(String.valueOf(new Date().getTime()));
        q.setQuestionnaireid("83652b77d6b54428858d736a07668794");
        q.setUserId("71ea3af3e1944403ae6c397d69c04a20");
        q.setCreateDate(String.valueOf(new Date().getTime()));
        boolean b = qd.addQuestionnaire(q);
        Assert.assertTrue(b);

    }


    @Test
    public void shouldSearchQuestionnaires() {
        QuestionnaireDao qd = new QuestionnaireDao(new XMLHelper());
        List<Questionnaire> questionnaires = qd.searchQuestionnaireByUserId("71ea3af3e1944403ae6c397d69c04a20");
        Assert.assertNotNull(questionnaires);

    }


    @Test
    public void shouldSearchQuestionnairesByStatus() {
        QuestionnaireDao qd = new QuestionnaireDao(new XMLHelper());
        List<Questionnaire> questionnaires = qd.searchQuestionnaireByUserIdAndQnStatus("71ea3af3e1944403ae6c397d69c04a20", Status.publish);

        assert questionnaires != null;

    }

    @Test
    public void shouldSearchQuestionnairesByTitle() {
        QuestionnaireDao qd = new QuestionnaireDao(new XMLHelper());
        List<Questionnaire> questionnaires = qd.searchQuestionnaireByUserIdAndQnTitle("71ea3af3e1944403ae6c397d69c04a20", "端午节大调查");

        assert questionnaires != null;

    }


    @Test
    public void shouldDeleteQuestionnaire() {
        QuestionnaireDao qd = new QuestionnaireDao(new XMLHelper());

        boolean b = qd.deleteQuestionnaireByUserIdAndQuestionnaireId("71ea3af3e1944403ae6c397d69c04a20", "83652b77d6b54428858d736a07668794");


        Assert.assertTrue(b);

    }



}
