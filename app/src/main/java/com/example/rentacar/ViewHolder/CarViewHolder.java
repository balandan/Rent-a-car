package com.example.rentacar.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentacar.R;

public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView carName, carPrice;
    public ImageView carImage;


    public CarViewHolder(@NonNull View itemView) {
        super(itemView);

        carName = (TextView) itemView.findViewById(R.id.car_name_TextView);
        carPrice =(TextView) itemView.findViewById(R.id.carPrice_TextView);
        carImage = (ImageView) itemView.findViewById(R.id.carPhoto_ImageView);
    }

    @Override
    public void onClick(View v) {

    }
}
