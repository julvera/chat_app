package com.jvera.chat_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jvera.chat_app.Constants;
import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;
import com.jvera.chat_app.database_access.CredsValidationInterface;
import com.jvera.chat_app.database_access.Database;
import com.jvera.chat_app.database_access.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfilSettingsFragment extends Fragment implements CredsValidationInterface {

    @BindView(com.jvera.chat_app.R.id.password_update_profil)
    EditText passwordUpdateProfil;
    public int base_fragment = R.id.base_fragment;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(com.jvera.chat_app.R.layout.fragment_profil_settings, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({com.jvera.chat_app.R.id.pseudo_update_btn})
    public void setOnClickLoginEvents(View v) {
        switch(v.getId()) {
            case R.id.pseudo_update_btn:
                // When Pseudo change btn is clicked , pseudo changed proceed
                String pass = passwordUpdateProfil.getText().toString();
                String invalidPassReason = Helper.checkPasswordValidity(pass);

                if (!"".equals(invalidPassReason)) {
                    passwordUpdateProfil.setError(invalidPassReason);
                } else {
                    Database.updateProfil(
                            getContext(),
                            DbHelper.generateCallback(this), //damned trick
                            Constants.API_URL_USERS_USERNAMES,
                            pass
                    );
                    Helper.createFragment(Helper.createUserListFragment(), base_fragment, "replace",getContext());
                }
                break;

            default:
                // Shouldn't get here
                break;
        }
    }

    @Override
    public void actionOnValidCredentials() {

    }
}
