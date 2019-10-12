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

public class Popup_ListAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Popup_ListModel> mylist = new ArrayList<>();

    public Popup_ListAdapter(ArrayList<Popup_ListModel> itemArray, Context mContext) {
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
        private TextView rates, qtys, taxs, gsts, discounts, mrps,c_names,p_names;
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
                convertView = inflator.inflate(R.layout.popup_list, null);
                view.c_names = (TextView) convertView.findViewById(R.id.c_name);
                view.p_names = (TextView) convertView.findViewById(R.id.p_name);
                view.rates = (TextView) convertView.findViewById(R.id.rate);
                view.qtys = (TextView) convertView.findViewById(R.id.qty);
                view.taxs = (TextView) convertView.findViewById(R.id.tax);
                view.gsts = (TextView) convertView.findViewById(R.id.gst);
                view.discounts = (TextView) convertView.findViewById(R.id.discount);
                view.mrps = (TextView) convertView.findViewById(R.id.mrp);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.c_names.setText("Category : "+mylist.get(position).getC_names());
            view.p_names.setText("Product : "+mylist.get(position).getP_names());
            view.rates.setText("Rate : "+mylist.get(position).getRates());
            view.qtys.setText("Quantity : "+mylist.get(position).getQtys());
            view.taxs.setText("Tax : "+mylist.get(position).getTaxs());
            view.gsts.setText("GST : "+mylist.get(position).getGsts());
            view.discounts.setText("Discount : "+mylist.get(position).getDiscounts());
            view.mrps.setText("MRP : "+mylist.get(position).getMrps());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}