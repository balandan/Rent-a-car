package com.example.rentacar.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.rentacar.R;
import com.example.rentacar.Sablon.UserLocalData;
import com.example.rentacar.user.model.Car;
import com.example.rentacar.user.model.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.PasswordAuthentication;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CarDetailsActivity extends AppCompatActivity {

    private TextView carTitle, carDescription, carPrice;
    private ImageView carPhoto;
    private ElegantNumberButton numberButton;
    private DatabaseReference orderRef;
    String carUniqueId ="";
    private Button confirmBtn;
    Date startDate, finishDate;
    String firstDay ="";
    String lastDay ="";
    String price ="";
    String title ="";
    List<String> fDay;
    List<String> lDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        carUniqueId = getIntent().getStringExtra("codeUnic");
        firstDay = getIntent().getStringExtra("firstDay");
        lastDay = getIntent().getStringExtra("lastDay");
        price = getIntent().getStringExtra("carPrice");
        title = getIntent().getStringExtra("titleCar");


        orderRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(UserLocalData.onlineUser.getUsername());
        carTitle = (TextView) findViewById(R.id.car_details_TitleTextView);
        carDescription = (TextView) findViewById(R.id.car_details_DescriptionTextView);
        carPrice = (TextView) findViewById(R.id.car_details_PriceTextView);
        carPhoto = (ImageView) findViewById(R.id.car_details_ImageView);
        confirmBtn = (Button) findViewById(R.id.detailsCar_addBtn);

        if(firstDay == null || lastDay == null)
        {
            confirmBtn.setVisibility(View.INVISIBLE);
        }

        getCarDetails(carUniqueId);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingCarToList();
            }
        });

    }

    private void addingCarToList()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calCurrentDay = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy,MM,dd");
        try {
            Date startDate = format.parse(firstDay);
            Date finishDate = format.parse(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyy,MM,dd");
        try {
            Date d = (Date) f.parse(String.valueOf(startDate));
            long milliseconds = d.getTime();

            System.out.print("dsada");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calCurrentDay.getTime()).toString();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calCurrentDay.getTime()).toString();



        DatabaseReference orders = FirebaseDatabase.getInstance().getReference("CAR ORDERS");

        final HashMap<String, Object> listMap = new HashMap<>();
        listMap.put("idClient", UserLocalData.onlineUser.getUsername());
        listMap.put("startDay",firstDay);
        listMap.put("finishDay",lastDay);
        listMap.put("price", price);


        orders.child(carUniqueId).child(saveCurrentDate + saveCurrentTime).updateChildren(listMap);
    }


    public void getCarDetails(String carUniqueId)
    {
        DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference().child("CARS");

        carsRef.child(carUniqueId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Car car = dataSnapshot.getValue(Car.class);

                    carTitle.setText(car.getMarca()+" "+ car.getModel() + " " + car.getTip());
                    carDescription.setText(car.getDescription());
                    carPrice.setText(car.getPrice()+ " LEI/ZI");
                    String price = car.getPrice();
                    Picasso.get().load(car.getImage()).into(carPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(CarDetailsActivity.this,UserMainActivity.class);
        intent.putExtra("codeUnic",carUniqueId);
        intent.putExtra("firstDay",firstDay);
        intent.putExtra("lastDay",lastDay);
        intent.putExtra("titleCar",title);
        intent.putExtra("carPrice",price);
        this.startActivity(intent);
        this.overridePendingTransition(0, 0);*/
    }
}
