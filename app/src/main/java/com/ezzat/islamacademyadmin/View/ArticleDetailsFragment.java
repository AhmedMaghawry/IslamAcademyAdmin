package com.ezzat.islamacademyadmin.View;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;

public class ArticleDetailsFragment extends Fragment {

    TextView title;
    TextView desc;
    TextView cont;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    public static ArticleDetailsFragment newInstance() {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.des);
        cont = view.findViewById(R.id.content);
        //Post post = ((DetailActivity)getActivity()).getPost();
        Post post = ((DetailActivity) getActivity()).getPost();
        title.setText(post.getTitle());
        desc.setText(post.getDesc());
        cont.setText(post.getContent());
        return view;
    }

}
