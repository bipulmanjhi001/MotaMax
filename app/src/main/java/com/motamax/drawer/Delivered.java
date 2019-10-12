package com.motamax.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.ViewDeliveryList;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Delivered extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    private OnFragmentInteractionListener mListener;
    ProgressBar delivered_progress;
    ListView deliverd_list;
    String token;
    ArrayList<ViewDeliveryList> viewDeliveredLists;
    ViewAttendanceAdapter viewDeliveredList;

    public Delivered() {
    }

    public static Delivered newInstance(String param1, String param2) {
        Delivered fragment = new Delivered();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivered_list, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");
        delivered_progress=(ProgressBar)view.findViewById(R.id.delivered_progress);
        deliverd_list=(ListView)view.findViewById(R.id.deliverd_list);
        viewDeliveredLists=new ArrayList<ViewDeliveryList>();
        ViewDelivery();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class ViewAttendanceAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<ViewDeliveryList> mylist = new ArrayList<>();

        public ViewAttendanceAdapter(ArrayList<ViewDeliveryList> itemArray, Context mContext) {
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
            private TextView id, name, mobile,designation,email,address,g_amount,t_amount,total_amount,paid;
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
                    convertView = inflator.inflate(R.layout.view_delivery_list, null);
                    view.id = (TextView) convertView.findViewById(R.id.idiss);
                    view.name = (TextView) convertView.findViewById(R.id.nameiss);
                    view.email=(TextView)convertView.findViewById(R.id.emailss);
                    view.designation = (TextView) convertView.findViewById(R.id.designation);
                    view.mobile=(TextView)convertView.findViewById(R.id.mobileiss);
                    view.address=(TextView)convertView.findViewById(R.id.addressiss);
                    view.g_amount = (TextView) convertView.findViewById(R.id.g_amountiss);
                    view.t_amount=(TextView)convertView.findViewById(R.id.t_amountiss);
                    view.total_amount = (TextView) convertView.findViewById(R.id.total_amountiss);
                    view.paid=(TextView)convertView.findViewById(R.id.paidiss);

                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText("ID : "+mylist.get(position).getId());
                view.name.setText("Date : "+mylist.get(position).getName());
                view.email.setText("Email : "+mylist.get(position).getEmail());
                view.designation.setText("Mobile : "+mylist.get(position).getDesignation());
                view.mobile.setText("Name : "+mylist.get(position).getMobile());
                view.address.setText("Address : "+mylist.get(position).getAddress());
                view.g_amount.setText("G Amount : "+mylist.get(position).getG_amount());
                view.t_amount.setText("T Amount : "+mylist.get(position).getT_amount());
                view.total_amount.setText("Total Amount : "+mylist.get(position).getTotal_amount());
                view.paid.setText("Paid : "+mylist.get(position).getPaid());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
    private void ViewDelivery(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_deliveredList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("message");

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("date");
                                    String mobile=itemslist.getString("name");
                                    String designation=itemslist.getString("mobile");
                                    String email = itemslist.getString("email");
                                    String address=itemslist.getString("address");
                                    String g_amount = itemslist.getString("g_amount");
                                    String t_amount=itemslist.getString("t_amount");
                                    String total_amount = itemslist.getString("total_amount");
                                    String paid=itemslist.getString("paid");

                                    ViewDeliveryList attendance = new ViewDeliveryList(id, name, mobile,designation,email,address,g_amount,t_amount,total_amount,paid);
                                    viewDeliveredLists.add(attendance);
                                }
                                try {
                                    delivered_progress.setVisibility(View.GONE);
                                    viewDeliveredList = new ViewAttendanceAdapter(viewDeliveredLists, getActivity());
                                    deliverd_list.setAdapter(viewDeliveredList);
                                    viewDeliveredList.notifyDataSetChanged();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}