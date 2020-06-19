package cn.ywj.www.dao;

import cn.ywj.www.entiry.Answers;

/**
 * 每份问卷一张答案表
 */
public interface AnswersCRUD {
    boolean addAnswer(Answers ans);
    boolean saveAnswer(Answers ans);
    Answers searchAnsByQuestionnaireId(String questionnaireId);
    boolean deleteAnsByQuestionnaireId(String questionnaireId);
}
