package com.motamax.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.motamax.R;
import java.util.ArrayList;

public class ViewPaidAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<ProuctsList> mylist = new ArrayList<>();

    public ViewPaidAdapter(ArrayList<ProuctsList> itemArray, Context mContext) {
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
        private TextView id,date,name,reference,customer_id,mobile,address,g_amount,total_amount;
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
                convertView = inflator.inflate(R.layout.view_paid_adapter, null);
                view.id = (TextView) convertView.findViewById(R.id.id);
                view.date = (TextView) convertView.findViewById(R.id.date);
                view.name = (TextView) convertView.findViewById(R.id.name);
                view.reference = (TextView) convertView.findViewById(R.id.reference);
                view.customer_id = (TextView) convertView.findViewById(R.id.customer_id);
                view.mobile = (TextView) convertView.findViewById(R.id.mobile);
                view.address = (TextView) convertView.findViewById(R.id.address);
                view.g_amount = (TextView) convertView.findViewById(R.id.g_amount);
                view.total_amount = (TextView) convertView.findViewById(R.id.total_amount);

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
            view.date.setText("Date : "+mylist.get(position).getDate());
            view.name.setText("Name : "+mylist.get(position).getName());
            view.reference.setText("Reference : "+mylist.get(position).getReference());
            view.customer_id.setText("Customer Id : "+mylist.get(position).getCustomer_id());
            view.mobile.setText("Mobile : "+mylist.get(position).getMobile());
            view.address.setText("Address : "+mylist.get(position).getAddress());
            view.g_amount.setText("Gross amount : "+mylist.get(position).getG_amount());
            view.total_amount.setText("Total Amount : "+mylist.get(position).getTotal_amount());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}