package com.coding.techblog.controller;


import com.coding.techblog.modal.Vo.UserVo;
import com.coding.techblog.utils.MapCache;
import com.coding.techblog.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;


public abstract class BaseController {

    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();


    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }



    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }

}
