package com.coding.techblog.controller;


import com.coding.techblog.modal.Vo.UserVo;

import com.coding.techblog.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;


public abstract class BaseController {

    public static String THEME = "themes/default";



    public String render(String viewName) {
        return THEME + "/" + viewName;
    }





    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }


    public String render_404() {
        return "comm/error_404";
    }

}
