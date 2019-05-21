package com.anubhav11march.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Single extends AppCompatActivity {
    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView singlePOstImage;
    private TextView singlePOstTitel, singlePOstDesc;
    private Button deleteButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Instagram");
        singlePOstDesc = (TextView ) findViewById(R.id.singleDesc);
        singlePOstTitel = (TextView) findViewById(R.id.singleTitle);
        singlePOstImage = (ImageView) findViewById(R.id.singleImageView);
        mAuth = FirebaseAuth.getInstance();
        deleteButton = (Button ) findViewById(R.id.singleDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title  = (String) dataSnapshot.child("title").getValue();
                String post_desc  = (String) dataSnapshot.child("desc").getValue();
                String post_image  = (String) dataSnapshot.child("image").getValue();
                String post_uid  = (String) dataSnapshot.child("uid").getValue();
                singlePOstTitel.setText(post_title);
                singlePOstDesc.setText(post_desc);
                Picasso.get().load(post_image).into(singlePOstImage);
                if(mAuth.getCurrentUser().getUid().equals(post_uid) ){
//                    Toast.makeText(Single.this, "You can delete", Toast.LENGTH_SHORT).show();
                    deleteButton.setVisibility(View.VISIBLE);
                }
//                else{
//                    Toast.makeText(Single.this, "You can't delete", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteButtonClicked(View view){
        mDatabase.child(post_key).removeValue();
        Intent intent = new Intent(Single.this, MainActivity.class);
        startActivity(intent);
    }
}
