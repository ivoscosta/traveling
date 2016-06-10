package com.cedrotech.traveling.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cedrotech.traveling.util.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.cedrotech.traveling.R;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.viewpagerindicator.UnderlinePageIndicator;

public class SplashActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private TourPagerAdapter mTourPagerAdapter;
    private ViewPager mViewPager;
    private UnderlinePageIndicator mIndicator;

    private RelativeLayout splash;
    private RelativeLayout tour;

    Animation anim;

    private Tour1Fragment tour1Fragment;
    private Tour2Fragment tour2Fragment;
    private Tour3Fragment tour3Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            // Colocando o status bar transparent para versão de sdk >= 21
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        //requisitando permissões públicas e email no login com o Facebook
        loginButton.setReadPermissions("public_profile", "email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goToMain();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        mTourPagerAdapter = new TourPagerAdapter(getSupportFragmentManager());

        //Set the pager with an adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mTourPagerAdapter);

        // ViewPager Indicator
        mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setSelectedColor(getResources().getColor(R.color.color2));

        splash = (RelativeLayout) findViewById(R.id.splash);
        tour = (RelativeLayout) findViewById(R.id.tour);

        tour.setVisibility(View.INVISIBLE);

        // Fragmentos usados para o slideShow (ou tour) no app
        tour1Fragment = new Tour1Fragment();
        tour2Fragment = new Tour2Fragment();
        tour3Fragment = new Tour3Fragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isLoggedIn()) {
            goToMain();
        } else
            startAnimations();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.closeApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void goToMain() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }

    private void startAnimations() {
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.setVisibility(View.VISIBLE);
        iv.clearAnimation();
        iv.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                try {
                    Thread.sleep(1500);
                    splash.setVisibility(View.INVISIBLE);
                    tour.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class TourPagerAdapter extends FragmentPagerAdapter {

        public TourPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return tour1Fragment;
                case 1:
                    return tour2Fragment;
                case 2:
                    return tour3Fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}


