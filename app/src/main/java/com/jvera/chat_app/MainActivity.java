package com.jvera.chat_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");
//        username.setBackgroundColor(getResources().getColor(R.color.something));

        Button button = findViewById(R.id.ckeck_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                EditText login = findViewById(R.id.login);
                EditText password = findViewById(R.id.password);
                String login_value = login.getText().toString();
                String password_value = password.getText().toString();
                Toast.makeText(
                        MainActivity.this, "Hi " + login_value, Toast.LENGTH_SHORT
                ).show();
            }
        });
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
