package com.jvera.chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserHomeActivity extends AppCompatActivity {
    private static final String TAG = "Debug" ;
    @BindView(R.id.usersList) ListView usersList;
    @BindView(R.id.noUsersText) TextView noUsersText;

    ArrayList<String> discussions_list = new ArrayList<>();
    ProgressDialog prog_dial;
    private int totalUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Log.i(TAG, "onCreate UserHomeActivity");
        ButterKnife.bind(this);

        StringRequest request = db_get_discussions();
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chat_with = discussions_list.get(position);
                startActivity(new Intent(UserHomeActivity.this, UserChatActivity.class));
            }
        });
    }

    private StringRequest db_get_discussions(){
        prog_dial = new ProgressDialog(this);
        prog_dial.setMessage("Loading...");
        prog_dial.show();
        Response.Listener<String> response_listener = new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                get_contact_discussions(s);
            }
        };

        Response.ErrorListener error_listener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        };

        return new StringRequest(
            Request.Method.GET,
            Constants.api_url_users_usernames_json,
            response_listener,
            error_listener
        );
    }

    public void get_contact_discussions(String s){
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key;

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserDetails.username)) {discussions_list.add(key);}
                totalUsers++;
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, discussions_list
            ));
        }
        prog_dial.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart UserHomeActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume UserHomeActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause UserHomeActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop UserHomeActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy UserHomeActivity");
    }
}