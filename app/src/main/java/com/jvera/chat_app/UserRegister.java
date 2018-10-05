package com.jvera.chat_app;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserRegister extends AppCompatActivity {

    final static private String TAG = Login.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Log.i(TAG, "onCreate UserRegister");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn})
    public void setOnClickUserRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                startActivity(new Intent(UserRegister.this, Login.class));
                break;

            case R.id.register_btn:
                if (register_click_action()) {
                    startActivity(new Intent(UserRegister.this, Login.class));
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    private boolean register_click_action() {
        boolean ret = false;
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString();
        String invalid_user_reason = Helper.check_username_validity(user);
        String invalid_pass_reason = Helper.check_password_validity(user);

        if (!"".equals(invalid_user_reason)) {
            login.setError(invalid_user_reason);
        } else if (!"".equals(invalid_pass_reason)) {
            password.setError(invalid_pass_reason);
        } else {
            StringRequest request = Helper.db_add_credentials(
                UserRegister.this,
                Constants.api_url_users_usernames,
                user,
                pass
            );
            RequestQueue rQueue = Volley.newRequestQueue(UserRegister.this);
            rQueue.add(request);
            ret = true;
        }
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart UserRegister");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume UserRegister");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause UserRegister");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop UserRegister");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy UserRegister");
    }
}
