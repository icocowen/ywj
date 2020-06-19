package cn.ywj.www.service;

import cn.ywj.www.dao.UserCRUD;
import cn.ywj.www.entiry.MyUserDetails;
import cn.ywj.www.entiry.User;
import cn.ywj.www.util.XMLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserCRUD userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * username 就是email
     *
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.queryUserByEmail(s);
        if (user == null) throw new UsernameNotFoundException("数据库中无此用户！");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role = user.getType().equals("1") ? "ROLE_VIP" : "ROLE_NOT_VIP";
        authorities.add(new SimpleGrantedAuthority(role));
        return new MyUserDetails(user.getEmail(), user.getPassword(), authorities);
    }


    public User pullUserByUid(String uid) {
        return userDao.queryUserByUid(uid);
    }



    public boolean updateUserInfo(User u) {
        if (u.getPhoneNum() != null && !XMLHelper.isPhone(u.getPhoneNum()))  return false;
        return userDao.updateUser(u);
    }


    public boolean updateUserPwd(User u, String oldpwd, String newPwd) {
        User user = userDao.queryUserByEmail(u.getEmail());
        if (!encoder.matches(oldpwd, user.getPassword())) return false;
        u.setPassword(encoder.encode(newPwd));
        return userDao.updateUser(u);
    }



    public boolean uploadAvater(MultipartFile file, String email) {
        String fileName = file.getOriginalFilename();
        if (!checkFileName(fileName)) {   // jpg,  png
            return false;
        }
        int point = fileName.indexOf(".");
        String suffix = fileName.substring(point);

        String avatarName = email.hashCode() + suffix;

        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File dir = new File(path + "/user");
        if (!dir.exists()) dir.mkdir();

        File avatar = new File(dir.getAbsolutePath() + "/"+ avatarName);
        if (avatar.exists())  avatar.delete(); //删除原文件
        try {

            file.transferTo(avatar);

            User u = new User();
            u.setEmail(email);
            u.setAvatar("/img/"+avatarName);
            updateUserInfo(u);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean checkFileName(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) return true;
        return false;
    }

    public String getAvatarURL(String email) {
        User user = userDao.queryUserByEmail(email);
        return user.getAvatar();
    }
}
