package com.yw.report.network;

public class Url {
    public static String base="http://121.89.205.235:9001/report";
    public static String SIGNUP=base+"/noLogin/register";
    public static String LOGIN=base+"/noLogin/login";
    public static String USER_INFO=base+"/user/getUserInfo";
    public static String UPDATE_USER_INFO=base+"/user/modifyUserInfo";
    public static String FILE_UPLOAD=base+"/file/uploadFile";
    public static String PUBLISH_NO_TAG=base+"/content/publish";//发帖子no tag
    public static String QUARY_PUBLISH=base+"/content/query";//查询所有帖子
    public static String PUBLISH_COMMENT=base+"/content/comment";//发表评论
    public static String PRAISE_PUBLISH=base+"/content/like";//帖子点赞
    public static String PUBLISH_WITH_TAG=base+"/tag/content/publish";//发帖子tag
    public static String QUARY_WITH_TAG_PUBLISH=base+"/tag/content/query";//查询所有帖子

}
