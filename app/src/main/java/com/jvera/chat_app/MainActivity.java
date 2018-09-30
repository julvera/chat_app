package com.jvera.chat_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @BindView(R.id.password) EditText password;
    @BindView(R.id.check_login) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        ButterKnife.bind(this);
        login.setBackgroundColor(getResources().getColor(R.color.something));
        button.setOnClickListener(this);
        password.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        String login_value = login.getText().toString();
        String password_value = password.getText().toString();

        switch(v.getId()) {
            case R.id.check_login:
                if(login_value.isEmpty() || password_value.isEmpty()){
                    TextView Error_pop = findViewById(R.id.Error_pop);
                    Error_pop.setVisibility(View.VISIBLE);
                    break;
                }
                Toast.makeText(
                    MainActivity.this,
                    "Hi " + login_value + "sweet password " + password_value,
                    Toast.LENGTH_SHORT
                ).show();
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
