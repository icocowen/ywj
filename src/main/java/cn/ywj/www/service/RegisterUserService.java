package cn.ywj.www.service;

import cn.ywj.www.dao.UserCRUD;
import cn.ywj.www.entiry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RegisterUserService {

    @Autowired
    private UserCRUD userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public boolean insertUser(User u) {
        boolean b = userDao.checkUserExistsByEmail(u.getEmail());
        if (!b) {
            User newUser = encryptPassword(u);
            return userDao.createUser(newUser);
        }
        return false;
    }

    public User encryptPassword(User u) {
        String password = u.getPassword();
        String encode = encoder.encode(password);
        User user = initUser();
        user.setPassword(encode);
        user.setEmail(u.getEmail());
        return user;
    }


    //初始化用户数据
    public User initUser(){
        User u = new User();
        u.setUserId(UUID.randomUUID().toString().replaceAll("-",""));
        u.setLastLoginDate(String.valueOf(new Date().getTime()));
        u.setRegisterDate(String.valueOf(new Date().getTime()));
        u.setNickname("新用户");
        u.setPhoneNum("");
        u.setMotto("今日事今日毕");
        u.setType("2");
        u.setAvatar("/images/avatar.jpg");
        return u;
    }

}
