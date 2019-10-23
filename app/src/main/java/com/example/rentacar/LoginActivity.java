package com.example.rentacar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentacar.Sablon.UserLocalData;
import com.example.rentacar.admin.AdminActivity;
import com.example.rentacar.admin.AdminCarsActivity;
import com.example.rentacar.user.UserMainActivity;
import com.example.rentacar.user.VerifyCode;
import com.example.rentacar.user.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername ,inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView adminLink, notAdminLink;
    private CheckBox chkBox;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.login_username_input);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        loginButton = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
        adminLink = (TextView) findViewById(R.id.admin_textView_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_textView_link);
        chkBox = (CheckBox) findViewById(R.id.remember_me_checkBtn);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginInApp();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }
    private void loginInApp()
    {
        String password = inputPassword.getText().toString();
        String username = inputUsername.getText().toString();

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Alege o parola",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this,"Alege un username",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Bine ai venit!");
            loadingBar.setMessage("Asteapta cateva momente...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAcces(username,password);
        }
    }

    private void allowAcces(final String username, final String password)
    {

        if(chkBox.isChecked())
        {
            Paper.book().write(UserLocalData.username, username);
            Paper.book().write(UserLocalData.password, password);

        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
               if (dataSnapshot.child(parentDbName).child(username).exists())
               {
                   User usersData = dataSnapshot.child(parentDbName).child(username).getValue(User.class);
                   if(usersData.username.equals(username))
                   {
                       if (usersData.password.equals(password))
                       {
                           if (parentDbName.equals("Admins"))
                           {
                               Toast.makeText(LoginActivity.this,"Bine ai venit!",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                               startActivity(intent);
                           }
                           else if (parentDbName.equals("Users"))
                           {
                               Toast.makeText(LoginActivity.this,"Bine ai venit!",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               UserLocalData.onlineUser = usersData;
                               if(!usersData.isVerified)
                               {
                                   Intent intent = new Intent(LoginActivity.this, VerifyCode.class);
                                   intent.putExtra("nume",username);
                                   startActivity(intent);
                               }
                               else {
                                   Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                   startActivity(intent);
                               }
                           }
                       }
                       else
                       {
                           Toast.makeText(LoginActivity.this,"Parola incorecta! Incearca din nou!",Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();

                       }
                   }
               }
               else
               {
                   Toast.makeText(LoginActivity.this,"Acest cont nu exista!!",Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
