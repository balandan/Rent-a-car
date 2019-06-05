package com.example.rentacar;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentacar.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber,inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        loginButton = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginInApp();
            }
        });
    }
    private void loginInApp()
    {
        String password = inputPassword.getText().toString();
        String phone = inputPhoneNumber.getText().toString();

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please complete password field!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please complete phone field!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login into the account!");
            loadingBar.setMessage("Please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAcces(phone,password);
        }
    }

    private void allowAcces(final String phone, final String password)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
               if (dataSnapshot.child(parentDbName).child(phone).exists())
               {
                   Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                   if(usersData.getPhone().equals(phone))
                   {
                       if (usersData.getPassword().equals(password))
                       {
                           Toast.makeText(LoginActivity.this,"You are in!",Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                       }
                       else
                       {
                           Toast.makeText(LoginActivity.this,"Password incorect! TRY AGAIN!",Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();

                       }
                   }
               }
               else
               {
                   Toast.makeText(LoginActivity.this,"Account don't exist!!",Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
