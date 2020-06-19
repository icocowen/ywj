package cn.ywj.www.entiry;

import cn.ywj.www.util.Status;

public class Questionnaire {

    private String questionnaireid;
    private String userId;
    private Status status;
    private String title;
    private String createDate;
    private String statusChangeTime;


    public String getQuestionnaireid() {
        return questionnaireid;
    }

    public void setQuestionnaireid(String questionnaireid) {
        this.questionnaireid = questionnaireid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(String statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }
}
