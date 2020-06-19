package cn.ywj.www.entiry;

import cn.ywj.www.util.QuestionType;

import java.util.List;

public class Question {


    private QuestionType type;
    private String value;
    private boolean use = true;
    private int length;
    private List<Item> items;
    public Question(){};

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Question(QuestionType type, String value, boolean use, int length, List<Item> items) {
        this.items = items;
        this.type = type;
        this.value = value;
        this.use = use;
        this.length = length;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}


