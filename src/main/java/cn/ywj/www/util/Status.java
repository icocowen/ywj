package cn.ywj.www.util;

public enum Status {
    publish(1, "发布中"),
    pause(2, "暂停"),
    draft(3, "草稿"),
    recycle(4 , "回收");
    public int i;
    public String msg;
    Status(int  i, String msg ){
        this.i = i;
        this.msg = msg;
    }

    public int getI() {
        return i;
    }

    public String getMsg() {
        return msg;
    }
}
