package com.jvera.chat_app.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.activities.UserChatActivity;
import com.jvera.chat_app.models.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserListFragment extends Fragment {
    private static final String TAG = "Debug" ;
    @BindView(R.id.usersList) ListView usersList;
    @BindView(R.id.noUsersText) TextView noUsersText;

    ArrayList<String> discussionsList = new ArrayList<>();
    ProgressDialog progDial;
    private int totalUsers = 0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userlist, container, false);
        Log.i(TAG, "onCreate UserListFragment");
        ButterKnife.bind(this, view);

        StringRequest request = dbGetDiscussions();
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chat_with = discussionsList.get(position);
                Helper.activityStarter(getContext(), UserChatActivity.class);
            }
        });
        return view;
    }

    private StringRequest dbGetDiscussions(){
        progDial = new ProgressDialog(getActivity());
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
                    getActivity(), android.R.layout.simple_list_item_1, discussionsList
            ));
        }
        progDial.dismiss();
    }

}