package com.ezzat.islamacademyadmin.Model;

import com.ezzat.islamacademyadmin.R;

public class Lecture extends Post {

    public Lecture(){
        this.res = R.drawable.ic_vid;
    }

    public Lecture(String title, String desc, String content) {
        this.title = title;
        this.content = content;
        this.desc = desc;
        this.type = Type.Lecture;
        this.res = R.drawable.ic_vid;
    }
}
