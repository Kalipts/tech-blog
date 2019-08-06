package com.coding.techblog.controller.admin;


import com.coding.techblog.constant.WebConst;
import com.coding.techblog.controller.BaseController;
import com.coding.techblog.exception.TipException;
import com.coding.techblog.modal.Bo.RestResponseBo;
import com.coding.techblog.modal.Bo.StatisticsBo;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.ContentVo;
import com.coding.techblog.modal.Vo.UserVo;
import com.coding.techblog.service.ISiteService;
import com.coding.techblog.service.IUserService;
import com.coding.techblog.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller("adminIndexController")
@RequestMapping("/super")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private ISiteService siteService;


    @Resource
    private IUserService userService;

    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        LOGGER.info("Enter admin-assets index method");
        List<CommentVo> comments = siteService.recentComments(5);
        List<ContentVo> contents = siteService.recentContents(5);
        StatisticsBo statistics = siteService.getStatistics();



        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);

        LOGGER.info("Exit admin-assets index method");
        return "admin/index";
    }


    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }


    @GetMapping(value = "logout")
    public String logout() {
        System.out.println("index-----------logout");
        return "admin/login";
    }



    @PostMapping(value = "/profile")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {

        UserVo users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UserVo temp = new UserVo();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);

            UserVo original = (UserVo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "/password")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        UserVo users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return RestResponseBo.fail("Mật khẩu củ hoặc mật khẩu mới trống ");
        }

        if (!users.getPassword().equals(TaleUtils.MD5encode(users.getUsername() + oldPassword))) {
            return RestResponseBo.fail("Mật khẩu cũ không chính xác ");
        }
        if (password.length() < 6 || password.length() > 14) {
            return RestResponseBo.fail("Mật khẩu từ 6 đến 14 kí tự ");
        }

        try {
            UserVo temp = new UserVo();
            temp.setUid(users.getUid());
            String pwd = TaleUtils.MD5encode(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateByUid(temp);


            UserVo original= (UserVo)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
            return RestResponseBo.ok();
        } catch (Exception e){
            String msg = "Đổi mật khẩu không thành công ";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
    }
}
