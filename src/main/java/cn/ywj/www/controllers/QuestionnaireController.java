package cn.ywj.www.controllers;

import cn.ywj.www.entiry.Questionnaire;
import cn.ywj.www.service.AnalyzeService;
import cn.ywj.www.service.ParseAnswerService;
import cn.ywj.www.service.QuestionnaireService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @Autowired
    ParseAnswerService parseAnswerService;

    @Autowired
    AnalyzeService analyzeService;

    @RequestMapping(value = "/addquestionnire", method = RequestMethod.GET)
    public String addQuestionnaire(
            HttpServletRequest request,
            Model model
    ) {
        String email = request.getUserPrincipal().getName(); // email
        model.addAttribute("uid", questionnaireService.pullUserId(email));
        return "addquestionnire";
    }


    @RequestMapping(value = "/addquestionnire", method = RequestMethod.POST)
    public String addQuestionnaire(
            RedirectAttributes att,
            @RequestParam(value = "q-name", required = false) String qName   //新的问卷名
    ) {

        if (!qName.equals("") && qName != null) {
            att.addAttribute("qName", qName);
        }

        return "redirect: /design/questionnaire";
    }


    @RequestMapping(value = "/design/questionnaire", method = RequestMethod.GET)
    public String designQuestionnaire(
            Model model,
            @ModelAttribute("qName") String qName
    ) {
        if (!qName.equals("") && qName != null) {
            model.addAttribute("qName", qName);
        }else {
            model.addAttribute("qName", "标题");
        }
        model.addAttribute("fn", "design");
        return "design";
    }




    /**
     * 根据上传的建问卷的字符串，生成问卷
     * @param model  问卷字符串
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save/questionnaire", method = RequestMethod.POST)
    public String saveQuestionnaire(
            HttpServletRequest request,
            @RequestParam(value = "m", required = false) String model
    ) {
        if (model.equals("") || model == null) return "redirect:/design/questionnaire";
        String email = request.getUserPrincipal().getName(); // 获取email
        questionnaireService.addQuestionnaire(email, model);
        return "/manage";
    }


    @ResponseBody
    @RequestMapping(value = "/update/questionnaire/{qid}", method = RequestMethod.POST)
    public String updateQuestionnaire(
            @RequestParam(value = "m", required = false) String model,
            @PathVariable(value = "qid") String qid
    ) {
        if (model.equals("") || model == null) return "redirect:/manage";
        questionnaireService.updateQuestionnaire(model, qid);
        return "/manage";
    }


    /**
     * 回收站  展示状态为recycle的问卷
     * @param request
     * @return
     */
    @RequestMapping(value = "/recycle", method = RequestMethod.GET)
    public String recycleQuestionnaire(
            HttpServletRequest request,
            Model model
    ) {
        String email = request.getUserPrincipal().getName(); // 获取email
        List<Questionnaire> questionnaireList = questionnaireService.pullQuestionnairesStatusRecycle(email);
        if (  questionnaireList != null || !questionnaireList.isEmpty()) {
            model.addAttribute("questionnaires", questionnaireList);
        }
        model.addAttribute("uid", questionnaireService.pullUserId(email));
        return "recycle";
    }


    @ResponseBody
    @RequestMapping(value = "/erase", method = RequestMethod.DELETE)
    public String eraseQuestionnaire(
            HttpServletRequest request,
            @RequestParam(name = "qs[]", required = false) String[] qids  //问卷id
    ) {
        if (qids == null || qids.length == 0 ) return "recycle";
        String email = request.getUserPrincipal().getName(); // 获取email
        questionnaireService.eraseQuestionnaires(email, qids);
        return "recycle";
    }



    @RequestMapping(value = "/questionnaire/{qid}", method = RequestMethod.GET)
    public String shareQuestionnaire(
            Model model,
            @PathVariable(name = "qid") String qid  //问卷id
    ) {
        //获取问卷信息
        Map<String, Object> map = questionnaireService.pullQuestionnaireAndModelPublishByQid(qid);
        model.addAttribute("data", map);
        model.addAttribute("qid", qid);
        return "questionnaire";
    }


    @ResponseBody
    @RequestMapping(value = "/reply/{qid}", method = RequestMethod.POST, produces = "application/x-www-forrm-ulencoded;charset=utf-8")
    public String replyQuestionnaire(
            Model model,
            @PathVariable(name = "qid") String qid,  //问卷id
            @RequestBody String param
    ) {
        if (qid == null && qid.equals("")) return "提交失败！";
        parseAnswerService.saveAnswer(qid, param);
        return "提交成功！！";
    }


    @RequestMapping(value = "/edit/{qid}", method = RequestMethod.GET)
    public String editQuestionnaire(
            Model model,
            @PathVariable(value = "qid") String qid
    ) {
        Map<String, Object> map = questionnaireService.pullQuestionnaireAndModelExceptRecycle(qid);
        if (map == null) return "redirect:/manage";
        model.addAttribute("qName", ((Questionnaire)map.get("q")).getTitle());
        model.addAttribute("data", map);
        model.addAttribute("fn", "edit");
        return "design";
    }



    @RequestMapping(value = "/analyze/{qid}", method = RequestMethod.GET)
    public String analyze(
            Model model,
            @PathVariable(value = "qid") String qid
    ) {
        Map<String, Object> map = analyzeService.pullAnalyzeData(qid);
        model.addAttribute("data", map);
        return "analyze";
    }





}
