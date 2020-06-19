package cn.ywj.www.entiry;

import java.util.List;

public class Answers {
    private String questionnaireid;
    private String answerNum;
    private List<Answer> answerList;

    public String getQuestionnaireid() {
        return questionnaireid;
    }

    public void setQuestionnaireid(String questionnaireid) {
        this.questionnaireid = questionnaireid;
    }

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
