package com.islamijindegi.islamijindegi;

/**
 * Created by Shahed on 2/22/2017.
 */

public class PostsModel {

    private int id;
    private String post_id;
    private String post_title;
    private String post_author;
    private String post_type;
    private String category;
    private String ulr;
    private String post_meta_ext_file;
    private String post_meta_upload_file;
    private String post_meta_video_link;
    private String post_download_file_url;
    private String post_last_update;
    private String post_content;

    public String getPost_new() {
        return post_new;
    }

    public void setPost_new(String post_new) {
        this.post_new = post_new;
    }

    private String post_new;


    public int getId() {
        return id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getPost_meta_ext_file() {
        return post_meta_ext_file;
    }

    public void setPost_meta_ext_file(String post_meta_ext_file) {
        this.post_meta_ext_file = post_meta_ext_file;
    }

    public String getPost_meta_upload_file() {
        return post_meta_upload_file;
    }

    public void setPost_meta_upload_file(String post_meta_upload_file) {
        this.post_meta_upload_file = post_meta_upload_file;
    }

    public String getPost_meta_video_link() {
        return post_meta_video_link;
    }

    public void setPost_meta_video_link(String post_meta_video_link) {
        this.post_meta_video_link = post_meta_video_link;
    }

    public String getPost_download_file_url() {
        return post_download_file_url;
    }

    public void setPost_download_file_url(String post_download_file_url) {
        this.post_download_file_url = post_download_file_url;
    }

    public String getPost_last_update() {
        return post_last_update;
    }

    public void setPost_last_update(String post_last_update) {
        this.post_last_update = post_last_update;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUlr() {
        return ulr;
    }

    public void setUlr(String ulr) {
        this.ulr = ulr;
    }
//    "posts": [{
//        "post_id": 5837,
//                "post_title": "কালিমায়ে তায়্যিবাহ",
//                "post_type": "books",
//                "category": "০১. আকাইদ, ০৭. মুখোশ উন্মোচন, প্রবন্ধ, ",
//                "url": "http://ij.futuremoveit.com/books/%e0%a6%95%e0%a6%be%e0%a6%b2%e0%a6%bf%e0%a6%ae%e0%a6%be%e0%a7%9f%e0%a7%87-%e0%a6%a4%e0%a6%be%e0%a7%9f%e0%a7%8d%e0%a6%af%e0%a6%bf%e0%a6%ac%e0%a6%be%e0%a6%b9/"
//    }]
}
