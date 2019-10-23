package com.example.rentacar.user.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentacar.R;
import com.example.rentacar.user.model.Car;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CarList extends ArrayAdapter<Car> {
    private Activity context;
    private List<Car> carList;

    public CarList(Activity context, List<Car> carList){
        super(context, R.layout.car_layout,carList);
        this.context = context;
        this.carList = carList;
     }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.car_layout,null,true);

        TextView title = (TextView) listViewItem.findViewById(R.id.car_name_TextView);
        TextView price = (TextView) listViewItem.findViewById(R.id.carPrice_TextView);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.carPhoto_ImageView);

        Car car = carList.get(position);

        title.setText(car.getMarca() +" " + car.getModel() + " " + car.getTip());
        price.setText(car.price + " LEI/zi");
        Picasso.get().load(car.getImage()).into(image);

        return listViewItem;
    }
}
