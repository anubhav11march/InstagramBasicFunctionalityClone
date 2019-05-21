package com.anubhav11march.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mInstaList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInstaList = (RecyclerView) findViewById(R.id.instaList);
        mInstaList.setHasFixedSize(true);
        mInstaList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("Instagram");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, Register.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter <Insta, InstaViewHiiolder> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHiiolder>(
                Insta.class,
                R.layout.insta_row,
                InstaViewHiiolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(InstaViewHiiolder instaViewHiiolder, Insta insta, int i) {
                final String postKey = getRef(i).getKey().toString();
                instaViewHiiolder.setTitle(insta.getTitle());
                instaViewHiiolder.setDesc(insta.getDesc());
                instaViewHiiolder.setImage(getApplicationContext(), insta.getImage());
                instaViewHiiolder.setUsername(insta.getUsername());

                instaViewHiiolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newIntent = new Intent(MainActivity.this, Single.class);
                        newIntent.putExtra("PostId", postKey);
                        startActivity(newIntent);
                    }
                });
            }
        };
        mInstaList.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class InstaViewHiiolder extends RecyclerView.ViewHolder{

        View mView;
        public InstaViewHiiolder(View itemView){
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);

        }
        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.postImage);
            Picasso.get().load(image).into(post_image);
        }

        public void setUsername (String username){
            TextView post_username = (TextView) mView.findViewById(R.id.textUsername);
            post_username.setText(username);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.addIcon){
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
