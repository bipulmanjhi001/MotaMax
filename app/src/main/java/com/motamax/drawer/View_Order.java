package com.motamax.drawer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.Popup_ListAdapter;
import com.motamax.model.Popup_ListModel;
import com.motamax.model.ProuctsList;
import com.motamax.model.ViewPaidAdapter;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;


public class View_Order extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    private OnFragmentInteractionListener mListener;

    ListView _list;
    String token;
    ArrayList<ProuctsList> mylist = new ArrayList<>();
    ViewPaidAdapter adapter;

    EditText to_date, from_date;
    String to_dates, from_dates;
    private int mYear, mMonth, mDay;
    ImageView from_date_img, to_date_img;
    String id,mrp,rate,quantity,discount,taxable_amount,gst_amount,amount,p_name,name,ids;
    Dialog myDialog;
    ListView products_list;
    ArrayList<Popup_ListModel> popup_listModels;
    Popup_ListAdapter popup_listAdapter;

    public View_Order() {
    }

    public static View_Order newInstance(String param1, String param2) {
        View_Order fragment = new View_Order();
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
        View view = inflater.inflate(R.layout.search_now_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        from_date = view.findViewById(R.id.from_date);
        from_date_img = view.findViewById(R.id.from_date_img);

        from_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                from_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                from_dates = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        to_date = view.findViewById(R.id.to_date);
        to_date_img = view.findViewById(R.id.to_date_img);
        to_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                to_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                to_dates = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        _list = (ListView)view.findViewById(R.id.search_list);
        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView c = (TextView)view.findViewById(R.id.id);
                ids = c.getText().toString();
                Rating();
                popup_listModels.clear();

            }
        });

        _list.setDivider(null);
        mylist=new ArrayList<ProuctsList>();
        popup_listModels=new ArrayList<Popup_ListModel>();
        CallList();
        myDialog = new Dialog(getActivity());

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_orderList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("resp",response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("message");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String address = itemslist.getString("address");
                                    String date = itemslist.getString("date");
                                    String reference = itemslist.getString("reference");
                                    String customer_id = itemslist.getString("customer_id");
                                    String gstin = itemslist.getString("gstin");

                                    String state = itemslist.getString("state");
                                    String name = itemslist.getString("name");
                                    String mobile = itemslist.getString("mobile");
                                    String g_amount = itemslist.getString("g_amount");
                                    String total_amount = itemslist.getString("total_amount");

                                    ProuctsList prouctsList = new ProuctsList(id, date, reference, customer_id, name, mobile, gstin, address, state, g_amount, total_amount);
                                    mylist.add(prouctsList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            adapter = new ViewPaidAdapter(mylist, getActivity());
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
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void Rating(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("message");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    id = itemslist.getString("id");
                                    p_name = itemslist.getString("p_name");
                                    name = itemslist.getString("name");
                                    mrp = itemslist.getString("mrp");
                                    rate = itemslist.getString("rate");
                                    quantity = itemslist.getString("quantity");
                                    discount = itemslist.getString("discount");
                                    taxable_amount = itemslist.getString("taxable_amount");
                                    gst_amount = itemslist.getString("gst_amount");
                                    amount = itemslist.getString("amount");

                                    Popup_ListModel popup_listModel=new Popup_ListModel(rate,quantity,taxable_amount,gst_amount,discount,mrp,name,p_name);
                                    popup_listModels.add(popup_listModel);

                                }

                                Button pay_date_click;
                                myDialog.setContentView(R.layout.invoice_popup2);
                                myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                products_list=(ListView)myDialog.findViewById(R.id.products_list);
                                try {
                                    popup_listAdapter = new Popup_ListAdapter(popup_listModels, getActivity());
                                    products_list.setAdapter(popup_listAdapter);
                                    popup_listAdapter.notifyDataSetChanged();
                                    ListUtils.setDynamicHeight(products_list);

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                pay_date_click=(Button)myDialog.findViewById(R.id.pay_date_click);
                                pay_date_click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.show();
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
                params.put("invoice_id", ids);
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}

