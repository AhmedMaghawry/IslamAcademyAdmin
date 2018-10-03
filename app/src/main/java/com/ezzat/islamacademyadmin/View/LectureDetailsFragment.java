package com.ezzat.islamacademyadmin.View;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;

public class LectureDetailsFragment extends Fragment {

    TextView title;
    TextView desc;

    public LectureDetailsFragment() {
        // Required empty public constructor
    }

    public static LectureDetailsFragment newInstance() {
        LectureDetailsFragment fragment = new LectureDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture_details, container, false);
        VideoView vidView = (VideoView)view.findViewById(R.id.myVideo);
        Post post = ((DetailActivity)getActivity()).getPost();
        String vidAddress = post.getContent();
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(getActivity());
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.requestFocus();
        vidView.start();
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.des);
        title.setText(post.getTitle());
        desc.setText(post.getDesc());
        return view;
    }

}
