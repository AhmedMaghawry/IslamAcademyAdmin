package com.ezzat.islamacademyadmin.View;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.islamacademyadmin.Model.Article;
import com.ezzat.islamacademyadmin.Model.Lecture;
import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLectureFragment extends Fragment {

    private EditText title;
    private EditText desc;
    private Button upload;
    private Button ok;
    private Button cancel;
    private TextView nice;
    private TextView bad;
    private ProgressDialog dialog;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public AddLectureFragment() {
        // Required empty public constructor
    }

    public static AddLectureFragment newInstance() {
        AddLectureFragment fragment = new AddLectureFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lecture, container, false);
        inializeViews(view);
        inializeActions();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        return view;
    }

    private void inializeActions() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new AddLectureFragment().addArticle().execute(title.getText().toString(), desc.getText().toString(), content.getText().toString());
                if (nice.getVisibility() == View.VISIBLE) {
                    ((AddActivity) getActivity()).goBack();
                } else {
                    showAlert();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddActivity) getActivity()).goBack();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void inializeViews(View view) {
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        upload = view.findViewById(R.id.upload);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Its loading....");
        dialog.setTitle("Adding Lecture");
        nice = view.findViewById(R.id.done);
        bad = view.findViewById(R.id.fail);
        //dialog.setProgressStyle(ProgressDialog.STYLE_SPINNE);
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("dodo", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(getActivity(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("dodo", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    new addLecture(uri).execute(path, uri.getEncodedPath(), title.getText().toString(), desc.getText().toString());
                }
                break;
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private class addLecture extends AsyncTask<String, String, String> {
        Uri ur;

        public addLecture(Uri uri) {
            ur = uri;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(final String... strings) {
            Uri file = ur;
            StorageReference riversRef = mStorageRef.child(strings[1]);

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Post post = new Lecture(strings[2], strings[3], downloadUrl.toString());
                            myRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    nice.setVisibility(View.VISIBLE);
                                    bad.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            bad.setVisibility(View.VISIBLE);
                            nice.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    });
            return null;
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Upload Faild we cant upload it")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        dialog = null;
    }
}
