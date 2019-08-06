package com.coding.techblog.controller;

import com.coding.techblog.constant.WebConst;
import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.dto.Types;
import com.coding.techblog.exception.TipException;
import com.coding.techblog.modal.Bo.CommentBo;
import com.coding.techblog.modal.Bo.RestResponseBo;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.ContentVo;
import com.coding.techblog.modal.Vo.MetaVo;
import com.coding.techblog.service.ICommentService;
import com.coding.techblog.service.IContentService;
import com.coding.techblog.service.IMetaService;
import com.coding.techblog.utils.Commons;
import com.coding.techblog.utils.PatternKit;
import com.coding.techblog.utils.TaleUtils;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;


@Controller
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private IContentService contentService;

    @Resource
    private ICommentService commentService;

    @Resource
    private IMetaService metaService;




    @GetMapping(value = {"/", "index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(request, 1, limit);
    }




    @GetMapping(value = "page/{p}")
    public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        PageInfo<ContentVo> articles = contentService.getContents(p, limit);

        request.setAttribute("articles", articles);
        if (p > 1) {

        }

        request.setAttribute("post_title", "Nha's Blog");

        return this.render("index");
    }




    @GetMapping(value = {"article/{cid}", "article/{cid}.html"})
    public String getArticle(HttpServletRequest request, @PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);

        if (null == contents || "draft".equals(contents.getStatus())) {
            return this.render_404();
        }

        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        request.setAttribute("post_title", contents.getTitle());
        request.setAttribute("post_keywords",contents.getContent());
        request.setAttribute("post_description",contents.getContent());

        completeArticle(request, contents);
        System.out.println("xin chao" + Commons.site_option("df adfa dsf asdf asdf", " "));
        return this.render("post");
    }


    @GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
    public String articlePreview(HttpServletRequest request, @PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        completeArticle(request, contents);
        return "themes/default/post";
    }


    private void completeArticle(HttpServletRequest request, ContentVo contents) {
                List<CommentVo> comments = commentService.getAllComment(contents.getCid());
                request.setAttribute("comments", comments);
    }


    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }


    @PostMapping(value = "/comment")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam Integer cid, @RequestParam Integer coid,
                                  @RequestParam String author, @RequestParam String mail,
                                  @RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {

        if (null == cid || StringUtils.isBlank(text)) {
            return RestResponseBo.fail("Vui lòng nhập bình luận đầy đủ");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return RestResponseBo.fail("Tên quá dài");
        }

        if (StringUtils.isNotBlank(mail) && TaleUtils.isEmail(mail)) {
            return RestResponseBo.fail("Vui lòng nhập đúng định dạng email");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return RestResponseBo.fail("Vui lòng nhập đúng định dạng url");
        }

        if (text.length() > 200) {
            return RestResponseBo.fail("Vui lòng nhập bình luận trong 200 kí tự ");
        }



        CommentVo comments = new CommentVo();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentService.insertComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "Bình luận thất bại ";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
    }



    @GetMapping(value = "category/{keyword}")
    public String categories(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.categories(request, keyword, 1, limit);
    }

    @GetMapping(value = "category/{keyword}/{page}")
    public String categories(HttpServletRequest request, @PathVariable String keyword,
                             @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        MetaDto metaDto = metaService.getMeta(Types.CATEGORY.getType(), keyword);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);

        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "Phân loại ");
        request.setAttribute("keyword", keyword);

        return this.render("page-category");
    }







    @GetMapping(value = "/{pagename}")
    public String page(@PathVariable String pagename, HttpServletRequest request) {
        ContentVo contents = contentService.getContents(pagename);
        if (null == contents) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        return this.render("page");
    }



    @GetMapping(value = "search/{keyword}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.search(request, keyword, 1, limit);
    }

    @GetMapping(value = "search/{keyword}/{page}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        PageInfo<ContentVo> articles = contentService.getArticles(keyword, page, limit);
        request.setAttribute("articles", articles);
        request.setAttribute("type", "Tìm kiếm");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
    }




    @GetMapping(value = "tag/{name}")
    public String tags(HttpServletRequest request, @PathVariable String name, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.tags(request, name, 1, limit);
    }


    @GetMapping(value = "tag/{name}/{page}")
    public String tags(HttpServletRequest request, @PathVariable String name, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

        name = name.replaceAll("\\+", " ");
        MetaDto metaDto = metaService.getMeta(Types.TAG.getType(), name);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "Tag");
        request.setAttribute("keyword", name);

        return this.render("page-category");
    }


    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

}
