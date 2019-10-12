package com.motamax.drawer;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.InvoiceAdapter;
import com.motamax.model.InvoiceList;
import com.motamax.model.Popup_ListAdapter;
import com.motamax.model.Popup_ListModel;
import com.motamax.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Order_Deliver extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    String token,ids;
    Dialog myDialog;
    ListView _list;
    ArrayList<InvoiceList> mylist = new ArrayList<>();
    InvoiceAdapter adapter;
    ProgressBar invoice_progress;
    String id,mrp,rate,quantity,discount,taxable_amount,gst_amount,amount,p_name,name;
    SignaturePad signaturePad;
    ListView products_list;
    ArrayList<Popup_ListModel> popup_listModels;
    Popup_ListAdapter popup_listAdapter;

    String getPaths,signs;
    String signature_type="InvoiceReceived";
    Bitmap newBitmap;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    public Order_Deliver() {
    }

    public static Order_Deliver newInstance(String param1, String param2) {
        Order_Deliver fragment = new Order_Deliver();
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
        verifyStoragePermissions(getActivity());
        View view = inflater.inflate(R.layout.order_deliver, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        invoice_progress=(ProgressBar)view.findViewById(R.id.invoice_progress);
        invoice_progress.setVisibility(View.VISIBLE);
        _list = (ListView)view.findViewById(R.id.invoice_list);
        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView c = (TextView)view.findViewById(R.id.idi);
                ids = c.getText().toString();
                Rating();
                popup_listModels.clear();
            }
        });
        _list.setDivider(null);
        mylist=new ArrayList<InvoiceList>();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_getInvoice,
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
                                    String date = itemslist.getString("date");
                                    String name = itemslist.getString("name");
                                    String cust_id = itemslist.getString("cust_id");
                                    String mobile = itemslist.getString("mobile");
                                    String mode = itemslist.getString("mode");
                                    String gstin = itemslist.getString("gstin");

                                    String g_amount = itemslist.getString("g_amount");
                                    String t_amount = itemslist.getString("t_amount");

                                    String address = itemslist.getString("address");
                                    String total_amount = itemslist.getString("total_amount");
                                    String paid = itemslist.getString("paid");
                                    String dues = itemslist.getString("dues");

                                    InvoiceList invoiceList = new InvoiceList(id,date,name,cust_id,mobile,mode,address,total_amount,paid,dues,gstin,g_amount,t_amount);
                                    mylist.add(invoiceList);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            invoice_progress.setVisibility(View.GONE);
                            adapter = new InvoiceAdapter(mylist, getActivity());
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

                                Button pay_date_click, clear_sign;
                                myDialog.setContentView(R.layout.invoice_popup);
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

                                signaturePad=(SignaturePad)myDialog.findViewById(R.id.expense_signaturePad);
                                pay_date_click = (Button) myDialog.findViewById(R.id.pay_date_click);
                                pay_date_click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                                        if (addJpgSignatureToGallery(signatureBitmap)) {
                                            signs = getStringImage(newBitmap);

                                        } else {
                                            Toast.makeText(getActivity(), "Unable to catch the signature", Toast.LENGTH_SHORT).show();
                                        }

                                        Final_Submit_Data();
                                        myDialog.dismiss();
                                    }
                                });

                                clear_sign = (Button) myDialog.findViewById(R.id.clear_sign);
                                clear_sign.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        signaturePad.clear();
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
    public void Final_Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_receiveInvoice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            signaturePad.clear();
                            mylist.clear();
                            CallList();
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
                params.put("invoice_id", ids);
                params.put("photo", signs);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();

    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateToStr = signature_type+ "-" +format.format(today);
        System.out.println(dateToStr);
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("%s.jpg", dateToStr));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
            getPaths=photo.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}
