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

public class CustomerAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<CustList> mylist = new ArrayList<>();

    public CustomerAdapter(ArrayList<CustList> itemArray, Context mContext) {
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
        private TextView id,date,mode,customer_id,g_amount;
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
                convertView = inflator.inflate(R.layout.company_list2, null);
                view.id = (TextView) convertView.findViewById(R.id.idc);
                view.date = (TextView) convertView.findViewById(R.id.datec);
                view.customer_id = (TextView) convertView.findViewById(R.id.customer_idc);
                view.g_amount = (TextView) convertView.findViewById(R.id.g_amountc);
                view.mode = (TextView) convertView.findViewById(R.id.mode);
                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.id.setTag(position);
            view.id.setText(mylist.get(position).getIds());
            view.date.setText("Date : "+mylist.get(position).getDates());
            view.customer_id.setText("Customer Id : "+mylist.get(position).getCustomer_id());
            view.g_amount.setText("Amount : "+mylist.get(position).getAmount());
            view.mode.setText(mylist.get(position).getMode());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}