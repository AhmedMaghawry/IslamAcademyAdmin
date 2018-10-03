package com.ezzat.islamacademyadmin.Model;

import com.ezzat.islamacademyadmin.R;

public class Article extends Post {

    public Article(){
        this.res = R.drawable.ic_art;
    }

    public Article(String title, String desc, String content) {
        this.title = title;
        this.content = content;
        this.desc = desc;
        this.type = Type.Article;
        this.res = R.drawable.ic_art;
    }
}
