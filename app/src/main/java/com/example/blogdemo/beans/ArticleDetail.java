package com.example.blogdemo.beans;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ArticleDetail {

    // 文章Id
    public String postId;

    // 文章内容
    public String content;

    public ArticleDetail() {

    }

    public ArticleDetail(String pid, String html) {
        this.postId = pid;
        this.content = html;
    }

}
