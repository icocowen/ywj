package cn.ywj.www.controllers;

import cn.ywj.www.service.QuestionnaireService;
import cn.ywj.www.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ManageController {

    @Autowired
    private QuestionnaireService questionnaireService;

    /**
     * 管理页面
     *
     * @param request 获取email, uid
     * @return manage
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String enterManage(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "status", required = false) String status
    ) {
        String email = request.getUserPrincipal().getName(); // email

        if (title != null && !title.equals("")) {
            model.addAttribute("qns", questionnaireService.pullQuestionnairesBrief(email, title));
        }else if (status != null && !status.equals("")){
            model.addAttribute("qns", questionnaireService.pullQuestionnairesBrief(email, Status.valueOf(status)));
        }else {
            model.addAttribute("qns", questionnaireService.pullQuestionnairesBrief(email));
        }
        model.addAttribute("uid", questionnaireService.pullUserId(email));
        return "manage";
    }


    @ResponseBody
    @RequestMapping(value = "/manage", method = RequestMethod.DELETE)
    public String enterManage(
            HttpServletRequest request,
            @RequestParam(name = "d", required = false) String delete  //上传的是qid
    ) {

        String email = request.getUserPrincipal().getName(); // email
        if (delete != null ) {
             questionnaireService.deleteQuestionnaireByqId(email, delete);
        }else{
            return "false";
        }
        return "true";
    }


    @ResponseBody
    @RequestMapping(value = {"/manage", "/recover"}, method = RequestMethod.PUT)
    public void enterManage(
            HttpServletRequest request,
            @RequestParam(name = "ev", required = false) String event,
            @RequestParam(name = "qid", required = false) String qid
    ) {
        if (event == null && event.equals("")) return;
        String email = request.getUserPrincipal().getName(); // email

        if (event.equals("publish")) {
            questionnaireService.publishQuestionnaireByqId(email, qid);
        }else if(event.equals("pause") || event.equals("recover")) {
            questionnaireService.pauseQuestionnaireByqId(email, qid);
        }

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dealDefaultPath() {
        return "redirect:manage";
    }
}
