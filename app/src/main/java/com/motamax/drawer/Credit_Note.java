package com.motamax.drawer;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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
import com.motamax.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Credit_Note extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    ListView Item;
    String token,date;
    EditText Mode, Amount, gst;
    TextView issue_datetext;
    String Modes, Amounts, gsts,issue_datetexts;
    Button select_area, submit_register;

    ArrayList item_names = new ArrayList();
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    CalendarView calendar_issue;

    public Credit_Note() {
    }

    public static Credit_Note newInstance(String param1, String param2) {
        Credit_Note fragment = new Credit_Note();
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
        View view = inflater.inflate(R.layout.register_form, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        issue_datetext = view.findViewById(R.id.issue_datetext);
        calendar_issue = view.findViewById(R.id.calendar_issue);
        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = year+"-"+(month + 1)+"-"+dayOfMonth;
                issue_datetext.setText(dayOfMonth+"-"+(month + 1)+"-"+"-"+year);
            }
        });

        Mode = view.findViewById(R.id.Mode);
        Amount = view.findViewById(R.id.Amount);
        gst = view.findViewById(R.id.gst);

        select_area = view.findViewById(R.id.select_Mode);
        select_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType(v);
            }
        });
        submit_register =(Button) view.findViewById(R.id.submit_register);
        submit_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        return view;
    }

    private void attemptRegister() {
        issue_datetexts = issue_datetext.getText().toString();
        Modes = Mode.getText().toString();
        Amounts = Amount.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(issue_datetexts)) {
            issue_datetext.setError(getString(R.string.error_field_required));
            focusView = issue_datetext;
            cancel = true;
        }
        if (TextUtils.isEmpty(Modes)) {
            Mode.setError(getString(R.string.error_field_required));
            focusView = Mode;
            cancel = true;
        }
        if (TextUtils.isEmpty(Amounts)) {
            Amount.setError(getString(R.string.error_field_required));
            focusView = Amount;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        } else {
            AddCustomer();
        }
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

    private void showPopupType(View view) {
        item_names.add("Cash");
        item_names.add("Bank Transfer");
        item_names.add("Cheque");
        item_names.add("LC");

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        dialog.setTitle("List..");
        Item= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_names);
        Item.setAdapter(adapter);
        Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mode.setText(item_names.get(position).toString());
                if(item_names.get(position).toString().equals("Cash"))
                {
                    Modes="Cash";
                }
                if(item_names.get(position).toString().equals("Bank Transfer"))
                {
                    Modes="Bank Transfer";
                }
                if(item_names.get(position).toString().equals("Cheque"))
                {
                    Modes="Cheque";
                }
                if(item_names.get(position).toString().equals("LC"))
                {
                    Modes="LC";
                }
                item_names.clear();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void AddCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_creditnote,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                issue_datetext.setText("");
                                Mode.setText("");
                                Amount.setText("");

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "No Customer added", Toast.LENGTH_SHORT).show();
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
                params.put("payment_mode", Modes);
                params.put("amount", Amounts);
                params.put("gstin", "");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

