package com.jvera.chat_app.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.CredsValidationInterface;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserRegisterActivity extends AppCompatActivity implements CredsValidationInterface {

    final static private String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Log.i(TAG, "onCreate UserRegisterActivity");
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick({R.id.register_btn, R.id.login_btn})
    public void setOnClickUserRegisterEvents(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
                startLoginActivity();
                break;

            case R.id.register_btn:
                registerClickAction();
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    //slight overkill but .. just because we can
    private void startLoginActivity() {
        Helper.activityStarter(this, LoginActivity.class);
    }

    /** Basic check on credentials then call to DB to add*/
    private void registerClickAction() {
        String user = login.getText().toString();
        String pass = password.getEditText().getText().toString();
        String invalidUserReason = Helper.checkUsernameValidity(user);
        String invalidPassReason = Helper.checkPasswordValidity(pass);

        if (!"".equals(invalidUserReason)) {
            login.setError(invalidUserReason);
        } else if (!"".equals(invalidPassReason)) {
            password.setError(invalidPassReason);
        } else {
            Database.addCredentials(
                this,
                DbHelper.generateCallback(this), //damned trick
                Constants.API_URL_USERS_USERNAMES,
                user,
                pass
            );
        }
    }

    /** To do list of things if credentials are valid*/
    public void actionOnValidCredentials() {
        Helper.toastAnnounce(this, Constants.TXT_REGISTRATION_SUCCESSFUL);
        startLoginActivity();
    }
}
