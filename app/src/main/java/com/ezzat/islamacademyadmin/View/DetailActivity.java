package com.ezzat.islamacademyadmin.View;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;

public class DetailActivity extends AppCompatActivity {

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        post = (Post) getIntent().getSerializableExtra("post");
        switch (post.getType()) {
            case Record:
                replaceFragment(RecordDetailsFragment.newInstance());
                break;
            case Article:
                replaceFragment(ArticleDetailsFragment.newInstance());
                break;
            case Lecture:
                replaceFragment(LectureDetailsFragment.newInstance());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container_det, fragment).addToBackStack(null).commit();
    }

    public Post getPost() {
        return post;
    }
}
