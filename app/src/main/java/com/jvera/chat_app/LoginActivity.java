package com.jvera.chat_app;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jvera.chat_app.database_access.CredsValidationInterface;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements CredsValidationInterface {

    final static private String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;
    @BindView(R.id.Error_pop) TextView errorPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate");
        ButterKnife.bind(this);
        if (!UserDetails.username.equals("")) {
            login.setText(UserDetails.username);
        }
    }

    @OnClick({R.id.register_btn, R.id.login_btn, R.id.guest_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                verifyLoginValidity();
                break;

            case R.id.register_btn:
                startActivity(UserRegisterActivity.class);
                break;

            case R.id.guest_btn:
                startActivity(GuestRegisterActivity.class);
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    //slight overkill but .. just because we can
    private void startActivity(Class newActivityClass) {
        Helper.activityStarter(this, newActivityClass);
    }

    /** Checks basic user and pass validity then calls DB to see the user exists*/
    private void verifyLoginValidity() {
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString(); //IDE whining for no damn reason

        if(user.isEmpty() || pass.isEmpty()){
            errorPop.setVisibility(View.VISIBLE);
        }
        else {
            errorPop.setVisibility(View.INVISIBLE); // For appearance on 2nd attempt with usr & pass
            Database.verifyUserCredentials(
                this,
                DbHelper.generateCallback(this), //damned trick
                user,
                pass
            );
        }
    }

    /** To do list of things if credentials are validated*/
    public void actionOnValidCredentials() {
        startActivity(UserHomeActivity.class);
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