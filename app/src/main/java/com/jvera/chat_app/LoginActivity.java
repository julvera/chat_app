package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    final static private String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn, R.id.guest_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                loginClickAction();
                break;

            case R.id.register_btn:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;

            case R.id.guest_btn:
                startActivity(new Intent(this, GuestRegisterActivity.class));
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private void loginClickAction() {
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString(); //IDE whining for no damn reason
        TextView errorPop = findViewById(R.id.Error_pop);

        if(user.isEmpty() || pass.isEmpty()){
            errorPop.setVisibility(View.VISIBLE);
        }
        else {
            errorPop.setVisibility(View.INVISIBLE); // For appearance on 2nd attempt with usr & pass
            StringRequest request = dbCheckCredentials(user, pass);
            RequestQueue rQueue = Volley.newRequestQueue(this);
            rQueue.add(request);
        }
    }

    private StringRequest dbCheckCredentials(final String user, final String pass) {
        final ProgressDialog progDial = new ProgressDialog(this);
        progDial.setMessage("Loading...");
        progDial.show();

        Response.Listener<String> response_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null")) {
                    Helper.toastError(LoginActivity.this, Constants.TXT_ERROR_USER_NOT_FOUND);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            Helper.toastError(LoginActivity.this, Constants.TXT_ERROR_USER_NOT_FOUND);
                        } else if (obj.getJSONObject(user).getJSONObject("profile").getString("password").equals(pass)) {
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                        } else {
                            Helper.toastError(LoginActivity.this, Constants.TXT_ERROR_INCORRECT_PASSWORD);
                        }
                    } catch (JSONException e) {e.printStackTrace();}
                }
                progDial.dismiss();
            }
        };

        Response.ErrorListener error_listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progDial.dismiss();
            }
        };

        return new StringRequest(
            Request.Method.GET,
            Constants.API_URL_USERS_USERNAMES_JSON,
            response_listener,
            error_listener
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}