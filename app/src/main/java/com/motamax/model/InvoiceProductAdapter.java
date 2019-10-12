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

public class InvoiceProductAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<InvoiceProductList> mylist = new ArrayList<>();

    public InvoiceProductAdapter(ArrayList<InvoiceProductList> itemArray, Context mContext) {
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
        private TextView id,name,p_name,mrp,rate,gst_rate,quantity,taxable_amount,gst_amount,amount,sales_id,hsn_code,stock;
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
                convertView = inflator.inflate(R.layout.invoice_products_list, null);
                view.id = (TextView) convertView.findViewById(R.id.id);
                view.name = (TextView) convertView.findViewById(R.id.name);
                view.p_name = (TextView) convertView.findViewById(R.id.p_name);
                view.mrp = (TextView) convertView.findViewById(R.id.mrp);
                view.rate = (TextView) convertView.findViewById(R.id.rate);
                view.gst_rate = (TextView) convertView.findViewById(R.id.gst_rate);
                view.quantity = (TextView) convertView.findViewById(R.id.quantity);
                view.taxable_amount = (TextView) convertView.findViewById(R.id.taxable_amount);
                view.gst_amount = (TextView) convertView.findViewById(R.id.gst_amount);
                view.amount = (TextView) convertView.findViewById(R.id.amount);
                view.sales_id = (TextView) convertView.findViewById(R.id.sales_id);
                view.hsn_code = (TextView) convertView.findViewById(R.id.hsn_code);
                view.stock = (TextView) convertView.findViewById(R.id.stock);

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
            view.name.setText("Dues : " + mylist.get(position).getName());
            view.p_name.setText("Paid : " + mylist.get(position).getP_name());
            view.mrp.setText("Mode : " + mylist.get(position).getMrp());
            view.rate.setText("Email : " + mylist.get(position).getRate());
            view.gst_rate.setText("GST IN : " + mylist.get(position).getGst_rate());
            view.quantity.setText("Address : " + mylist.get(position).getQuantity());
            view.taxable_amount.setText("Gross amount : " + mylist.get(position).getTaxable_amount());
            view.gst_amount.setText("Transport amount : " + mylist.get(position).getGst_amount());
            view.amount.setText("Total Amount  : " + mylist.get(position).getAmount());
            view.sales_id.setText("Date : " + mylist.get(position).getSales_id());
            view.hsn_code.setText("Name : " + mylist.get(position).getHsn_code());
            view.stock.setText("Mobile : " + mylist.get(position).getStock());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
