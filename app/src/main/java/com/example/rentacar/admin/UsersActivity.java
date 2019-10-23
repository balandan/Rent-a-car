package com.example.rentacar.admin;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UsersActivity extends AppCompatActivity {

    ListView userListView;
    EditText userSearch;
    ImageView searchIcon,bannUser;
    DatabaseReference userRef;
    List<User> userList;
    User userToBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_activity);

        userSearch = (EditText) findViewById(R.id.user_searchBar);
        searchIcon = (ImageView) findViewById(R.id.user_searchIcon);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userListView = (ListView) findViewById(R.id.user_ListView);
        bannUser = (ImageView) findViewById(R.id.bann_userBtn);
        userList = new ArrayList<>();

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userSearch.getText().toString().equals("")) {
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                if (user.username.contains(userSearch.getText().toString()))
                                    userList.add(user);
                            }
                            UsersList adapter = new UsersList(UsersActivity.this, userList);
                            userListView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userToBan = (User)parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                builder.setTitle("Atentie!");
                builder.setMessage("Vrei sa stergi acest user?");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userRef.child(userToBan.getUsername()).child("banned").setValue(true);
                    }
                });
                builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        userSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            User user = ds.getValue(User.class);
                            if(user.banned == false) userList.add(user);
                        }
                        UsersList adapter = new UsersList(UsersActivity.this,userList);
                        userListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                    if(user.banned == false) userList.add(user);
                }
                UsersList adapter = new UsersList(UsersActivity.this,userList);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}



