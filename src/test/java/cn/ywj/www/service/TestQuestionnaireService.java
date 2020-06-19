package cn.ywj.www.service;

import cn.ywj.www.util.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("spring-core.xml")
public class TestQuestionnaireService {

    @Autowired
    QuestionnaireService questionnaireService;

    @Test
    public void shouldGetDefaultBrief() {
        List<Map<String, String>> maps = questionnaireService.pullQuestionnairesBrief("1558165507@qq.com");
        assert maps != null;
    }


    @Test
    public void shouldGetTitleBrief() {
        List<Map<String, String>> maps =
                questionnaireService.pullQuestionnairesBrief("1558165507@qq.com", "端午节大调查");
        assert maps != null;
    }

    @Test
    public void shouldGetStatusBrief() {
        List<Map<String, String>> maps =
                questionnaireService.pullQuestionnairesBrief("1558165507@qq.com", Status.publish);
        assert maps != null;
    }


    @Test
    public void shouldAddQn() {
        String str = "学习§§0§;§radio§;§〒§你喜欢再怎么样的环境下学习？?§〒甜味粽〒咸味粽〒辣味粽§¤§§;§radio§;§〒§你吃过什么味的粽子？§〒甜味粽〒咸味粽〒辣味粽";


       for (int i = 0; i < 4; i++) {
           questionnaireService.addQuestionnaire("1558165507@qq.com", str);
       }
    }


    @Test
    public void delete() {
        String[] qids = {
                "a324840424524d7b82096f4d6ba7d7f7",
                "46b92d9122574d78b600ffa61261b258",
                "1c8b6bb1f0b54b97b02b0d461db634f9",
                "5e36b3deff94465f996ec9f2d388afff"
        };
        questionnaireService.eraseQuestionnaires("1558165507@qq.com", qids);

    }
}
