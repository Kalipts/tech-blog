package com.coding.techblog.utils;


import com.coding.techblog.constant.WebConst;
import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.dto.Types;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.ContentVo;
import com.coding.techblog.service.ISiteService;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public final class Commons {

    private static ISiteService siteService;

    public static String THEME = "themes/default";

    private static final List EMPTY = new ArrayList(0);

    public static void setSiteService(ISiteService ss) {
        siteService = ss;
    }


    public static boolean is_empty(PageInfo paginator) {
        return paginator == null || (paginator.getList() == null) || (paginator.getList().size() == 0);
    }

    public static String site_url() {
        return site_url("");
    }

    public static String site_index() {
        return "index";
    }


    public static String site_login() {
        return "admin/login";
    }


    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    public static String site_title() {
        return site_option("site_title");
    }


    public static String site_option(String key) {
        return site_option(key, "");
    }


    public static String site_option(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        String str = WebConst.initConfig.get(key);
        if (StringUtils.isNotBlank(str)) {
            return str;
        } else {
            return defalutValue;
        }
    }


    public static String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }


    public static String theme_url() {
        return site_url(Commons.THEME);
    }


    public static String theme_url(String sub) {
        return site_url(Commons.THEME + sub);
    }


    public static String gravatar(String email) {
        String avatarUrl = "https://secure.gravatar.com/avatar";
        if (StringUtils.isBlank(email)) {
            return avatarUrl;
        }
        String hash = TaleUtils.MD5encode(email.trim().toLowerCase());
        return avatarUrl + "/" + hash;
    }


    public static String permalink(ContentVo contents) {
        return permalink(contents.getCid(), contents.getSlug());
    }



    public static String random(int max, String str) {
        return new Random().nextInt(max-1+1) + 1 + str;
    }


    public static String permalink(Integer cid, String slug) {
        return site_url("/article/" + (StringUtils.isNotBlank(slug) ? slug : cid.toString()));
    }


    public static String fmtdate(Integer unixTime) {
        return fmtdate(unixTime, "yyyy-MM-dd");
    }


    public static String fmtdate(Integer unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return DateKit.formatDateByUnixTime(unixTime, patten);
        }
        return "";
    }


    public static String show_categories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            String[] arr = categories.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/category/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return show_categories("Phân loại mặt định");
    }


    public static String show_tags(String tags) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(tags)) {
            String[] arr = tags.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/tag/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return "";
    }


    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return TaleUtils.htmlToText(TaleUtils.mdToHtml(html));
        } else {
            String text = TaleUtils.htmlToText(TaleUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }


    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return TaleUtils.mdToHtml(value);
        }
        return "";
    }


    public static String show_thumb(ContentVo contents) {
        int cid = contents.getCid();
        int size = cid % 20;
        size = size == 0 ? 1 : size;
        return "/user/img/rand/" + size + ".jpg";
    }

    public static List<ContentVo> recent_articles(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        return siteService.recentContents(limit);
    }


    public static List<CommentVo> recent_comments(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        return siteService.recentComments(limit);
    }


    public static List<MetaDto> categries(int limit) {
        return siteService.metas(Types.CATEGORY.getType(), null, limit);
    }

    public static List<MetaDto> categries() {
        return categries(WebConst.MAX_POSTS);
    }


    public static List<MetaDto> tags(int limit) {
        return siteService.metas(Types.TAG.getType(), null, limit);
    }


    public static List<MetaDto> tags() {
        return tags(WebConst.MAX_POSTS);
    }


    public static String comment_at(Integer coid) {
        CommentVo comments = siteService.getComment(coid);
        if (null != comments) {
            return "<a href=\"#comment-" + coid + "\">@" + comments.getAuthor() + "</a>";
        }
        return "";
    }

    private static final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};


    public static String show_icon(int cid) {
        return ICONS[cid % ICONS.length];
    }


    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }


    public static String show_thumb(String content) {
        content = TaleUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(content);
            if (m_image.find()) {
                img = img + "," + m_image.group();

                Matcher m = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)").matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }



}
