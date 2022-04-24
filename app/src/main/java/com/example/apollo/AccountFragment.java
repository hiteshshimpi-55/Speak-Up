package com.example.apollo;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;


public class AccountFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserLayout> viewList;
    private FirebaseFirestore firestore;
    private UserLayoutAdapter adapter;
    private FirebaseAuth  firebaseAuth;
    private String sc;
    private ImageView logout_btn;
    public ProgressBar progressBar_del;

    public  AccountFragment () {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        viewList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.account_recycler);

        firestore    = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sc = firebaseAuth.getCurrentUser().getEmail();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);

        progressBar_del = (ProgressBar) view.findViewById(R.id.progressBar2);
        ChangeListener();

        adapter = new UserLayoutAdapter(viewList);
        recyclerView.setAdapter(adapter);


        //LogoutFeature
        logout_btn = (ImageView)view.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void ChangeListener() {
        Query firstQuery = firestore.collection("Posts").orderBy("TimeStamp", Query.Direction.DESCENDING);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }
                viewList.clear();

                for (DocumentChange doc : value.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        QueryDocumentSnapshot snapshot = doc.getDocument();
                        Map<String, Object> mp = snapshot.getData();
                        if (mp.get("User").equals(sc)) {
                            String content = (String) mp.get("Post_Content");
                            DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                            Date date = snapshot.getDate("TimeStamp", behavior);
                            String userid = (String) mp.get("User");
                            String posit  = snapshot.getId();
                            UserLayout ul = new UserLayout(content, date,posit);

                            viewList.add(ul);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent login_Intent = new Intent(getContext(),Login_Activity.class);
        startActivity(login_Intent);
    }
}