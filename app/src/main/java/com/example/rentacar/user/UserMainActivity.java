package com.example.rentacar.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentacar.LoginActivity;
import com.example.rentacar.R;
import com.example.rentacar.Sablon.UserLocalData;
import com.example.rentacar.user.model.Car;
import com.example.rentacar.user.model.CarOrder;
import com.example.rentacar.user.ui.adapters.CarList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference carRef;
    ListView listViewCars;
    List<Car> carList;
    EditText userInput;
    ImageView searchIcon;
    Car selectedCar;
    String firstDay ="";
    String lastDay ="";
    List<String> fDay;
    List<String> lDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        firstDay = getIntent().getStringExtra("firstDay");
        lastDay = getIntent().getStringExtra("lastDay");

        fDay = new ArrayList<>();
        lDay = new ArrayList<>();
        if(firstDay != null && lastDay != null && !firstDay.equals("") && !lastDay.equals("")) {
            fDay = Arrays.asList(firstDay.split(","));
            lDay = Arrays.asList(lastDay.split(","));
        }

        Toast.makeText(UserMainActivity.this,firstDay,Toast.LENGTH_SHORT).show();
        Toast.makeText(UserMainActivity.this,lastDay,Toast.LENGTH_SHORT).show();

        carRef = FirebaseDatabase.getInstance().getReference().child("CARS");
        carList = new ArrayList<>();
        userInput = (EditText) findViewById(R.id.car_searchBar);
        searchIcon = (ImageView) findViewById(R.id.car_searchIcon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(UserLocalData.onlineUser.getUsername().toString());

        listViewCars = (ListView) findViewById(R.id.cars_ListView);

        listViewCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCar = (Car)parent.getItemAtPosition(position);
                Intent intent = new Intent(UserMainActivity.this,CarDetailsActivity.class);
                intent.putExtra("codeUnic",selectedCar.getTitle());
                intent.putExtra("firstDay",firstDay);
                intent.putExtra("lastDay",lastDay);
                intent.putExtra("titleCar",selectedCar.getMarca()+selectedCar.getModel()+selectedCar.getTip());
                intent.putExtra("carPrice",selectedCar.price);
                startActivity(intent);
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.getText().toString().equals("")) {
                    carRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            carList.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                Car car = ds.getValue(Car.class);
                                if((car.model+car.marca+car.tip).contains(userInput.getText().toString()))
                                {
                                    carList.add(car);
                                }
                            }
                            CarList adapter = new CarList(UserMainActivity.this,carList);
                            listViewCars.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        carList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            Car car = ds.getValue(Car.class);
                            carList.add(car);

                        }
                        CarList adapter = new CarList(UserMainActivity.this,carList);
                        listViewCars.setAdapter(adapter);
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
        //onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        carRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    final Car car = ds.getValue(Car.class);
                    if(firstDay == null || lastDay == null) {
                        carList.add(car);
                    }
                    else {
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("CAR ORDERS");
                        rootRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Boolean isAvailable = true;
                                if (dataSnapshot.hasChild(car.getTitle())) {
                                    for (DataSnapshot ds : dataSnapshot.child(car.getTitle()).getChildren()) {
                                        CarOrder cOrder = ds.getValue(CarOrder.class);
                                        String[] carFirstDay = cOrder.getStartDay().split(",");
                                        String[] carLastDay = cOrder.getFinishDay().split(",");

                                        if((Integer.parseInt(lDay.get(0)) == Integer.parseInt(carFirstDay[0]) && Integer.parseInt(fDay.get(0)) == Integer.parseInt(carFirstDay[0]))
                                          && (Integer.parseInt(lDay.get(1))+1 == Integer.parseInt(carFirstDay[1]) && Integer.parseInt(fDay.get(1))+1 == Integer.parseInt(carFirstDay[1]))
                                          && (Integer.parseInt(lDay.get(2)) < Integer.parseInt(carFirstDay[2]) && Integer.parseInt(fDay.get(2)) < Integer.parseInt(carFirstDay[2])))
                                        {
                                            isAvailable = true;
                                        }
                                        else if((Integer.parseInt(lDay.get(0)) == Integer.parseInt(carLastDay[0]) && Integer.parseInt(fDay.get(0)) == Integer.parseInt(carLastDay[0]))
                                                && (Integer.parseInt(lDay.get(1))+1 == Integer.parseInt(carLastDay[1]) && Integer.parseInt(fDay.get(1))+1 == Integer.parseInt(carLastDay[1]))
                                                && (Integer.parseInt(lDay.get(2)) > Integer.parseInt(carLastDay[2]) && Integer.parseInt(fDay.get(2)) > Integer.parseInt(carLastDay[2])))
                                        {
                                            isAvailable = true;
                                        }
                                        else if((Integer.parseInt(lDay.get(0)) == Integer.parseInt(carFirstDay[0]) && Integer.parseInt(fDay.get(0)) == Integer.parseInt(carFirstDay[0]))
                                                && (Integer.parseInt(lDay.get(1))+1 < Integer.parseInt(carFirstDay[1]) && Integer.parseInt(fDay.get(1))+1 < Integer.parseInt(carFirstDay[1])))
                                        {
                                            isAvailable = true;
                                        }
                                        else if((Integer.parseInt(lDay.get(0)) == Integer.parseInt(carLastDay[0]) && Integer.parseInt(fDay.get(0)) == Integer.parseInt(carLastDay[0]))
                                                && (Integer.parseInt(lDay.get(1))+1 > Integer.parseInt(carLastDay[1]) && Integer.parseInt(fDay.get(1))+1 > Integer.parseInt(carLastDay[1])))
                                        {
                                            isAvailable = true;
                                        }
                                        else
                                        {
                                            isAvailable = false;
                                            break;
                                        }
                                    }
                                    if(isAvailable == true)
                                    {
                                        carList.add(car);
                                    }
                                }
                                else
                                {
                                    carList.add(car);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                CarList adapter = new CarList(UserMainActivity.this,carList);
                listViewCars.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id == R.id.nav_perioada){
            Intent intent = new Intent(UserMainActivity.this, PickTimeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders) {
            Intent intent  = new Intent(UserMainActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_AllCars) {
            sortCarByCategory("all cars");
        }
        else if(id == R.id.nav_masiniDeEpoca) {
            sortCarByCategory("Masini de epoca");
        }
        else if(id == R.id.nav_MasiniMici) {
            sortCarByCategory("Smart car");
        }
        else if(id == R.id.nav_masiniSedan) {
            sortCarByCategory("Masini sedan");
        }
        else if(id == R.id.nav_AgroCars) {
            sortCarByCategory("Utilaje agricole");
        }
        else if(id == R.id.nav_camioane) {
            sortCarByCategory("Camioane");
        }
        else if(id == R.id.nav_masiniOffRoad) {
            sortCarByCategory("Masini OFF Road");
        }
        else if (id == R.id.nav_settings) {

        }
        else if (id == R.id.nav_logout)
        {
            Intent intent  = new Intent(UserMainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Paper.book().destroy();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sortCarByCategory(final String category)
    {
        carRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Car car = ds.getValue(Car.class);
                    if(category.equals("all cars"))
                    {
                        carList.add(car);
                    }
                    else if(car.getCategory().equals(category))
                    {
                        carList.add(car);
                    }
                }
                CarList adapter = new CarList(UserMainActivity.this,carList);
                listViewCars.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
