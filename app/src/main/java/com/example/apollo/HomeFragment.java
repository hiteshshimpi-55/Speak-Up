package com.example.apollo;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BlogPost> blogList;
    private FirebaseFirestore firestore;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private BlogPostAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor

    }

//    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        blogList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        database  = FirebaseDatabase.getInstance();

        reference = database.getReference("Posts");
//        blogList.clear();
        recyclerView = (RecyclerView)view.findViewById(R.id.post_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter   = new BlogPostAdapter(blogList);
        EventChangeListener();
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void EventChangeListener() {

        Query firstQuery = firestore.collection("Posts").orderBy("TimeStamp",Query.Direction.DESCENDING);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.e("Firestore Error",error.getMessage());
                    return;
                }
                blogList.clear();
                for(DocumentChange doc : value.getDocumentChanges())
                {
                    if(doc.getType()== DocumentChange.Type.ADDED)
                    {
                        String blogPostId = doc.getDocument().getId();
//                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                        QueryDocumentSnapshot post = doc.getDocument();
                        Map<String, Object> s = post.getData();
                        String content = (String) s.get("Post_Content");
                        DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                        Date date = post.getDate("TimeStamp", behavior);
                        String userid = (String) s.get("User");
                        blogList.add(new BlogPost(userid,content,date).withId(blogPostId));
                    }

                }

                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}