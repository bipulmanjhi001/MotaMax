package com.motamax.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.motamax.R;
import com.motamax.api.URL;
import com.motamax.model.ConnectivityReceiver;
import com.motamax.model.PrefManager;
import com.motamax.model.SharedPrefManager;
import com.motamax.model.User;
import com.motamax.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText UserView;
    private EditText mPasswordView;
    String username,password;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_BRANCH_ID= "branch_id";
    private PrefManager prefManager;
    String tokens,branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserView = (EditText) findViewById(R.id.user_id);
        mPasswordView = (EditText) findViewById(R.id.password);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        branch_id= prefs.getString(KEY_BRANCH_ID, null);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            try {
                finish();
                startActivity(new Intent(this, Dashboard.class));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack ( boolean isConnected){
        String message;
        int color;
        if (isConnected) {
            attemptLogin();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }
    private void attemptLogin() {
        UserView.setError(null);
        mPasswordView.setError(null);
        username = UserView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            UserView.setError(getString(R.string.error_field_required));
            focusView = UserView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            Authenticate();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void Authenticate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("user");
                                String token=userJson.getString("token");
                                String name=userJson.getString("name");
                                String email=userJson.getString("email");

                                User user = new User(
                                        userJson.getString("token"),
                                        userJson.getString("mobile"),
                                        userJson.getString("user_id"),
                                        userJson.getString("name"),
                                        userJson.getString("email")
                                );

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("name", name);
                                intent.putExtra("token", token);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();

                            } else if(!obj.getBoolean("status")) {

                                String error=obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", username);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Login.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
