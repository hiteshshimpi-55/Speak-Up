package com.example.apollo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MostLikeView extends RecyclerView.Adapter<MostLikeView.ViewHolder> {
    private List<Most> blog_lists;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private Context context;

    public MostLikeView(List<Most> blog_lists) {
        this.blog_lists = blog_lists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_view,parent,false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        context   = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String blogPostId = blog_lists.get(position).blogPostId;
        String sc = blog_lists.get(position).getPost_content();
        holder.postContent.setText(sc);
        String currentUserId = mAuth.getCurrentUser().getUid();
        long millisecond = blog_lists.get(position).getTimeStamp().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy");
        String dateString = simpleDateFormat.format(millisecond);
        holder.date.setText(dateString);
        holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_liked));

        firestore.collection("Posts").document(blogPostId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                ArrayList<String> ar = (ArrayList<String>) value.get("Likes");
                if (!ar.isEmpty()) {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_liked));
                } else {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_likebutton));
                }
            }
        });
        firestore.collection("Posts").document(blogPostId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<String> ar = (ArrayList<String>) value.get("Likes");
                if(!ar.isEmpty())
                {
                    int count = ar.size();
                    holder.updateLikes(count);
                }
                else {
                    holder.updateLikes(0);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return blog_lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView postContent,date,likeCnt;
        private ImageView likeBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = (TextView) itemView.findViewById(R.id.post_content);
            date        = (TextView) itemView.findViewById(R.id.date);
            likeBtn     = (ImageView) itemView.findViewById(R.id.like_btn);
            likeCnt     = (TextView) itemView.findViewById(R.id.likes_cnt);
        }
        public void updateLikes(int count)
        {
            likeCnt.setText(count+" Likes");
        }
    }
}
