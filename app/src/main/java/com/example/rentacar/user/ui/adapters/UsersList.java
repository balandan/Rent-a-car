package com.example.rentacar.user.ui.adapters;

import android.app.Activity;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentacar.R;
import com.example.rentacar.user.model.User;

import java.util.List;

public class UsersList extends ArrayAdapter {
    private Activity context;
    private List<User> userList;

    public UsersList(Activity context, List<User> userList){
        super(context, R.layout.list_layout,userList);
        this.context = context;
        this.userList = userList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView username = (TextView) listViewItem.findViewById(R.id.usernameTextView);
        TextView email = (TextView) listViewItem.findViewById(R.id.emailTextView);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.user_image);

        User user = userList.get(position);

        username.setText(user.getUsername().toString());
        email.setText(user.getEmail().toString());

        return listViewItem;

    }
}
