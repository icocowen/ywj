package cn.ywj.www.entiry;

import java.util.List;

/**
 * 问卷模板实体，包含3中题型
 * 单选，多选，自己输入的文字题
 */
public class QuestionnaireModel {
    private String questionnaireId;
    private long num;
    private List<Question> questions;

    public String getQuestionnaireid() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
