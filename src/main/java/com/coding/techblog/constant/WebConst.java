package com.coding.techblog.constant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
public class WebConst {

    public static final String INSTALL_FILE_CONF = "install.lock";

    public static Map<String, String> initConfig = new HashMap<>();


    public static String LOGIN_SESSION_KEY = "login_user";

    public static final String USER_IN_COOKIE = "S_L_ID";


    public static String AES_SALT = "0123456789abcdef";

    public static final int MAX_POSTS = 9999;

    public static final int MAX_PAGE = 100;


    public static final int MAX_TEXT_COUNT = 200000;


    public static final int MAX_TITLE_COUNT = 200;


    public static final int HIT_EXCEED = 10;

    public static Integer MAX_FILE_SIZE = 1048576;



    public static final Set<String> BLOCK_IPS = new HashSet<>(16);
}
