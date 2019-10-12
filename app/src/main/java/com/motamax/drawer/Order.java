package com.motamax.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.CompanyModel;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Order extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";

    String getId2;
    String token;
    Button submit_order;
    String fitting_data="",date;
    ListView company_list;
    CalendarView calendar_issue;
    TextView issue_datetext;
    ArrayList<CompanyModel> companyModels;
    CompanyAdapter companyAdapter;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Order() {
    }

    public static Order newInstance(String param1, String param2) {
        Order fragment = new Order();
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
        View view = inflater.inflate(R.layout.order_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        ProductList();

        issue_datetext = view.findViewById(R.id.issue_datetext);
        calendar_issue = view.findViewById(R.id.calendar_issue);
        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                date = year+"-"+(month + 1)+"-"+dayOfMonth;
                Log.d("date",date);
                issue_datetext.setText(dayOfMonth+"-"+(month + 1)+"-"+"-"+year);

            }
        });

        company_list = view.findViewById(R.id.product_list);
        company_list.setDivider(null);
        LayoutInflater myinflater2 = getLayoutInflater();
        ViewGroup myHeader2 = (ViewGroup) myinflater2.inflate(R.layout.headerlayout2, company_list, false);
        company_list.addHeaderView(myHeader2, null, false);

        companyModels = new ArrayList<CompanyModel>();
        submit_order = view.findViewById(R.id.submit_order);
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

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
    public void ProductList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_COMPANY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("productList");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("p_name");

                                    CompanyModel companyModel = new CompanyModel(id, name, false);
                                    companyModels.add(companyModel);
                                }
                            } else {
                                Toast.makeText(getActivity(), "No Product added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            companyAdapter = new CompanyAdapter(companyModels, getActivity());
                            company_list.setAdapter(companyAdapter);
                            companyAdapter.notifyDataSetChanged();

                            ListUtils.setDynamicHeight(company_list);
                        } catch (NullPointerException e) {
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

    private void attemptRegister() {

       // if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(fitting_data)) {
            Final_Submit_Data();
        //}
    }
    public void Final_Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_ORDERFINALSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), "Order Success", Toast.LENGTH_SHORT).show();
                                date="";
                                getId2="";

                                issue_datetext.setText("");

                                company_list.setAdapter(companyAdapter);
                                companyAdapter.notifyDataSetChanged();
                                ListUtils.setDynamicHeight(company_list);

                            } else {
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                            }
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
                params.put("date", date);
                params.put("product", fitting_data);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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

    public class CompanyAdapter extends BaseAdapter {
        ArrayList<CompanyModel> mylist = new ArrayList<CompanyModel>();
        private Context mContext;
        String valueList;
        JSONObject jObjectData;

        public CompanyAdapter(ArrayList<CompanyModel> itemArray, Context mContext) {
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
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder view;
            LayoutInflater inflator = null;
            if (convertView == null) {
                view = new ViewHolder();
                try {
                    inflator = ((Activity) mContext).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.company_list, null);
                    view.id = convertView.findViewById(R.id.company_id);
                    view.name = convertView.findViewById(R.id.company_name);
                    view.fitting_num = (EditText) convertView.findViewById(R.id.fitting_number);
                    view.checkBox = convertView.findViewById(R.id.company_check);
                    view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                try {
                                        jObjectData = new JSONObject();
                                        jObjectData.put("id", mylist.get(position).getIda());
                                        jObjectData.put("value", valueList);

                                } catch (Exception e) {
                                }
                            }
                            if (fitting_data.length() > 5) {
                                fitting_data = fitting_data.concat(jObjectData.toString());
                                Log.d("data",fitting_data);
                            } else {
                                fitting_data = jObjectData.toString();
                                Log.d("data",fitting_data);
                            }
                        }
                    });
                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText(mylist.get(position).getIda());
                view.name.setText(mylist.get(position).getName());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            view.fitting_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    valueList = s.toString();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            private TextView id, name;
            private CheckBox checkBox;
            public EditText fitting_num;
        }
    }

}
