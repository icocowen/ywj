package cn.ywj.www.dao;

import cn.ywj.www.entiry.QuestionnaireModel;

public interface QuestionnaireModeCRUD {
    boolean addQuestionnaireMode(QuestionnaireModel questionnaireModel);
    boolean updateQuestionnaireMode(QuestionnaireModel qm);
    boolean deleteQuestionnaireModeByQuestionnaireId(String questionnaireId);
    QuestionnaireModel seachQuestionnaireModelByQuestionnaireId(String questionnaireId);
}
