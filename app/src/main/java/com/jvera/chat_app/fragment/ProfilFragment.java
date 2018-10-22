package com.jvera.chat_app.fragment;

import android.os.Bundle;
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

    @BindView(R.id.password_profile) TextView passwordProfil;

    public int base_fragment = R.id.base_fragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ButterKnife.bind(this,view);
        passwordProfil.setText(UserDetails.password);
        return view;
    }

    @OnClick({R.id.password_change_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.password_change_btn:
                // When Pseudo change btn is clicked , pseudo changed proceed
                Helper.createFragment(Helper.createProfileSettingsFragment(), base_fragment, "replace",getContext());

                Helper.toastAnnounce(getContext(),"Change Pseudo button selected");

                break;

            default:
                // Shouldn't get here
                break;
        }
    }


    }