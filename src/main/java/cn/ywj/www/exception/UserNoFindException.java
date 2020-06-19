package cn.ywj.www.exception;

public class UserNoFindException extends Exception {
    public UserNoFindException() {
        super();
    }

    public UserNoFindException(String message) {
        super(message);
    }
}
