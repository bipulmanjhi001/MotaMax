package com.motamax.drawer;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.CustList;
import com.motamax.model.CustomerAdapter;
import com.motamax.model.MonthlyModel;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class View_Ledger extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    ListView _list;
    ArrayList<CustList> mylist = new ArrayList<>();
    CustomerAdapter adapter;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    String token,select;
    TextView Dues,Paid,Purchase;
    ProgressBar follow_progress;
    String dues,paid,purchase;
    String mode,monthname;
    int year;
    CustomAdapter customAdapter;
    public static Spinner spCompany;
    ArrayList<MonthlyModel> monthlyModels;
    FloatingActionButton submit_refresh;
    public View_Ledger() {
    }

    public static View_Ledger newInstance(String param1, String param2) {
        View_Ledger fragment = new View_Ledger();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow_up_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        _list = (ListView)view.findViewById(R.id.follow_list);
        _list.setDivider(null);
        mylist=new ArrayList<CustList>();
        Dues=(TextView)view.findViewById(R.id.Dues);
        Paid=(TextView)view.findViewById(R.id.Paid);
        Purchase=(TextView)view.findViewById(R.id.Purchase);
        follow_progress=(ProgressBar)view.findViewById(R.id.follow_progress);
        follow_progress.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
        monthname=(String)android.text.format.DateFormat.format("MMMM", new Date()) + "-" + String.valueOf(year);
        monthlyModels=new ArrayList<MonthlyModel>();
        spCompany = (Spinner)view.findViewById(R.id.spinner);

        submit_refresh=(FloatingActionButton)view.findViewById(R.id.submit_refresh);
        submit_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(submit_refresh, "rotation", 0f, 360f).setDuration(800).start();
                CallSpinnerList();
            }
        });

        CallList();

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

    public void CallList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_customerLedger,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject message= obj.getJSONObject("message");

                                JSONArray userJson = message.getJSONArray("ledger");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String date = itemslist.getString("date");
                                    String customer_id = itemslist.getString("customer_id");
                                    String amount = itemslist.getString("amount");
                                    if(Double.parseDouble(amount) > 0){
                                        mode="Credit";
                                    }
                                    else{
                                        mode="Debit";
                                    }
                                    CustList custList = new CustList(id, date, amount, customer_id,mode);
                                    mylist.add(custList);

                            }
                            JSONObject acc_details= message.getJSONObject("acc_details");
                            paid = acc_details.getString("paid");
                            purchase = acc_details.getString("purchase");
                            dues = acc_details.getString("payable_dues");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            follow_progress.setVisibility(View.GONE);
                            Dues.setText("Dues  : " + dues);
                            Paid.setText("Paid  : " + paid);
                            Purchase.setText("Purchase  : " + purchase);
                            adapter = new CustomerAdapter(mylist, getActivity());
                            _list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
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
                params.put("month",monthname);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        Get_Date();
    }

    public void Get_Date() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_monthly,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("message");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String month = itemslist.getString("month");

                                    MonthlyModel monthlyModel = new MonthlyModel(month);
                                    monthlyModels.add(monthlyModel);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                            }

                            customAdapter = new CustomAdapter(monthlyModels,getActivity());
                            spCompany.setAdapter(customAdapter);
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public class CustomAdapter  extends BaseAdapter implements SpinnerAdapter {
        ArrayList<MonthlyModel> company = new ArrayList<MonthlyModel>();
        private Context context;
        public CustomAdapter(ArrayList<MonthlyModel> company,Context context) {
            super();
            this.company = company;
            this.context = context;
        }
        @Override
        public int getCount() {
            return company.size();
        }

        @Override
        public String getItem(int position) {
            return company.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            public TextView main;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder view = null;
            LayoutInflater inflator = null;

            if (convertView == null) {
                view = new ViewHolder();
                try {
                    inflator = ((Activity) context).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.company_main, null);
                    view.main = (TextView) convertView.findViewById(R.id.main);

                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {
                view = (ViewHolder) convertView.getTag();
            }

            try {
                view.main.setText(company.get(position).getMontly());
                select=company.get(position).getMontly();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;
            view =  View.inflate(context, R.layout.company_dropdown, null);
            final TextView textView = (TextView) view.findViewById(R.id.dropdown);
            textView.setText(company.get(position).getMontly());

            return view;
        }
    }

    public void CallSpinnerList(){
        mylist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_customerLedger,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject message= obj.getJSONObject("message");

                            JSONArray userJson = message.getJSONArray("ledger");
                            for (int i = 0; i < userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                String date = itemslist.getString("date");
                                String customer_id = itemslist.getString("customer_id");
                                String amount = itemslist.getString("amount");
                                if(Double.parseDouble(amount) > 0){
                                    mode="Credit";
                                }
                                else{
                                    mode="Debit";
                                }
                                CustList custList = new CustList(id, date, amount, customer_id,mode);
                                mylist.add(custList);

                            }
                            JSONObject acc_details= message.getJSONObject("acc_details");
                            paid = acc_details.getString("paid");
                            purchase = acc_details.getString("purchase");
                            dues = acc_details.getString("payable_dues");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            follow_progress.setVisibility(View.GONE);
                            Dues.setText("Dues  : " + dues);
                            Paid.setText("Paid  : " + paid);
                            Purchase.setText("Purchase  : " + purchase);
                            adapter = new CustomerAdapter(mylist, getActivity());
                            _list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
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
                params.put("month",select);
                Log.d("months",select);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        monthlyModels.clear();
        Get_Date();
    }
}
