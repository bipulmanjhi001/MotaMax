package com.motamax.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.drawer.Statements;
import com.motamax.drawer.Home;
import com.motamax.drawer.Profile;
import com.motamax.drawer.View_Ledger;
import com.motamax.drawer.Order;
import com.motamax.drawer.Credit_Note;
import com.motamax.drawer.View_Order;
import com.motamax.model.DispatchTab;
import com.motamax.model.InvoiceTab;
import com.motamax.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Class fragmentClass;
    DrawerLayout drawer;
    Fragment fragment = null;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    String name, email, token;
    private View navHeader;
    NavigationView navigationView;
    TextView p_name,p_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            token = bundle.getString("token");
            email = bundle.getString("email");
        } else {
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            name = sp.getString("keyusername", "");
            token = sp.getString("keyid", "");
            email = sp.getString("email", "");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTitle("Profile");
                Fragment fragment = new Profile();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard, fragment);
                ft.commitAllowingStateLoss();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        p_name=(TextView)navHeader.findViewById(R.id.p_name);
        p_name.setText(name);
        p_email=(TextView)navHeader.findViewById(R.id.p_email);
        p_email.setText(email);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, new Home()).commit();
        setTitle("Home");

        InvoiceNotification();
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Dashboard.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {

            fragmentClass = Home.class;
            setTitle("Home");
        }
        else if (id == R.id.nav_credit) {

            fragmentClass = Credit_Note.class;
            setTitle("Credit Note");

        } else if (id == R.id.nav_gallery) {

            fragmentClass = Order.class;
            setTitle("Order");

        } else if (id == R.id.nav_slideshow) {

            fragmentClass = View_Order.class;
            setTitle("View Order");

        } else if (id == R.id.nav_tools) {

            fragmentClass = View_Ledger.class;
            setTitle("Monthly Statement");

        } else if (id == R.id.nav_deliver) {

            fragmentClass = InvoiceTab.class;
            setTitle("Invoice");

        } else if (id == R.id.nav_receive) {

            fragmentClass = DispatchTab.class;
            setTitle("Dispatch");

        } else if (id == R.id.nav_cust) {

            fragmentClass = Statements.class;
            setTitle("Advance Statement");
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void InvoiceNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_invoiceNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String message=obj.getString("message");
                            if(message.equals("true")) {
                                navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot);
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
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        DispatchNotofication();
    }

    public void DispatchNotofication() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_dispatchNotofication,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String message=obj.getString("message");
                            if(message.equals("true")) {
                                navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot);
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
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
