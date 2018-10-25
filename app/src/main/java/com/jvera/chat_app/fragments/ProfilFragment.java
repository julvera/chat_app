package com.jvera.chat_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.models.UserDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfilFragment extends Fragment {

    @BindView(R.id.password_profile) TextView usernameProfile;
    public int base_fragment = R.id.base_fragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ButterKnife.bind(this,view);
        usernameProfile.setText(UserDetails.username);
        return view;
    }

    @OnClick({R.id.password_change_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.password_change_btn:
                // When Pseudo change btn is clicked , pseudo changed proceed
                Helper.createFragment(
                    Helper.createProfileSettingsFragment(),
                    base_fragment,
                    "replace",
                    getContext()
                );
                break;

            default:
                // Shouldn't get here
                break;
        }
    }
}