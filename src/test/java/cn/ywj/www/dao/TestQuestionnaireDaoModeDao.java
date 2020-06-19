package cn.ywj.www.dao;

import cn.ywj.www.entiry.Item;
import cn.ywj.www.entiry.Question;
import cn.ywj.www.entiry.QuestionnaireModel;
import cn.ywj.www.util.QuestionType;
import cn.ywj.www.util.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class TestQuestionnaireDaoModeDao {

    @Test
    public void shouldAddQuestionnire() {
        QuestionnaireModeDao qmd = new QuestionnaireModeDao(new XMLHelper());
        QuestionnaireModel qm = new QuestionnaireModel();
        qm.setNum(2);
        qm.setQuestionnaireId(UUID.randomUUID().toString().replaceAll("-",""));
        Question q = new Question();
        q.setType(QuestionType.radio);
        q.setValue("你喜欢什么口味的番薯？");
        Item i = new Item();
        i.setOption("A");
        i.setValue("甜为");

        Item i2 = new Item();
        i2.setOption("B");
        i2.setValue("枯萎");
        ArrayList<Item> ii = new ArrayList<>();
        ii.add(i);
        ii.add(i2);
        ArrayList<Question> qs = new ArrayList<Question>();
        qs.add(q);
        q.setItems(ii);

        qm.setQuestions(qs);


        Question q1 = new Question();
        q1.setType(QuestionType.radio);
        q1.setValue("你喜欢什么口味的番薯？");
        Item i3 = new Item();
        i3.setOption("A");
        i3.setValue("甜为");

        Item i4 = new Item();
        i4.setOption("B");
        i4.setValue("枯萎");
        ArrayList<Item> iii = new ArrayList<>();
        iii.add(i3);
        iii.add(i4);
        ArrayList<Question> qs1 = new ArrayList<Question>();
        qs1.add(q1);
        q1.setItems(iii);

        qm.setQuestions(qs1);



        qmd.addQuestionnaireMode(qm);

        Assert.assertNotNull(qm);

    }


    @Test
    public void shouldRemoveQuestionnire() {
        QuestionnaireModeDao qmd = new QuestionnaireModeDao(new XMLHelper());
        boolean b = qmd.deleteQuestionnaireModeByQuestionnaireId("83652b77d6b54428858d736a07668794");
        Assert.assertTrue(b);
    }


    @Test
    public void shouldSearchQuestionnire() {
        QuestionnaireModeDao qmd = new QuestionnaireModeDao(new XMLHelper());
        QuestionnaireModel b = qmd.seachQuestionnaireModelByQuestionnaireId("21331");

        Assert.assertNotNull(b);
    }


    @Test
    public void shouldSearchQuestionnireOne() {
        QuestionnaireModeDao qmd = new QuestionnaireModeDao(new XMLHelper());
        QuestionnaireModel b = qmd.seachQuestionnaireModelByQuestionnaireId("21331");
        b.getQuestions().get(0).setValue("dsadasdasdad");
        b.getQuestions().get(0).getItems().get(0).setValue("22222222222");
        qmd.updateQuestionnaireMode(b);

        Assert.assertNotNull(b);
    }
}

