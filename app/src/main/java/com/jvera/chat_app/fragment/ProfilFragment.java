package com.jvera.chat_app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.UserDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfilFragment extends Fragment {

    @BindView(R.id.pseudo_profile) TextView pseudoProfile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ButterKnife.bind(this,view);
        pseudoProfile.setText(UserDetails.username);
        return view;
    }

    @OnClick({R.id.pseudo_change_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.pseudo_change_btn:
                // When Pseudo change btn is clicked , pseudo changed proceed
                Helper.toastAnnounce(getContext(),"Change Pseudo button selected");

                break;

            default:
                // Shouldn't get here
                break;
        }
    }
}