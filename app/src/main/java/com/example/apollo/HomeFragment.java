package com.example.apollo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView post_view;
    private List<BlogPost> blogList;
    private FirebaseFirestore firestore;
    private BlogPostAdapter blogPostAdapter;
    public HomeFragment() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        post_view = view.findViewById(R.id.post_list_view);
        blogList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        blogPostAdapter = new BlogPostAdapter(blogList);
        post_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_view.setAdapter(blogPostAdapter);
        firestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        BlogPost blogpost = doc.getDocument().toObject(BlogPost.class);
                        blogList.add(blogpost);
                        blogPostAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        return view;
    }
}