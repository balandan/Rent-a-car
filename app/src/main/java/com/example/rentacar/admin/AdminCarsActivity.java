package com.example.rentacar.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.rentacar.R;

public class AdminCarsActivity extends AppCompatActivity {

    private ImageView miniCars,sedanCars,vintageCars,offRoadCars,trucks,tractors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cars);

        miniCars = (ImageView) findViewById(R.id.smallCars_ImageView);
        sedanCars = (ImageView) findViewById(R.id.sedanCars_ImageView);
        vintageCars = (ImageView) findViewById(R.id.oldCars_ImageView);
        offRoadCars = (ImageView) findViewById(R.id.monsterCars_ImageView);
        trucks = (ImageView) findViewById(R.id.truckCars_ImageView);
        tractors = (ImageView) findViewById(R.id.tractorCars_ImageView);

        miniCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this, AdminAddCar.class);
                intent.putExtra("categorie","Smart car");
                startActivity(intent);
            }
        });

        sedanCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this,AdminAddCar.class);
                intent.putExtra("categorie","Masini sedan");
                startActivity(intent);
            }
        });

        vintageCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this,AdminAddCar.class);
                intent.putExtra("categorie","Masini de epoca");
                startActivity(intent);
            }
        });

        offRoadCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this,AdminAddCar.class);
                intent.putExtra("categorie","Masini OFF Road");
                startActivity(intent);
            }
        });

        trucks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this,AdminAddCar.class);
                intent.putExtra("categorie","Camioane");
                startActivity(intent);
            }
        });

        tractors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCarsActivity.this,AdminAddCar.class);
                intent.putExtra("categorie","Utilaje agricole");
                startActivity(intent);
            }
        });

    }

}
