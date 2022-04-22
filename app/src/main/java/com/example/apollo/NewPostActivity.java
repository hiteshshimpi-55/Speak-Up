package com.example.apollo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    private EditText post_txt;
    private ImageView post_btn;
    private ProgressBar new_post_progress;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String currUser;
    private Uri post_txtUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        post_txt = (EditText) findViewById(R.id.post_txt);
        post_btn = (ImageView) findViewById(R.id.post_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        currUser = firebaseAuth.getCurrentUser().getEmail();
        new_post_progress = (ProgressBar) findViewById(R.id.new_post_progress);
        storageReference = FirebaseStorage.getInstance().getReference().child("Post_Contents");
        firestore        = FirebaseFirestore.getInstance();


        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = post_txt.getText().toString();

                if(!TextUtils.isEmpty(text))
                {
                    new_post_progress.setVisibility(View.VISIBLE);
                    String randomName = FieldValue.serverTimestamp().toString();

                            Map<String,Object> postMap = new HashMap<>();
                            postMap.put("Post_Content",text);
                            postMap.put("User",currUser);
                            postMap.put("TimeStamp",FieldValue.serverTimestamp());

                            firestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    new_post_progress.setVisibility(View.INVISIBLE);
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(NewPostActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent mainPage = new Intent(NewPostActivity.this,MainActivity.class);
                                        startActivity(mainPage);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(NewPostActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                else
                {
                      Toast.makeText(NewPostActivity.this,"Error in posting",Toast.LENGTH_SHORT).show();
                }
//                firestore.collection("Posts").document().update("TimeStamp",FieldValue.serverTimestamp());
            }
        });
    }

}