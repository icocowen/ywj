package cn.ywj.www.controllers;//package cn.ywj.www.controllers;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//public class Hello {
//    private final Logger logger = LoggerFactory.getLogger(Hello.class);
//
//
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    public String hello(Model model, HttpServletRequest request) {
//        String name = "jiangbei";
//        model.addAttribute("name", request.getUserPrincipal().getName());
//        return "login";
//    }
//
//
//
//}
