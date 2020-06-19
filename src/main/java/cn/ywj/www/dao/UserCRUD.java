package cn.ywj.www.dao;

import cn.ywj.www.entiry.User;

public interface UserCRUD {
    boolean createUser(User u);
    User queryUserByEmail(String email);
    User queryUserByUid(String uid);
    boolean updateUser(User u);
    boolean checkUserExistsByEmail(String email);
}
