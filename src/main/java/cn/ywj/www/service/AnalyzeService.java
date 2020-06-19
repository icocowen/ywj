package cn.ywj.www.service;

import cn.ywj.www.dao.AnswersCRUD;
import cn.ywj.www.dao.QuestionnaireCRUD;
import cn.ywj.www.dao.QuestionnaireModeCRUD;
import cn.ywj.www.dao.UserCRUD;
import cn.ywj.www.entiry.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * qid   qname
 *
 * qitem title
 *
 * option name option-num
 *
 *
 * text  直接展示表格
 *
 *
 * {
 *     'qid': ''3131231,
 *     'qname': '3123123123131',
 *     questions:[
 *          {
 *              qtitle: '1213123',
 *              type: 'radio',
 *              items: [
 *                  {
 *                      'value': '123',
 *  *                  'name':'甜'
 *                  }
 *              ]
 *          }
 *     ]
 * }
 *
 */
@Service
public class AnalyzeService {
    @Autowired
    private AnswersCRUD answersDao;

    @Autowired
    private QuestionnaireCRUD questionnaireDao;

    @Autowired
    private QuestionnaireModeCRUD questionnaireModeDao;


    /**
     * 根据qid分析问卷答案和数据
     *
     * @param qid  问卷id
     * @return  返回 ↓↓↓↓↓↓
     * {
     *     'qid': '3131231‘,
     *     'qname': '问卷名',
     *       questions:[
     *           {
     *               qtitle: '问题标题',
     *               type: 'radio',
     *               items: [
     *                   {
     *                       'value': '选该选项的人数',
     *                       'name':'选项的描述'  // 或者直接是文本答案
     *                  }
     *               ]
     *           }
     *      ]
     *  }
     */
    public Map<String, Object> pullAnalyzeData(String qid) {
        Map<String, Object> map = analyzeQuestionnaire(qid);

        Answers answers = answersDao.searchAnsByQuestionnaireId(qid);
        QuestionnaireModel qm = questionnaireModeDao.seachQuestionnaireModelByQuestionnaireId(qid);
        List<Object> listQuestions = new ArrayList<>();
        map.put("questions", listQuestions);




        //init  question title    item value
        for (Question q : qm.getQuestions()) {
            Map<String, Object> questionsMap = new HashMap<>();
            listQuestions.add(questionsMap);

            questionsMap.put("qtitle", q.getValue());
            questionsMap.put("type", q.getType().toString());
            List<Object> items = new ArrayList<>();
            questionsMap.put("items", items);

            for (Item it : q.getItems()) {
                Map<String, Object> mm = new HashMap<>();
                items.add(mm);
                mm.put("value", "0");
                mm.put("name", it.getValue());
            }

        }

        //fill data
        for (Answer a : answers.getAnswerList()) {
            List<Set<String>> sets = parseAnswer(a.getValue());
            for (int i = 0; i < listQuestions.size(); i++) {
                Map<String, Object> questionsMap = (Map<String, Object>)listQuestions.get(i);
                Set<String> strings = sets.get(i);
                List<Object> lo = (List<Object>)questionsMap.get("items");

                if (questionsMap.get("type").equals("text")) {
                    for (String s : strings) {
                        if (s.equals("") || s == null) continue;
                        lo.add(s);
                    }
                }else {
                    for (String s : strings) {
                        Map<String, Object> mso = (Map<String, Object>)lo.get(s.toLowerCase().toCharArray()[0] - 'a');
                        mso.put("value", String.valueOf(Integer.valueOf(String.valueOf(mso.get("value"))) + 1));
                    }
                }

            }

        }

      for (Object o :  listQuestions) {
          Map<String, Object> msp = (Map<String, Object>)o;
          if (msp.get("type").equals("text")) {
              continue;
          }
          msp.put("items", JSONArray.toJSONString(msp.get("items")));
      }


        return map;
    }




    private List<Set<String>> parseAnswer(String answer) {
        JSONArray array = JSONArray.parseArray(answer);
        List<Set<String>> lss = new ArrayList<>();
        for (int i = 0; i < array.size(); i++ ) {
            Set<String> s = new HashSet<>();
            JSONArray arr = (JSONArray)array.get(i);
            for (int j = 0; j < arr.size(); j++) {
                s.add(arr.get(j).toString());
            }
            lss.add(s);
        }
        return lss;
    }





    private Map<String, Object> analyzeQuestionnaire(String qid) {
        Questionnaire questionnaire = questionnaireDao.searchQuestionnaireStatusExceptRecycleByQid(qid);
        Map<String , Object> m = new HashMap<>();
        m.put("qid", qid);
        m.put("qname", questionnaire.getTitle());
        return m;
    }








}
