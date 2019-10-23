package com.example.rentacar.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.rentacar.LoginActivity;
import com.example.rentacar.R;
import com.example.rentacar.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    List<User> rezult = new ArrayList<User>();
    List<User> listOfUsers = new ArrayList<>();

    ImageView cars,users,logOut,bannedUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        cars = (ImageView) findViewById(R.id.cars_image);
        users = (ImageView) findViewById(R.id.admin_users_imageView);
        bannedUsers = (ImageView) findViewById(R.id.forbidden_imageView);
        logOut = (ImageView) findViewById(R.id.admin_logOut_imageView);


        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (AdminActivity.this, AdminCarsActivity.class);
                startActivity(intent);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bannedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, BannedUsersActivity.class);
                startActivity(intent);
            }
        });
    }
}
