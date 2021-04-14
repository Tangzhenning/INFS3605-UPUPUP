package com.yw.report.bean;

import java.util.List;

public class UploadNoTag {

    /**
     * respCode : 0000
     * respDesc : OK
     * list : [{"uploadId":5,"userId":7,"title":"标题","content":"内容","tag":null,"resources":["fdgdf","fghdfhg"],"comments":[{"commentId":2,"uploadId":5,"comment":"test","createTime":"2021-04-12 15:18:06","updateTime":"2021-04-12 15:18:06"}],"commentNum":1,"likeNum":2,"createTime":"2021-04-12 15:04:10","updateTime":"2021-04-12 15:18:06"}]
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
         * uploadId : 5
         * userId : 7
         * title : 标题
         * content : 内容
         * tag : null
         * resources : ["fdgdf","fghdfhg"]
         * comments : [{"commentId":2,"uploadId":5,"comment":"test","createTime":"2021-04-12 15:18:06","updateTime":"2021-04-12 15:18:06"}]
         * commentNum : 1
         * likeNum : 2
         * createTime : 2021-04-12 15:04:10
         * updateTime : 2021-04-12 15:18:06
         */

        private int uploadId;
        private int userId;
        private String title;
        private String content;
        private Object tag;
        private int commentNum;
        private int likeNum;
        private String createTime;
        private String updateTime;
        private List<String> resources;
        private List<CommentsBean> comments;
        private String nickName;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

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

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
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

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * commentId : 2
             * uploadId : 5
             * comment : test
             * createTime : 2021-04-12 15:18:06
             * updateTime : 2021-04-12 15:18:06
             */

            private int commentId;
            private int uploadId;
            private int userId;
            private String comment;
            private String createTime;
            private String updateTime;
            private String nickName;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getCommentId() {
                return commentId;
            }

            public void setCommentId(int commentId) {
                this.commentId = commentId;
            }

            public int getUploadId() {
                return uploadId;
            }

            public void setUploadId(int uploadId) {
                this.uploadId = uploadId;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
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
        }
    }
}
