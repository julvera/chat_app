package com.jvera.chat_app;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static private String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) TextInputLayout password;
    @BindView(R.id.check_login_btn) Button check_login_btn;
    @BindView(R.id.register_btn) Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        ButterKnife.bind(this);
//        login.setBackgroundColor(getResources().getColor(R.color.some_red));

        check_login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        String login_value = login.getText().toString();
        String password_value = password.getEditText().getText().toString(); //IDE whining for no damn reason

        switch(v.getId()) {
            case R.id.check_login_btn:
                if(login_value.isEmpty() || password_value.isEmpty()){
                    TextView Error_pop = findViewById(R.id.Error_pop);
                    Error_pop.setVisibility(View.VISIBLE);
                    break;
                }
                Toast.makeText(
                    MainActivity.this, "Hi " + login_value, Toast.LENGTH_SHORT
                ).show();
                //
                //TODO: something
                //
                break;

            case R.id.register_btn:
                //
                //TODO: register logic
                //
                break;

            default:
                //should never get here
                break;
        }

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
