package com.example.apollo;

import static android.content.ContentValues.TAG;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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

        recyclerView = (RecyclerView)view.findViewById(R.id.post_list_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        EventChangeListener();
        adapter = new BlogPostAdapter(blogList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void EventChangeListener() {

        Query firstQuery = firestore.collection("Posts").orderBy("TimeStamp",Query.Direction.DESCENDING);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.e("Firestore Error",error.getMessage());
                    return;
                }
                blogList.clear();
                for(DocumentSnapshot doc : value.getDocuments())
                {

                        BlogPost blogPost = new BlogPost();
                        Map<String, Object> s = doc.getData();
                        int[] likeCnt = {0};
                        blogPost.setPost_txt((String) s.get("Post_Content"));
                        DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                        blogPost.setTimestamp(doc.getDate("TimeStamp", behavior));
                        blogPost.setUser_id((String) s.get("User"));
                        blogList.add(blogPost.withId(doc.getId()));

                }
                adapter.notifyDataSetChanged();
            }

        });
    }


}