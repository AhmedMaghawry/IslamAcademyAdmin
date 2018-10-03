package com.ezzat.islamacademyadmin.Model;


import com.ezzat.islamacademyadmin.R;

public class Record extends Post {

    public Record(){
        this.res = R.drawable.ic_rec;
    }

    public Record(String title, String desc, String content) {
        this.title = title;
        this.content = content;
        this.desc = desc;
        this.type = Type.Record;
        this.res = R.drawable.ic_rec;
    }
}
