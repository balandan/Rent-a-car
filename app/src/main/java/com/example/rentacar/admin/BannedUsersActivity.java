package com.example.rentacar.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.rentacar.R;
import com.example.rentacar.user.model.User;
import com.example.rentacar.user.ui.adapters.UsersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BannedUsersActivity extends AppCompatActivity {

    ListView bannedUsersListView;
    DatabaseReference userRef;
    List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_users);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();
        bannedUsersListView = (ListView) findViewById(R.id.bannedUsers_listView);


    }
    @Override
    protected void onStart() {
        super.onStart();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    if(user.banned == true) userList.add(user);
                }
                UsersList adapter = new UsersList(BannedUsersActivity.this,userList);
                bannedUsersListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
