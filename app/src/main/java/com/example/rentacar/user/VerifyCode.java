package com.example.rentacar.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentacar.LoginActivity;
import com.example.rentacar.R;
import com.example.rentacar.user.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyCode extends AppCompatActivity {

    public String username = "";
    Button trimiteBtn;
    EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        username = getIntent().getStringExtra("nume");

        trimiteBtn = (Button) findViewById(R.id.trimite_Button);
        code = (EditText) findViewById(R.id.verifiedCode_EditText);

        trimiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            User user = ds.getValue(User.class);
                            if (user.username.equals(username)) {
                                if (user.verifiedCode.equals(code.getText().toString())) {
                                    userRef.child(username).child("isVerified").setValue(true);
                                    Intent intent = new Intent(VerifyCode.this, UserMainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(VerifyCode.this, "Va rugam reintroduceti codul!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
