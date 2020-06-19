package cn.ywj.www.service;

import cn.ywj.www.dao.AnswersCRUD;
import cn.ywj.www.dao.QuestionnaireCRUD;
import cn.ywj.www.dao.QuestionnaireModeCRUD;
import cn.ywj.www.dao.UserCRUD;
import cn.ywj.www.entiry.*;
import cn.ywj.www.util.QuestionType;
import cn.ywj.www.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.NoSuchAttributeException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class QuestionnaireService {

    @Autowired
    private QuestionnaireCRUD questionnaireDao;

    @Autowired
    private AnswersCRUD answersDao;

    @Autowired
    private UserCRUD userDao;

    @Autowired
    private QuestionnaireModeCRUD questionnaireModeDao;

    @Autowired
    private ParseService parseService;


    /**
     * brief 包括 问卷状态，问卷名，问卷id，答卷数量，问卷创建日期
     * @return
     */
    public List<Map<String, String>> pullQuestionnairesBrief(String email) {
        return  encapsBrief( pullQuestionnaires(pullUserId(email)));

    }

    /**
     * 封装 为brief信息
     * @param questionnaires
     * @return
     */
    public  List<Map<String , String>>  encapsBrief(List<Questionnaire> questionnaires) {
        List<Map<String , String>> questionnairesBrief = new ArrayList<>();
        for (Questionnaire qn : questionnaires) {
            if (qn.getStatus() == Status.recycle) continue;

            Map<String , String> questionnaireBrief = new HashMap<>();
            questionnaireBrief.put("createDate",
                    (new SimpleDateFormat("MM月dd日 HH:mm"))
                            .format(new Date(Long.valueOf(qn.getCreateDate()))) );
            questionnaireBrief.put("status", questionnaireStatus(qn.getStatus()));
            questionnaireBrief.put("qnTitle", qn.getTitle());
            questionnaireBrief.put("qnId", qn.getQuestionnaireid());
            questionnaireBrief.put("ansNum", pullAnswerNum(qn.getQuestionnaireid()));
            questionnairesBrief.add(questionnaireBrief);
        }
        return questionnairesBrief;
    }


    /**
     * 通过title搜索
     *
     * @param email
     * @param t
     * @return
     */
    public List<Map<String, String>> pullQuestionnairesBrief(String email, String t) {
        return encapsBrief( pullQuestionnairesByTitle(pullUserId(email), t));
    }

    /**
     * 通过status搜索
     *
     * @param email
     * @param s
     * @return
     */
    public List<Map<String, String>> pullQuestionnairesBrief(String email, Status s) {
        return encapsBrief( pullQuestionnairesByStatus(pullUserId(email), s));
    }

    public String questionnaireStatus( Status code) {
        String status = "草稿";
        switch (code){
            case  publish:
                status = "发布中";
                break;
            case pause:
                status = "暂停";
                break;
            case draft:
                status = "草稿";
                break;
            case recycle:
                status = "回收";
                break;
        }
        return status;
    }


    public String pullAnswerNum(String questionnaireId) {
        return answersDao.searchAnsByQuestionnaireId(questionnaireId).getAnswerNum();
    }

    public String pullUserId(String email) {
        return userDao.queryUserByEmail(email).getUserId();
    }

    public List<Questionnaire> pullQuestionnaires(String uid) {
        return questionnaireDao.searchQuestionnaireByUserId(uid);
    }

    public List<Questionnaire> pullQuestionnairesByTitle(String uid, String title) {
        return questionnaireDao.searchQuestionnaireByUserIdAndQnTitle(uid, title);
    }

    public List<Questionnaire> pullQuestionnairesByStatus(String uid, Status s) {
        return questionnaireDao.searchQuestionnaireByUserIdAndQnStatus(uid, s);
    }


    public boolean deleteQuestionnaireByqId(String email, String qid) {
        return updateQuestionnaire(email, qid, Status.recycle);
    }


    public boolean publishQuestionnaireByqId(String email, String qid) {
        return updateQuestionnaire(email, qid, Status.publish);
    }

    public boolean pauseQuestionnaireByqId(String email, String qid) {
        return updateQuestionnaire(email, qid, Status.pause);
    }


    public boolean updateQuestionnaire(String email, String qid, Status s) {
        try {
            String uid = pullUserId(email);
            Questionnaire q = new Questionnaire();
            q.setQuestionnaireid(qid);
            q.setUserId(uid);
            q.setStatus(s);
            q.setStatusChangeTime(String.valueOf(new Date().getTime())); //记录变化的时间
            questionnaireDao.updateQuestionnaire(q);
        }catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean addQuestionnaire(String email, String model)  {
        String uid = pullUserId(email);
        String qid = "";
        synchronized (this){
            qid = UUID.randomUUID().toString().replaceAll("-","");
        }

        parseService.setParseStr(model);
        synchronized (this) {
            addNewQuestionnaire(uid, qid);
        }


        QuestionnaireModel qm = new QuestionnaireModel();

        qm.setQuestionnaireId(qid);
        List<Question> lq = new ArrayList<>();
        for (String item : parseService.parseQnItem()) {
            Question q = new Question();
            String type = null;

            String s = "";
            String itemTitle = "";
            try {

                type = parseService.parseItemType(item);
                q.setType(QuestionType.valueOf(type));
                s = item.substring(type.length() + 6);
                itemTitle = parseService.parseItemTitle(s);
                q.setValue(itemTitle);

                int i = 0;
                List<Item> li = new ArrayList<>();
                for (String o : parseService.parseItemOption(s.substring(itemTitle.length() + 4))){
                    if (o.equals("")) continue;

                    Item it = new Item();
                    it.setOption(String.valueOf((char)('A'+i)));
                    it.setValue(o);
                    li.add(it);
                    i++;
                }
                q.setItems(li);

            } catch (NoSuchAttributeException e) {
                e.printStackTrace(); //这里是日志
                return false;
            }

            lq.add(q);
        }
        qm.setNum(lq.size());
        qm.setQuestions(lq);
        synchronized (this){
            initAnswerFile(qid);

            questionnaireModeDao.addQuestionnaireMode(qm);
        }

        return true;
    }



    public boolean updateQuestionnaire(String model, String qid)  {
//        String uid = pullUserId(email); //这里应该根据uid和qid更新问卷

        parseService.setParseStr(model);
        parseService.parseQnTitle();

        QuestionnaireModel qm = new QuestionnaireModel();

        qm.setQuestionnaireId(qid);
        List<Question> lq = new ArrayList<>();
        for (String item : parseService.parseQnItem()) {
            Question q = new Question();
            String type = null;

            String s = "";
            String itemTitle = "";
            try {

                type = parseService.parseItemType(item);
                q.setType(QuestionType.valueOf(type));
                s = item.substring(type.length() + 6);
                itemTitle = parseService.parseItemTitle(s);
                q.setValue(itemTitle);

                int i = 0;
                List<Item> li = new ArrayList<>();
                for (String o : parseService.parseItemOption(s.substring(itemTitle.length() + 4))){
                    if (o.equals("")) continue;

                    Item it = new Item();
                    it.setOption(String.valueOf((char)('A'+i)));
                    it.setValue(o);
                    li.add(it);
                    i++;
                }
                q.setItems(li);

            } catch (NoSuchAttributeException e) {
                e.printStackTrace(); //这里是日志
                return false;
            }

            lq.add(q);
        }
        qm.setNum(lq.size());
        qm.setQuestions(lq);
        synchronized (this){
            questionnaireModeDao.updateQuestionnaireMode(qm);
        }

        return true;
    }



    private void addNewQuestionnaire(String uid, String qid) {
        Questionnaire q = new Questionnaire();
        String timeStr = String.valueOf(new Date().getTime());
        q.setCreateDate(timeStr);
        q.setStatusChangeTime(timeStr);
        q.setStatus(Status.draft);
        q.setUserId(uid);
        q.setQuestionnaireid(qid);
        q.setTitle(parseService.parseQnTitle());
        questionnaireDao.addQuestionnaire(q);
    }


    //初始化 anser文件
    private void initAnswerFile(String qid) {
        Answers ans = new Answers();
        ans.setQuestionnaireid(qid);
        ans.setAnswerNum("0");
        answersDao.addAnswer(ans);
    }


    public List<Questionnaire> pullQuestionnairesStatusRecycle(String email) {
        String uid  = pullUserId(email);
        return pullQuestionnairesByStatus(uid, Status.recycle);
    }


    public boolean eraseQuestionnaires(String email, String[] qids) {
        String uid  = pullUserId(email);
        for (String qid : qids) {
            questionnaireDao.deleteQuestionnaireByUserIdAndQuestionnaireId(uid, qid);
            try {
                questionnaireModeDao.deleteQuestionnaireModeByQuestionnaireId(qid);

            }finally {
                answersDao.deleteAnsByQuestionnaireId(qid);
            }
        }
        return true;
    }


    /**
     * 只获取 publish状态的
     * @param qid
     * @return
     */
    public Map<String, Object> pullQuestionnaireAndModelPublishByQid(String qid) {
        Questionnaire questionnaire = questionnaireDao.searchQuestionnaireStatusPublishByQid(qid);
        Map<String, Object> map = pullQuestionnaireAndModel(qid);
        map.put("q", questionnaire);
        return map;
    }

    /**
     * 获取除了 recycle状态的
     */
    public Map<String, Object> pullQuestionnaireAndModelExceptRecycle(String qid) {
        Questionnaire questionnaire = questionnaireDao.searchQuestionnaireStatusExceptRecycleByQid(qid);
        if (questionnaire == null) return null;

        Map<String, Object> map = pullQuestionnaireAndModel(qid);
        map.put("q", questionnaire);
        return map;
    }


    public Map<String, Object> pullQuestionnaireAndModel(String qid) {
        QuestionnaireModel questionnaireModel = questionnaireModeDao.seachQuestionnaireModelByQuestionnaireId(qid);
        Map<String, Object> map = new HashMap<>(2);
        map.put("qm", questionnaireModel);
        return map;
    }



}
