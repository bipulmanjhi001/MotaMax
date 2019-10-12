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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.motamax.model.DeliveryAdapter;
import com.motamax.model.DeliveryModel;
import com.motamax.model.InvoiceList;
import com.motamax.model.Popup_ListModel2;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Delivery extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    ListView products_list;
    ArrayList<Popup_ListModel2> popup_listModels;
    Popup_ListAdapter2 popup_listAdapter;
    String token,signs;
    String signature_type="Delivery",ids;
    String getPaths,data="";
    Bitmap newBitmap;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ListView _list;
    ArrayList<DeliveryModel> deliveryModels;
    Dialog myDialog;
    ProgressBar invoice_progress;
    DeliveryAdapter deliveryAdapter;
    SignaturePad signaturePad;
    String id,mrp,rate,quantity,discount,taxable_amount,gst_amount,amount,p_name,name;
    ArrayList<InvoiceList> mylist = new ArrayList<InvoiceList>();
    JSONObject jsonObject;

    public Delivery() {

    }
    public static Delivery newInstance(String param1, String param2) {
        Delivery fragment = new Delivery();
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
        View view = inflater.inflate(R.layout.payment_receive, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        invoice_progress=(ProgressBar)view.findViewById(R.id.pay_pro);
        _list = (ListView)view.findViewById(R.id.delivery_list);

        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView c = (TextView)view.findViewById(R.id.idis);
                ids = c.getText().toString();
                Confirm_Delivery();
                popup_listModels.clear();
            }
        });
        _list.setDivider(null);
        mylist=new ArrayList<InvoiceList>();
        popup_listModels=new ArrayList<Popup_ListModel2>();
        deliveryModels=new ArrayList<DeliveryModel>();
        CallList();
        myDialog = new Dialog(getActivity());
        jsonObject=new JSONObject();
        return view;
    }

    public void CallList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_deliveryList,
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
                                    String email = itemslist.getString("email");
                                    String gstin = itemslist.getString("gstin");
                                    String address = itemslist.getString("address");
                                    String g_amount = itemslist.getString("g_amount");
                                    String t_amount = itemslist.getString("t_amount");
                                    String total_amount = itemslist.getString("total_amount");
                                    String paid = itemslist.getString("paid");

                                    DeliveryModel deliveryModel=new DeliveryModel(id,date,name,cust_id,mobile,mode,email,gstin,address,g_amount,t_amount,total_amount,paid);
                                    deliveryModels.add(deliveryModel);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            invoice_progress.setVisibility(View.GONE);
                            deliveryAdapter = new DeliveryAdapter(deliveryModels, getActivity());
                            _list.setAdapter(deliveryAdapter);
                            deliveryAdapter.notifyDataSetChanged();
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
    public class Popup_ListAdapter2 extends BaseAdapter {
        private Context mContext;
        ArrayList<Popup_ListModel2> mylist = new ArrayList<Popup_ListModel2>();

        public Popup_ListAdapter2(ArrayList<Popup_ListModel2> itemArray, Context mContext) {
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
            private TextView id,rates, qtys, taxs, gsts, discounts, mrps,c_names,p_names;
            private CheckBox checkBox;
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
                    convertView = inflator.inflate(R.layout.popup_list2, null);
                    view.id = (TextView) convertView.findViewById(R.id.ids);
                    view.c_names = (TextView) convertView.findViewById(R.id.c_name);
                    view.p_names = (TextView) convertView.findViewById(R.id.p_name);
                    view.rates = (TextView) convertView.findViewById(R.id.rate);
                    view.qtys = (TextView) convertView.findViewById(R.id.qty);
                    view.taxs = (TextView) convertView.findViewById(R.id.tax);
                    view.gsts = (TextView) convertView.findViewById(R.id.gst);
                    view.discounts = (TextView) convertView.findViewById(R.id.discount);
                    view.mrps = (TextView) convertView.findViewById(R.id.mrp);
                    view.checkBox=(CheckBox)convertView.findViewById(R.id.get_id);
                    view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                try {
                                    jsonObject = new JSONObject();
                                    jsonObject.put("id", mylist.get(position).getId());
                                } catch (Exception e) {
                                }
                                if (data.length() > 5) {
                                    data = data.concat(jsonObject.toString());
                                    Log.d("data",data);
                                } else {
                                    data = jsonObject.toString();
                                    Log.d("data",data);
                                }
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
                view.id.setText(mylist.get(position).getId());
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
    public void Confirm_Delivery(){
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

                                    Popup_ListModel2 popup_listModel=new Popup_ListModel2(id,rate,quantity,taxable_amount,gst_amount,discount,mrp,name,p_name,false);
                                    popup_listModels.add(popup_listModel);
                                }

                                Button pay_date_click, clear_sign;
                                myDialog.setContentView(R.layout.invoice_popup);
                                myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                products_list=(ListView)myDialog.findViewById(R.id.products_list);
                                try {
                                    popup_listAdapter = new Popup_ListAdapter2(popup_listModels, getActivity());
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_delivery,
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
                params.put("products", data);
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
