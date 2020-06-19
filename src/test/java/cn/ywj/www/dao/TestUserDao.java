package cn.ywj.www.dao;

import cn.ywj.www.entiry.User;
import cn.ywj.www.util.XMLHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;



public class TestUserDao {

    @Test
    public void shouldLoadXml() {
        UserDao userDao = new UserDao(new XMLHelper());
        User user = userDao.queryUserByEmail("1558165507@qq.com");
        Assert.assertNotNull(user);
    }

    @Test
    public void shouldAddUserXml() {
        UserDao userDao = new UserDao(new XMLHelper());
        User u = new User();
        u.setType("2");
        u.setRegisterDate(String.valueOf(new Date().getTime()));
        u.setPhoneNum("12345678453");
        u.setPassword("14e1b600b1fd579f47433b88e8d85291");
        u.setNickname("你好");
        u.setLastLoginDate(String.valueOf(new Date().getTime()));
        u.setEmail("1558166507@qq.com");
        u.setAvatar("/xsd/asd/sad.jpg");
        u.setUserId(UUID.randomUUID().toString().replaceAll("-",""));
        boolean user = userDao.createUser(u);
        Assert.assertTrue(user);
    }


    @Test
    public void shouldUpdateUserXml() {
        UserDao userDao = new UserDao(new XMLHelper());
        User u = new User();
        u.setType("2");
        u.setRegisterDate(String.valueOf(new Date().getTime()));
        u.setPhoneNum("12345678453");
        u.setPassword("14e1b600b1fd579f47433b88e8d85291");
        u.setNickname("ooooooooooo");
        u.setLastLoginDate(String.valueOf(new Date().getTime()));
        u.setEmail("1558166507@qq.com");
        u.setAvatar("/xsd/asd/sad.jpg");
        boolean user = userDao.updateUser(u);
        Assert.assertTrue(user);
    }


    @Test
    public void testFormatTime() {
        System.out.println((new SimpleDateFormat("MM月dd日 HH:mm")).format(new Date("1560047957")));
    }


}
