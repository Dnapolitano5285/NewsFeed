package com.mediocremidgardian.newsfeed;

/**
 * Created by Valhalla on 9/10/16.
 */
public class Article {

    private String title;
    private String author;
    private String webUrl;
    private String date;

    public Article(String title, String author, String webUrl, String date) {
        this.title = title;
        this.author = author;
        this.webUrl = webUrl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor(){
        return author;
    }

}
