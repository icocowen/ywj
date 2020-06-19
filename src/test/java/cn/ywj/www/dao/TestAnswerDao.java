package cn.ywj.www.dao;

import cn.ywj.www.entiry.Answers;
import cn.ywj.www.util.XMLHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("spring-core.xml")
public class TestAnswerDao {

    @Autowired
    AnswersCRUD ad;

    @Test
    public void shouldCreateAnswerNewFile() {
         ad = new AnswersDao(new XMLHelper());

        Answers answers = ad.searchAnsByQuestionnaireId("21331");

        answers.setQuestionnaireid("71ea3af3e1944403ae6c397d69c04a20");
        ad.addAnswer(answers);

        Assert.assertNotNull(answers);

    }


    @Test
    public void shouldDeleteAnswerNewFile() {
        AnswersDao ad = new AnswersDao(new XMLHelper());

        boolean answers = ad.deleteAnsByQuestionnaireId("71ea3af3e1944403ae6c397d69c04a20");


        Assert.assertTrue(answers);

    }
}
