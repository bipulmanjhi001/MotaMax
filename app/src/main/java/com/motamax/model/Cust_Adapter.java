package com.motamax.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.motamax.R;
import java.util.ArrayList;

public class Cust_Adapter extends BaseAdapter {

    private Context mContext;
    ArrayList<CustomerList> mylist = new ArrayList<>();

    public Cust_Adapter(ArrayList<CustomerList> itemArray, Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView id, name, mobile, email, state_name, address,status,change_text;
        LinearLayout CONFIRM_add;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {

                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.cust_list, null);
                view.id = (TextView) convertView.findViewById(R.id.id);
                view.name = (TextView) convertView.findViewById(R.id.name);
                view.mobile = (TextView) convertView.findViewById(R.id.mobile);
                view.email = (TextView) convertView.findViewById(R.id.email);
                view.state_name = (TextView) convertView.findViewById(R.id.state_name);
                view.address = (TextView) convertView.findViewById(R.id.address);
                view.status = (TextView) convertView.findViewById(R.id.status);
                view.change_text = (TextView) convertView.findViewById(R.id.change_text);
                view.CONFIRM_add = (LinearLayout) convertView.findViewById(R.id.CONFIRM_add);
                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.id.setTag(position);
            view.id.setText(mylist.get(position).getId());
            view.name.setText("Name : " + mylist.get(position).getName());
            view.mobile.setText("Mobile : " + mylist.get(position).getMobile());
            view.email.setText("Payment Mode : " + mylist.get(position).getEmail());
            view.state_name.setText("Amount : " + mylist.get(position).getState_name());
            view.address.setText("Date : " + mylist.get(position).getAddress());
            view.status.setText(mylist.get(position).getStatus());
            if(mylist.get(position).getStatus().equals("0")){
                view.change_text.setText("Pending");
                view.CONFIRM_add.setVisibility(View.VISIBLE);
            }
            if(mylist.get(position).getStatus().equals("1")){
                view.change_text.setText("Accepted");
                view.CONFIRM_add.setVisibility(View.VISIBLE);
            }
            if(mylist.get(position).getStatus().equals("2")){
                view.change_text.setText("Rejected");
                view.CONFIRM_add.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}