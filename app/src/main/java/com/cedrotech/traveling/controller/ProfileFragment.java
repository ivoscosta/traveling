package com.cedrotech.traveling.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.model.User;
import com.cedrotech.traveling.util.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private TextView name, email;
    private View rootView;
    private ImageView profile_picture;
    private User user;
    private LinearLayout noInternet;
    private LinearLayout preloader;
    private FrameLayout contentProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); //usado para acessar os menus do fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        name = (TextView) rootView.findViewById(R.id.name);
        email = (TextView) rootView.findViewById(R.id.email);
        profile_picture = (ImageView) rootView.findViewById(R.id.profile_picture);
        noInternet = (LinearLayout) rootView.findViewById(R.id.no_internet);
        preloader = (LinearLayout) rootView.findViewById(R.id.progressBar);
        contentProfile = (FrameLayout) rootView.findViewById(R.id.contentProfile);

        noInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
        init();
        return rootView;
    }

    public void init() {
        if (Utils.isConnectInternet(getActivity()))
            initLoading();
        else {
            noInternet.setVisibility(View.VISIBLE);
            contentProfile.setVisibility(View.GONE);
        }
    }

    private void initLoading() {
        preloader.setVisibility(View.VISIBLE);
        contentProfile.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        getProfile();
    }

    private void finishLoading() {
        preloader.setVisibility(View.GONE);
        contentProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.action_search);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutFacebook();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
        Intent i = new Intent(getActivity(), SplashActivity.class);
        startActivity(i);
    }

    public void getProfile() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,picture.width(180).height(180),email");

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if (response.getRawResponse() != null) {
                            try {
                                JSONObject obj = new JSONObject(response.getRawResponse().toString());
                                user = new User();

                                //setando a foto de perfil
                                try {
                                    user.setPicture(obj.getJSONObject("picture").getJSONObject("data").getString("url"));

                                    Uri uri = Uri.parse(user.getPicture());
                                    Picasso.with(getActivity()).load(uri).into(profile_picture);
                                } catch (Exception e) {
                                    user.setPicture(null);
                                }

                                //setando o nome
                                try {
                                    user.setName(obj.getString("name"));
                                    name.setText(user.getName());
                                } catch (Exception e) {
                                    user.setName(null);
                                }

                                //setando o email
                                try {
                                    user.setEmail(obj.getString("email"));
                                    email.setText(user.getEmail());
                                } catch (Exception e) {
                                    user.setEmail(null);
                                }

                                finishLoading();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
        ).executeAsync();
    }

}