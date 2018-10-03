package com.ezzat.islamacademyadmin.View;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ezzat.islamacademyadmin.Model.Article;
import com.ezzat.islamacademyadmin.Model.Lecture;
import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.Model.Record;
import com.ezzat.islamacademyadmin.Model.Type;
import com.ezzat.islamacademyadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public List<Post> posts = new ArrayList();
    FirebaseDatabase database;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });
        getData();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(HomeFragment.newInstance());
        } else if (id == R.id.nav_lec) {
            replaceFragment(LectureFragment.newInstance());
        } else if (id == R.id.nav_art) {
            replaceFragment(ArticleFragment.newInstance());
        } else if (id == R.id.nav_rec) {
            replaceFragment(RecordFragment.newInstance());
        } else if (id == R.id.tube) {
            Uri uri = Uri.parse("https://www.youtube.com/channel/UC6PjoUHuropUbFagibBjF2g?app=desktop");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(likeIng);
        } else if (id == R.id.snap) {
            Uri uri = Uri.parse("https://www.snapchat.com/add/fag-1407");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(likeIng);
        } else if (id == R.id.tele) {
            Uri uri = Uri.parse("https://t.me/fagihalshehri1407");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(likeIng);
        } else if (id == R.id.insta) {
            Uri uri = Uri.parse("https://www.instagram.com/fagih_alshehri/?utm_source=ig_profile_share&igshid=5ejgppsg14i9");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(likeIng);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Post> getArticles() {
        List<Post> articles = new ArrayList<>();
        for (Post post : posts) {
            if (post.getType() == Type.Article)
                articles.add(post);
        }
        return articles;
    }

    public List<Post> getLectures() {
        List<Post> lectures = new ArrayList<>();
        for (Post post : posts) {
            if (post.getType() == Type.Lecture)
                lectures.add(post);
        }
        return lectures;
    }

    public List<Post> getRecords() {
        List<Post> records = new ArrayList<>();
        for (Post post : posts) {
            if (post.getType() == Type.Record)
                records.add(post);
        }
        return records;
    }

    public void getData() {
        DatabaseReference res = database.getReference();
        res.keepSynced(true);
        res.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    switch ((String)d.child("type").getValue()) {
                        case "Lecture" :
                            Lecture lecture = d.getValue(Lecture.class);
                            /*Log.i("dodo", lecture.getTitle());
                            Log.i("dodo", lecture.getDesc());
                            Log.i("dodo", lecture.getContent());*/
                            posts.add(lecture);
                            break;
                        case "Article" :
                            Article article = d.getValue(Article.class);
                            /*Log.i("dodo", article.getTitle());
                            Log.i("dodo", article.getDesc());
                            Log.i("dodo", article.getContent());*/
                            posts.add(article);
                            break;
                        case "Record" :
                            Record record = d.getValue(Record.class);
                            /*Log.i("dodo", record.getTitle());
                            Log.i("dodo", record.getDesc());
                            Log.i("dodo", record.getContent());*/
                            posts.add(record);
                            break;
                    }
                    Collections.reverse(posts);
                    getFragmentManager().beginTransaction().replace(R.id.container, HomeFragment.newInstance()).addToBackStack(null).commitAllowingStateLoss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
