package com.example.apollo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.apollo.AccountFragment;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class UserLayoutAdapter extends RecyclerView.Adapter<UserLayoutAdapter.ViewHolder>{

    public List<UserLayout> viewList;
    public UserLayoutAdapter(List<UserLayout> viewList) {
        this.viewList = viewList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String sc =  viewList.get(position).getPost_content();
        long millisecond = viewList.get(position).getTimeStamp().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy");
        String dateString = simpleDateFormat.format(millisecond);
        holder.posts_txt.setText(sc);
        holder.date.setText(dateString);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Posts").
                        document(viewList.get(position).getPosition()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), "Cannot Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return viewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView posts_txt;
        TextView date;
        ImageView options;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posts_txt = (TextView)itemView.findViewById(R.id.posts_user);
            date      = (TextView)itemView.findViewById(R.id.date_user);
            options   = (ImageView)itemView.findViewById(R.id.button_options);

        }
    }
}
