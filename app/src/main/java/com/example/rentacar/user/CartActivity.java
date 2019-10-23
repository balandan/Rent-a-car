package com.example.rentacar.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.rentacar.R;
import com.example.rentacar.Sablon.UserLocalData;
import com.example.rentacar.user.model.OrderItem;
import com.example.rentacar.user.ui.adapters.CartAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ListView ordersListView;
    DatabaseReference ordersRef;
    List<OrderItem>  orderItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ordersListView = (ListView) findViewById(R.id.cart_listView);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(UserLocalData.onlineUser.getUsername());
        orderItemsList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderItemsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    OrderItem orderItem = ds.getValue(OrderItem.class);
                    orderItemsList.add(orderItem);
                }
                CartAdaptor adaptor = new CartAdaptor(CartActivity.this,orderItemsList);
                ordersListView.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
