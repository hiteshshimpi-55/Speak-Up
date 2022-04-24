package com.example.apollo;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.SupportErrorDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.ViewHolder> {

    private List<BlogPost> blog_lists;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private Context context;

    public BlogPostAdapter(List<BlogPost> lc)
    {
        this.blog_lists = lc;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        context   = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.setIsRecyclable(false);
        String blogPostId = blog_lists.get(position).blogPostId;
        String sc = blog_lists.get(position).getPost_txt();
        holder.postContent.setText(sc);
        String currentUserId = mAuth.getCurrentUser().getUid();
        long millisecond = blog_lists.get(position).getTimestamp().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy");
        String dateString = simpleDateFormat.format(millisecond);
        holder.date.setText(dateString);

//        holder.date.setRotation(90);

        //Like Count
        firestore.collection("Posts/"+blogPostId+"/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty())
                {
                    int count = value.size();

                    holder.updateLikes(count);
                }
                else {
                    holder.updateLikes(0);
                }
            }
        });

                //Get Likes
                firestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.exists()) {
                            holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_liked));
                        } else {
                            holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_likebutton));
                        }
                    }
                });
        //Like Feature
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firestore.collection("Posts/"+blogPostId+"/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists())
                        {

                            Map<String,Object> mp = new HashMap<>();
                            mp.put("TimeStamp", FieldValue.serverTimestamp());
                            firestore.collection("Posts/"+blogPostId+"/Likes")
                                    .document(currentUserId).set(mp);
                        }
                        else
                        {
                            firestore.collection("Posts/"+blogPostId+"/Likes")
                                    .document(currentUserId).delete();
                        }
                    }
                });

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
