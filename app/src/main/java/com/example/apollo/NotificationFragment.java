package com.example.apollo;

import static android.content.ContentValues.TAG;
import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

import android.content.Intent;
import android.nfc.Tag;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Most> listview;
    private MostLikeView adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private int LikeCount;

    NotificationFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.most_liked_view);
        listview = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        OnEventChangeListener();

        adapter = new MostLikeView(listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
    public void OnEventChangeListener() {

        firestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }
                listview.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    LikeCount = 0;
                    String blogPostId = doc.getId();
//                      BlogPost blogPost = doc.getDocument().toObject(BlogPost.class)
//                        QueryDocumentSnapshot post = doc.getDocument();
                    Map<String, Object> s = doc.getData();
                    String content = (String) s.get("Post_Content");
                    DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                    Date date = doc.getDate("TimeStamp", behavior);
                    String userid = (String) s.get("User");
                    ArrayList<String> ar = (ArrayList<String>)s.get("Likes");
                    listview.add(new Most(userid, content, date, blogPostId,ar.size()));
                }

                Collections.sort(listview, new Comparator<Most>() {
                    @Override
                    public int compare(Most o1, Most o2) {
                        return o1.getLike_Count()-o2.getLike_Count();
                    }
                });
                Collections.reverse(listview);

                adapter.notifyDataSetChanged();
            }
        });
    }
}