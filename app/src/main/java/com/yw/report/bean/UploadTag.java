package com.yw.report.bean;

import java.util.List;

public class UploadTag {

    /**
     * respCode : 0000
     * respDesc : OK
     * list : [{"uploadId":1,"userId":3,"title":"测试","content":"测试一下","tag":"rainstorm","resources":["http://121.89.205.235:9001/report/20210412134921_wx_camera_1618203202920.jpg"],"comments":null,"commentNum":null,"likeNum":null,"createTime":"2021-04-12 13:49:30","updateTime":"2021-04-12 13:49:30"},{"uploadId":2,"userId":3,"title":"1","content":"同","tag":"blizzard","resources":["http://121.89.205.235:9001/report/resources/20210412135510_wx_camera_1618203202920.jpg"],"comments":null,"commentNum":null,"likeNum":null,"createTime":"2021-04-12 13:55:35","updateTime":"2021-04-12 13:55:35"},{"uploadId":3,"userId":3,"title":"是2文件","content":"欧缇丽","tag":"rainstorm","resources":["http://121.89.205.235:9001/report/resources/20210412135617_wx_camera_1618203202920.jpg","http://121.89.205.235:9001/report/resources/20210412135619_wx_camera_1618120869278.jpg"],"comments":null,"commentNum":null,"likeNum":null,"createTime":"2021-04-12 13:56:25","updateTime":"2021-04-12 13:56:25"}]
     */

    private String respCode;
    private String respDesc;
    private List<ListBean> list;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * uploadId : 1
         * userId : 3
         * title : 测试
         * content : 测试一下
         * tag : rainstorm
         * resources : ["http://121.89.205.235:9001/report/20210412134921_wx_camera_1618203202920.jpg"]
         * comments : null
         * commentNum : null
         * likeNum : null
         * createTime : 2021-04-12 13:49:30
         * updateTime : 2021-04-12 13:49:30
         */

        private int uploadId;
        private int userId;
        private String title;
        private String content;
        private String tag;
        private Object comments;
        private Object commentNum;
        private Object likeNum;
        private String createTime;
        private String updateTime;
        private List<String> resources;

        public int getUploadId() {
            return uploadId;
        }

        public void setUploadId(int uploadId) {
            this.uploadId = uploadId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public Object getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(Object commentNum) {
            this.commentNum = commentNum;
        }

        public Object getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Object likeNum) {
            this.likeNum = likeNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<String> getResources() {
            return resources;
        }

        public void setResources(List<String> resources) {
            this.resources = resources;
        }
    }
}
