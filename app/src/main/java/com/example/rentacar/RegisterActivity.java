package com.example.rentacar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText registerUsername,registerPassword,registerEmail;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = (Button) findViewById(R.id.register_create_account_btn);
        registerUsername = (EditText) findViewById(R.id.register_username_input);
        registerPassword = (EditText) findViewById(R.id.register_password_input);
        registerEmail = (EditText) findViewById(R.id.register_email_input);
        loadingBar = new ProgressDialog(this);

        createAccountBtn.setOnClickListener( new View.OnClickListener() {
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
            String email = registerEmail.getText().toString();

            if (TextUtils.isEmpty(username))
            {
                Toast.makeText(this,"Alege un username!",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this,"Alege o parola!",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this,"Alege un email!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadingBar.setTitle("Creare cont...!");
                loadingBar.setMessage("Dureaza cateva momente.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validatePhoneNumber(username,password,email);
            }


        }

    private void validatePhoneNumber(final String username, final String password, final String email)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!((dataSnapshot.child("Users").child(username).exists()) && (dataSnapshot.child("Users").child(email).exists())))
                {
                    HashMap<String,Object> usersMap = new HashMap<>();
                    String  uniqueID = UUID.randomUUID().toString();
                    final String code = UUID.randomUUID().toString().substring(0,6);
                    Boolean banned = false;
                    Boolean isVerified = false;

                    usersMap.put("userID",uniqueID);
                    usersMap.put("username",username);
                    usersMap.put("password",password);
                    usersMap.put("email",email);
                    usersMap.put("banned",banned);
                    usersMap.put("isVerified",isVerified);
                    usersMap.put("verifiedCode",code);

                    rootRef.child("Users").child(username).updateChildren(usersMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Salut! Contul tau a fost creat!",Toast.LENGTH_SHORT).show();
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

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                sendEmail(email,code);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Acest username sau email este luat. Incearca altele!",Toast.LENGTH_SHORT).show();
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

    private void sendEmail(String receiver, String code)
    {
        Properties props = new Properties();


        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        final String myEmail = "rentacar2019app@gmail.com";
        final String password = "Rentacar2019!";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail,password);
            }
        });

        Message message = prepareMessage(session,myEmail,receiver,code);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String myEmail, String receiver, String code) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(receiver));
            message.setSubject("[Rent a car]Check your account!");
            message.setText("Welcome! \n \n Your confirmation code is:" + code);
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



}
