package com.example.apollo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.ViewHolder> {

    public List<BlogPost> blog_lists;

    public BlogPostAdapter(List<BlogPost> lc)
    {
        this.blog_lists = lc;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String sc = blog_lists.get(position).getPost_txt();
        holder.postContent.setText(sc);

    }

    @Override
    public int getItemCount() {
        return blog_lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView postContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postContent = (TextView) itemView.findViewById(R.id.post_content);
        }
    }
}
