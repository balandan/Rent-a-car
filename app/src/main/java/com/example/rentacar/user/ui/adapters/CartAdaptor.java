package com.example.rentacar.user.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rentacar.R;
import com.example.rentacar.user.model.OrderItem;

import java.util.List;


public class CartAdaptor extends ArrayAdapter<OrderItem> {

    private Activity context;
    private List<OrderItem> orderList;

    public CartAdaptor(Activity context, List<OrderItem> orderList)
    {
        super(context, R.layout.order_list_item, orderList);
        this.context = context;
        this.orderList = orderList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.order_list_item, null,true);

        TextView nameCarTextView = (TextView) listViewItem.findViewById(R.id.cart_product_carName);
        TextView idCarTextView = (TextView) listViewItem.findViewById(R.id.cart_car_ID);
        TextView priceCarTextView = (TextView) listViewItem.findViewById(R.id.cart_product_price);
        TextView startDateTextView = (TextView) listViewItem.findViewById(R.id.startDate);
        TextView endDateTextView = (TextView) listViewItem.findViewById(R.id.endDate);

        OrderItem item= orderList.get(position);

        nameCarTextView.setText(item.getCarTitle());
        idCarTextView.setText(item.getIdCar());
        priceCarTextView.setText(item.getPrice());
        startDateTextView.setText(item.getFirstDay());
        endDateTextView.setText(item.getLastDay());

        return listViewItem;
    }
}
