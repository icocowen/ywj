package cn.ywj.www.dao;

import cn.ywj.www.entiry.User;
import cn.ywj.www.exception.UserNoFindException;
import cn.ywj.www.util.XMLHelper;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Repository
public class UserDao implements UserCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

    private final XMLHelper xh;

    @Autowired
    public UserDao(XMLHelper xh) {
        this.xh = xh;
    }

    @Override
    public boolean createUser(User u) {
        Document doc = null;
        try {
            doc = xh.loadUsersDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
            return false;
        }

        assert doc != null;
        Element root = doc.getRootElement();
        Element user = root.addElement("user");
        user.addAttribute("id",  u.getUserId());
        user.addAttribute("type", u.getType());
        user.addElement("email").addText(u.getEmail());
        user.addElement("password").addText(u.getPassword());
        user.addElement("nickname").addText(u.getNickname());
        user.addElement("motto").addText(u.getMotto());
        user.addElement("phone_num").addText(u.getPhoneNum());
        user.addElement("avatar").addText(u.getAvatar());
        user.addElement("register_date").addText(u.getRegisterDate());
        user.addElement("last_login_date").addText(u.getLastLoginDate());

        try {
            xh.writeUsersDoc(doc);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return true;
    }



    @Override
    public User queryUserByEmail(String email)  {
        Document document = null;
        try {
            document = xh.loadUsersDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("u","http://www.ywj.cn/users");

        XPath xPath = document.createXPath("//u:user[u:email='" + email + "']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("用户未找到 {}", email);
                throw new UserNoFindException("用户不存在");
            } catch (UserNoFindException e) {
               return null;
            }
        }

        /**
         * 这些可以优化，可以做成mapper的形式
         */
        User u = new User();
        u.setUserId(XMLHelper.getNodeAttributeText(node, "id"));
        u.setAvatar(XMLHelper.getNodeElementText(node, "avatar"));
        u.setEmail(email);
        u.setLastLoginDate(XMLHelper.getNodeElementText(node, "last_login_date"));
        u.setNickname(XMLHelper.getNodeElementText(node, "nickname"));
        u.setNickname(XMLHelper.getNodeElementText(node, "motto"));
        u.setPassword(XMLHelper.getNodeElementText(node, "password"));
        u.setPhoneNum(XMLHelper.getNodeElementText(node, "phone_num"));
        u.setRegisterDate(XMLHelper.getNodeElementText(node, "register_date"));
        u.setType(XMLHelper.getNodeAttributeText(node, "type"));

        return u;
    }

    @Override
    public User queryUserByUid(String uid) {
        Document document = null;
        try {
            document = xh.loadUsersDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("u","http://www.ywj.cn/users");

        XPath xPath = document.createXPath("//u:user[@id='" + uid + "']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("用户未找到 {}", uid);
                throw new UserNoFindException("用户不存在");
            } catch (UserNoFindException e) {
                return null;
            }
        }

        /**
         * 这些可以优化，可以做成mapper的形式
         */
        User u = new User();
        u.setUserId(uid);
        u.setAvatar(XMLHelper.getNodeElementText(node, "avatar"));
        u.setEmail(XMLHelper.getNodeElementText(node, "email"));
        u.setLastLoginDate(XMLHelper.getNodeElementText(node, "last_login_date"));
        u.setNickname(XMLHelper.getNodeElementText(node, "nickname"));
        u.setMotto(XMLHelper.getNodeElementText(node, "motto"));
        u.setPassword(XMLHelper.getNodeElementText(node, "password"));
        u.setPhoneNum(XMLHelper.getNodeElementText(node, "phone_num"));
        u.setRegisterDate(XMLHelper.getNodeElementText(node, "register_date"));
        u.setType(XMLHelper.getNodeAttributeText(node, "type"));

        return u;
    }


    @Override
    public boolean updateUser(User u) {
        Document document = null;
        try {
            document = xh.loadUsersDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("u","http://www.ywj.cn/users");

        XPath xPath = document.createXPath("//u:user[u:email='" + u.getEmail() + "']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            try {
                LOG.warn("用户未找到 {}", u.getEmail());
                throw new UserNoFindException("用户不存在");
            } catch (UserNoFindException e) {
                return false;
            }
        }
        if (u.getType() != null)
            ((Element)node).attribute("type").setValue(u.getType());
        if (u.getEmail() != null)
            ((Element)node).element("email").setText(u.getEmail());
        if (u.getPassword() != null)
            ((Element)node).element("password").setText(u.getPassword());
        if (u.getNickname() != null)
            ((Element)node).element("nickname").setText(u.getNickname());
        if (u.getMotto() != null)
            ((Element)node).element("motto").setText(u.getMotto());
        if (u.getPhoneNum() != null)
            ((Element)node).element("phone_num").setText(u.getPhoneNum());
        if (u.getAvatar() != null)
            ((Element)node).element("avatar").setText(u.getAvatar());
        if (u.getRegisterDate() != null)
            ((Element)node).element("register_date").setText(u.getRegisterDate());
        if (u.getLastLoginDate() != null)
            ((Element)node).element("last_login_date").setText(u.getLastLoginDate());

        try {
            xh.writeUsersDoc(document);
        } catch (IOException e) {
            LOG.error("文档写入异常 {}",e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean checkUserExistsByEmail(String email) {
        Document document = null;
        try {
            document = xh.loadUsersDoc();
        } catch (DocumentException e) {
            LOG.error("文档异常 {}",e.getMessage());
        }

        assert document != null;
        Map m = new HashMap<String, String>();
        m.put("u","http://www.ywj.cn/users");

        XPath xPath = document.createXPath("//u:user[u:email='" + email + "']");
        xPath.setNamespaceURIs(m);
        Node node = xPath.selectSingleNode(document);
        if (node == null) {
            return false;
        }
        return true;
    }
}
