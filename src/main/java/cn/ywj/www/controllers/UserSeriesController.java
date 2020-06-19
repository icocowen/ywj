package cn.ywj.www.controllers;

import cn.ywj.www.dao.UserCRUD;
import cn.ywj.www.entiry.User;
import cn.ywj.www.service.RegisterUserService;
import cn.ywj.www.service.UserService;
import cn.ywj.www.util.XMLHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserSeriesController {

    @Autowired
    private RegisterUserService  registerUserService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            Model model,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword
    ){
        if (password.equals(confirmPassword)) {
            User u = new User();
            u.setEmail(email);
            u.setPassword(password);
            if(registerUserService.insertUser(u)){
                model.addAttribute("register","success");
                return "login";
            }
        }
        model.addAttribute("register","fail");
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "redirect:login";
    }


    @RequestMapping(value = "/user/{uid}", method = RequestMethod.GET)
    public String userHome(
            Model model,
            @PathVariable(value = "uid") String uid
    ){
        User user = userService.pullUserByUid(uid);
        model.addAttribute("user", user);
        return "userhome";
    }



    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/x-www-forrm-ulencoded;charset=utf-8" )
    public String updateUserInfo(
            HttpServletRequest request,
        @RequestParam(value = "t") String method,
        @RequestParam(value = "phnum", required = false) String num,
        @RequestParam(value = "motto", required = false) String motto,
        @RequestParam(value = "ol", required = false) String oldpwd,
        @RequestParam(value = "npwd", required = false) String newpwd,
        @RequestParam(value = "confirm", required = false) String confpwd
    ){
        String email = request.getUserPrincipal().getName();
        boolean flag = false;
        User u  = new User();
        if (method.equals("pn")) {
            u.setEmail(email);
            u.setPhoneNum(num);
           flag = userService.updateUserInfo(u);
        }else if (method.equals("mt")) {
            u.setEmail(email);
            u.setMotto(motto);
            flag = userService.updateUserInfo(u);
        }else if (method.equals("ap")){
            if (!newpwd.equals(confpwd)) {
                flag = false;
            }else {
                u.setEmail(email);
                flag = userService.updateUserPwd(u, oldpwd,  newpwd);
            }
        }
        return flag ? "更新成功": "更新失败";
    }


    @ResponseBody
    @RequestMapping(value = "/uploadAvatar" , method = RequestMethod.POST)
    public String uploadAvatar(
            HttpServletRequest request,
            @RequestPart("newAvatar") MultipartFile uploadFile
    ) {
        String email = request.getUserPrincipal().getName();
        return String.valueOf(userService.uploadAvater(uploadFile,email));
    }


    @ResponseBody
    @RequestMapping(value = "/showAvatar", method = RequestMethod.GET)
    public String showAvatar(
            @RequestParam(value = "email") String email
    ) {
        String url = userService.getAvatarURL(email);
        String status = "";
        if (url.equals("") || url == null) {
            status = "fail";
        }else {
            status = "success";
        }

        Map<String, String> m = new HashMap<>();
        m.put("status", status);
        m.put("src", url);
        return JSONObject.toJSONString(m);
    }
}
