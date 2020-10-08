package com.adeasy.advertise.config;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Configurations {

    //EMAIL
    public static final String EMAIL_RONICY = "weekendwarmad99@gmail.com";
    public static final String PASSWORD_RONICY = "weekendSliit123";
    public static final String EMAIL_HOST = "smtp.gmail.com";
    public static final String EMAIL_PORT = "587";

    //PAYHERE
    public static final String PAYHERE_MERCHANTID = "1214373";
    public static final String PAYHERE_SECRET = "8bIpNGLREQb4UobgTUm9Dj49Z3dIeUkau4DrwsH4olA0";

    //spring boot server
    public static final String SPRING_SERVER_URL = "https://ronicy.herokuapp.com";
    public static final String SERVER_URL_AUTH_TOKEN = SPRING_SERVER_URL + "/user/get_token?tokenID=";
    public static final String SERVER_URL_PUBLIC_FCM = SPRING_SERVER_URL + "/fcm/save?token=";
    public static final String SERVER_URL_ADMIN_FCM = SPRING_SERVER_URL + "/fcm/save-admin?token=";
    public static final String SERVER_URL_ADMIN_FCM_UID = "&uid=";

    //Algolia Search Engine
    public static final String ALGOLIA_APP_ID = "43D39I7H4Q";
    public static final String ALGOLIA_SEARCH_KEY = "1067f2f27036d929447da00cb71bf4ce";

}
