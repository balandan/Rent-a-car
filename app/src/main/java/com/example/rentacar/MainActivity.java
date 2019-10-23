package com.example.rentacar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.rentacar.Sablon.UserLocalData;
import com.example.rentacar.admin.AdminActivity;
import com.example.rentacar.user.UserMainActivity;
import com.example.rentacar.user.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String username = Paper.book().read(UserLocalData.username);
        String password = Paper.book().read(UserLocalData.password);

        if(username !="" && password !="")
        {
            if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
            {
                AllowAccess(username,password);
            }
        }
    }

    public void AllowAccess(final String username, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(username).exists()) {
                    User usersData = dataSnapshot.child("Users").child(username).getValue(User.class);
                    if (usersData.username.equals(username)) {
                        if (usersData.password.equals(password)) {
                            Toast.makeText(MainActivity.this, "You are in!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            UserLocalData.onlineUser = usersData;
                            Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Password incorect! TRY AGAIN!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account don't exist!!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
