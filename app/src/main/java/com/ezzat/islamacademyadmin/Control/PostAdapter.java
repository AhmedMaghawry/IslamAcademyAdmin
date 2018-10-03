package com.ezzat.islamacademyadmin.Control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;
import com.ezzat.islamacademyadmin.View.DetailActivity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView thumbnail;
        public CardView cardView;

        public PostViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.des);
            thumbnail = view.findViewById(R.id.thumbnail);
            cardView = view.findViewById(R.id.card_view);
        }
    }

    public PostAdapter(Context mContext, List<Post> postList) {
        this.context = mContext;
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final Post post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.desc.setText(post.getDesc());
        holder.thumbnail.setImageResource(post.getRes());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetails(post);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDetails(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void goDetails(Post post) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("post", post);
        context.startActivity(intent);
    }

}
