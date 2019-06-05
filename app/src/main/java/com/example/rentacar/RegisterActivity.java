package com.example.rentacar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText registerUsername,registerPassword,registerPhoneNumber;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = (Button) findViewById(R.id.register_create_account_btn);
        registerUsername = (EditText) findViewById(R.id.register_username_input);
        registerPassword = (EditText) findViewById(R.id.register_password_input);
        registerPhoneNumber = (EditText) findViewById(R.id.register_phone_input);
        loadingBar = new ProgressDialog(this);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }

        });
    }
        private void createAccount()
        {
            String username = registerUsername.getText().toString();
            String password = registerPassword.getText().toString();
            String phone = registerPhoneNumber.getText().toString();

            if (TextUtils.isEmpty(username))
            {
                Toast.makeText(this,"Please complete username field!",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this,"Please complete password field!",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(phone))
            {
                Toast.makeText(this,"Please complete phone number field!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadingBar.setTitle("Create account!");
                loadingBar.setMessage("Please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validatePhoneNumber(username,password,phone);
            }


        }

    private void validatePhoneNumber(final String username, final String password, final String phone)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> usersMap = new HashMap<>();
                    usersMap.put("phone",phone);
                    usersMap.put("username",username);
                    usersMap.put("password",password);

                    rootRef.child("Users").child(phone).updateChildren(usersMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Welcome! Your account has been created!",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent (RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this,"Network error",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This phone number is already in use. Try another one!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent (RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


}
