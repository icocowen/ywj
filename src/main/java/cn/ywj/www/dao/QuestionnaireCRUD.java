package cn.ywj.www.dao;

import cn.ywj.www.entiry.Questionnaire;
import cn.ywj.www.util.Status;

import java.util.List;

public interface QuestionnaireCRUD {
    boolean addQuestionnaire(Questionnaire q);
    boolean updateQuestionnaire(Questionnaire q);
    boolean deleteQuestionnaireByUserIdAndQuestionnaireId(String uid, String questionnaireId);
    List<Questionnaire> searchQuestionnaireByUserId(String uid);
    List<Questionnaire> searchQuestionnaireByUserIdAndQnTitle(String uid, String questionnaireTitle);
    List<Questionnaire> searchQuestionnaireByUserIdAndQnStatus(String uid, Status s);
    Questionnaire searchQuestionnaireByQidAndStatus(String qid, String xpath);

    /**
     * 根据qid返回问卷对象
     * @param qid
     * @return
     */
    Questionnaire searchQuestionnaireStatusPublishByQid(String qid);
    Questionnaire searchQuestionnaireStatusExceptRecycleByQid(String qid);
}
