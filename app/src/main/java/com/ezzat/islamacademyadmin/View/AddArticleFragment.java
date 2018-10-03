package com.ezzat.islamacademyadmin.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ezzat.islamacademyadmin.Model.Article;
import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddArticleFragment extends Fragment {


    private EditText title;
    private EditText desc;
    private EditText content;
    private Button ok;
    private Button cancel;
    private ProgressDialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public AddArticleFragment() {
        // Required empty public constructor
    }
    public static AddArticleFragment newInstance() {
        AddArticleFragment fragment = new AddArticleFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_article, container, false);
        inializeViews(view);
        inializeActions();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        return view;
    }

    private void inializeActions() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addArticle().execute(title.getText().toString(), desc.getText().toString(), content.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddActivity) getActivity()).goBack();
            }
        });
    }

    private void inializeViews(View view) {
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        content = view.findViewById(R.id.cont);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Its loading....");
        dialog.setTitle("Adding Article");
        //dialog.setProgressStyle(ProgressDialog.STYLE_SPINNE);
    }

    private class addArticle extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Post post = new Article(strings[0], strings[1], strings[2]);
            myRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    ((AddActivity) getActivity()).goBack();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        dialog = null;
    }
}
