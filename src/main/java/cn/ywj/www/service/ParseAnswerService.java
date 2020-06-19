package cn.ywj.www.service;

import cn.ywj.www.dao.AnswersCRUD;
import cn.ywj.www.entiry.Answer;
import cn.ywj.www.entiry.Answers;
import cn.ywj.www.util.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParseAnswerService {

    @Autowired
    private AnswersCRUD answersDao;

    public boolean saveAnswer(String qid ,String param) {
        Answers ans = new Answers();
        ans.setQuestionnaireid(qid);
        List<Answer> an = new ArrayList<Answer>();
        Answer a = new Answer();
        a.setValue(param);
        a.setSource(Source.link);
        an.add(a);
        ans.setAnswerList(an);
        return answersDao.saveAnswer(ans);
    }
}
