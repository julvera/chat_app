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

    ArrayList<String> discussionsList = new ArrayList<>();
    ProgressDialog progDial;
    private int totalUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Log.i(TAG, "onCreate UserHomeActivity");
        ButterKnife.bind(this);

        StringRequest request = dbGetDiscussions();
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chat_with = discussionsList.get(position);
                startActivity(new Intent(UserHomeActivity.this, UserChatActivity.class));
            }
        });
    }

    private StringRequest dbGetDiscussions(){
        progDial = new ProgressDialog(this);
        progDial.setMessage("Loading...");
        progDial.show();
        Response.Listener<String> response_listener = new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                getContactDiscussions(s);
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
            Constants.API_URL_USERS_USERNAMES_JSON,
            response_listener,
            error_listener
        );
    }

    public void getContactDiscussions(String s){
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key;

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserDetails.username)) {
                    discussionsList.add(key);}
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
                this, android.R.layout.simple_list_item_1, discussionsList
            ));
        }
        progDial.dismiss();
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