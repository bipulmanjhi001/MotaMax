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

public class DeliveryAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<DeliveryModel> mylist = new ArrayList<>();

    public DeliveryAdapter(ArrayList<DeliveryModel> itemArray, Context mContext) {
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
        private TextView id,date,name,cust_id,mobile,mode,email,gstin,address,g_amount,t_amount,total_amount,paid;
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
                convertView = inflator.inflate(R.layout.delivery_adapter, null);
                view.id = (TextView) convertView.findViewById(R.id.idis);
                view.date = (TextView) convertView.findViewById(R.id.dateis);
                view.name = (TextView) convertView.findViewById(R.id.nameis);
                view.cust_id = (TextView) convertView.findViewById(R.id.cust_idis);
                view.mode = (TextView) convertView.findViewById(R.id.modes);
                view.email = (TextView) convertView.findViewById(R.id.emails);
                view.paid = (TextView) convertView.findViewById(R.id.paidis);
                view.mobile = (TextView) convertView.findViewById(R.id.mobileis);
                view.address = (TextView) convertView.findViewById(R.id.addressis);
                view.gstin = (TextView) convertView.findViewById(R.id.gstinis);
                view.g_amount = (TextView) convertView.findViewById(R.id.g_amountis);
                view.t_amount = (TextView) convertView.findViewById(R.id.t_amountis);
                view.total_amount = (TextView) convertView.findViewById(R.id.total_amountis);
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
            view.date.setText("Date : " + mylist.get(position).getDate());
            view.name.setText("Name : " + mylist.get(position).getName());
            view.cust_id.setText("cust_id : " + mylist.get(position).getCust_id());
            view.mode.setText("Mode : " + mylist.get(position).getMode());
            view.paid.setText("Paid : " + mylist.get(position).getPaid());
            view.email.setText("Email : " + mylist.get(position).getEmail());
            view.mobile.setText("Mobile : " + mylist.get(position).getMobile());
            view.address.setText("Address : " + mylist.get(position).getAddress());
            view.gstin.setText("GST : " + mylist.get(position).getGstin());
            view.g_amount.setText("Gross Amount : " + mylist.get(position).getG_amount());
            view.t_amount.setText("Transport Amount : " + mylist.get(position).getT_amount());
            view.total_amount.setText("Total Amount : " + mylist.get(position).getTotal_amount());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
