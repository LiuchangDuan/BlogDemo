package com.example.blogdemo.beans;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Article {

    public String title;
    public String publishTime;
    public String author;
    public String post_id;
    public int category;

    public Article() {

    }

    public Article(String pid) {
        post_id = pid;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", author='" + author + '\'' +
                ", post_id='" + post_id + '\'' +
                ", category=" + category +
                '}';
    }

}
