package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Register extends AppCompatActivity {

    final static private String TAG = Login.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;
    @BindView(R.id.login_btn) TextView login_btn;
    @BindView(R.id.register_btn) Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.i(TAG, "onCreate Register");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn})
    public void setOnClickRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                startActivity(new Intent(Register.this, Login.class));
                break;

            case R.id.register_btn:
                register_click_action();
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private void register_click_action() {
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString();

        if (user.equals("")) {
            login.setError(constants.txt_error_field_required);
        } else if(pass.equals("")){
            password.setError(constants.txt_error_field_required);
        } else if (!user.matches("[A-Za-z0-9]+")) {
            login.setError(constants.txt_error_alpha_or_number_only);
        } else if (user.length() < 5) {
            login.setError(constants.txt_error_short_username);
        } else if (pass.length() < 5) {
            password.setError(constants.txt_error_short_password);
        } else {
            StringRequest request = db_add_credentials(user, pass);
            RequestQueue rQueue = Volley.newRequestQueue(Register.this);
            rQueue.add(request);
        }
    }

    private StringRequest db_add_credentials(final String user, final String pass){
        final ProgressDialog prog_dial = new ProgressDialog(Register.this);
        prog_dial.setMessage("Loading...");
        prog_dial.show();

        Response.Listener<String> response_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase(constants.api_url_users);

                if (s.equals("null")) {
                    reference.child(user).child("password").setValue(pass);
                    helper.toast_error(Register.this, constants.txt_registration_successful);
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            reference.child(user).child("password").setValue(pass);
                            helper.toast_error(Register.this, constants.txt_registration_successful);
                            startActivity(new Intent(Register.this, Login.class)); // if registration successful come back to Login Page

                        } else {
                            helper.toast_error(Register.this, constants.txt_error_user_exists);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                prog_dial.dismiss();
            }
        };

        Response.ErrorListener error_listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                prog_dial.dismiss();
            }
        };

        return new StringRequest(
            Request.Method.GET,
            constants.api_url_users_json,
            response_listener,
            error_listener
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart Register");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume Register");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause Register");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop Register");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy Register");
    }

}
