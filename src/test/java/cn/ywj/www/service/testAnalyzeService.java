package cn.ywj.www.service;

import cn.ywj.www.dao.AnswersCRUD;
import cn.ywj.www.entiry.Answer;
import cn.ywj.www.entiry.Answers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("spring-core.xml")
public class testAnalyzeService {
    @Autowired
    AnswersCRUD answersdao;

    @Autowired
    AnalyzeService analyzeService;

    @Test
    public void shouldPrintAnswer() {
        Answers answers = answersdao.searchAnsByQuestionnaireId("21331");
        List<Set<String>> sets = null;
        for (Answer a: answers.getAnswerList()) {
//            sets = analyzeService.parseAnswer(a.getValue());
        }
        Assert.assertNotNull(sets);
    }


    @Test
    public void shouldInitAnalyze() {
        Map<String, Object> map = analyzeService.pullAnalyzeData("21331");
        Assert.assertNotNull(map);
    }
}
