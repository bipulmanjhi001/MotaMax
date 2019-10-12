package com.motamax.drawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.InvoiceProductAdapter;
import com.motamax.model.InvoiceProductList;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class InvoiceProduct extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    String token;
    ProgressBar invoice_product_progress;
    ListView _list;
    ArrayList<InvoiceProductList> invoiceProductLists;
    InvoiceProductAdapter invoiceProductAdapter;

    public InvoiceProduct() {

    }

    public static InvoiceProduct newInstance(String param1, String param2) {
        InvoiceProduct fragment = new InvoiceProduct();
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
        View view = inflater.inflate(R.layout.invoice_products, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        invoice_product_progress=(ProgressBar)view.findViewById(R.id.invoice_product_progress);
        _list = (ListView)view.findViewById(R.id.invoice_product_list);
        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        _list.setDivider(null);
        invoiceProductLists=new ArrayList<InvoiceProductList>();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_oldInvoice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                                JSONArray userJson = obj.getJSONArray("message");

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);

                                    String id = itemslist.getString("id");
                                    String sales_id = itemslist.getString("date");
                                    String hsn_code = itemslist.getString("name");
                                    String stock = itemslist.getString("mobile");
                                    String mrp = itemslist.getString("mode");
                                    String rate = itemslist.getString("email");
                                    String gst_rate = itemslist.getString("gstin");
                                    String quantity = itemslist.getString("address");
                                    String taxable_amount = itemslist.getString("g_amount");
                                    String gst_amount = itemslist.getString("t_amount");
                                    String amount = itemslist.getString("total_amount");
                                    String p_name = itemslist.getString("paid");
                                    String name = itemslist.getString("dues");

                                    InvoiceProductList invoiceProductList = new InvoiceProductList(
                                            id, sales_id, hsn_code, stock, mrp, rate,
                                            gst_rate,quantity,taxable_amount,gst_amount,amount,p_name,name);

                                    invoiceProductLists.add(invoiceProductList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "No value for..", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            invoice_product_progress.setVisibility(View.GONE);
                            invoiceProductAdapter = new InvoiceProductAdapter(invoiceProductLists, getActivity());
                            _list.setAdapter(invoiceProductAdapter);
                            invoiceProductAdapter.notifyDataSetChanged();
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
}
