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

public class InvoiceAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<InvoiceList> mylist = new ArrayList<>();

    public InvoiceAdapter(ArrayList<InvoiceList> itemArray, Context mContext) {
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
        private TextView idi, datei, namei, cust_idi, mode, paidi, duesi, mobilei, addressi,gst,g_amt,t_amt,total_amounti;
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
                convertView = inflator.inflate(R.layout.invoice_adapter, null);
                view.idi = (TextView) convertView.findViewById(R.id.idi);
                view.datei = (TextView) convertView.findViewById(R.id.datei);
                view.namei = (TextView) convertView.findViewById(R.id.namei);
                view.cust_idi = (TextView) convertView.findViewById(R.id.cust_idi);
                view.mode = (TextView) convertView.findViewById(R.id.mode);
                view.paidi = (TextView) convertView.findViewById(R.id.paidi);
                view.duesi = (TextView) convertView.findViewById(R.id.duesi);
                view.mobilei = (TextView) convertView.findViewById(R.id.mobilei);
                view.addressi = (TextView) convertView.findViewById(R.id.addressi);
                view.gst = (TextView) convertView.findViewById(R.id.gstini);
                view.g_amt = (TextView) convertView.findViewById(R.id.g_amounti);
                view.t_amt = (TextView) convertView.findViewById(R.id.t_amounti);
                view.total_amounti = (TextView) convertView.findViewById(R.id.total_amounti);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.idi.setTag(position);
            view.idi.setText(mylist.get(position).getId());
            view.datei.setText("Date : " + mylist.get(position).getDate());
            view.namei.setText("Name : " + mylist.get(position).getName());
            view.cust_idi.setText("cust_id : " + mylist.get(position).getCust_id());
            view.mode.setText("Mode : " + mylist.get(position).getMode());
            view.paidi.setText("Paid : " + mylist.get(position).getPaid());
            view.duesi.setText("Dues : " + mylist.get(position).getDues());
            view.mobilei.setText("Mobile : " + mylist.get(position).getMobile());
            view.addressi.setText("Address : " + mylist.get(position).getAddress());
            view.gst.setText("GST : " + mylist.get(position).getGstin());
            view.g_amt.setText("Gross Amount : " + mylist.get(position).getG_amount());
            view.t_amt.setText("Transport Amount : " + mylist.get(position).getT_amount());
            view.total_amounti.setText("Total Amount : " + mylist.get(position).getTotal_amount());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}