package com.example.tianyunchen.arvin_shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import adapter.PagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.ScanImageFragment;
import fragment.TestFragment;
import server.LeanCloudServer;
import utility.ImageLoader;
import view.RevealBackgroundView;

/**
 * Created by tianyunchen on 16/3/9.
 */
public class UserPro extends BaseDrawerActivity implements RevealBackgroundView.OnstateChangeListerner {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    static final String LOG_TAG = UserPro.class.getSimpleName();

    static final int REQUEST_SELECT_AVATAR = 1;

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private List<Fragment> list = new ArrayList<Fragment>();
    @Bind(R.id.vRevealBackground)
    RevealBackgroundView mRevealBackgroundView;

    @Bind(R.id.userInformation)
    ViewPager mViewpager;

    @Bind(R.id.layout)
    View mUserProfileRoot;

    @Bind(R.id.tlUserProfileTabs)
    TabLayout tUserProfileTabs;

    @Bind(R.id.user_avatar_roundImgView)
    RoundedImageView mAvatarView;

    @Bind(R.id.user_user_name_textView)
    TextView mUserNameTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        setUpRevealBackground();
        setupTabs();

    }



    @Override
    public void onStateChange(int state) {

//        setUserAvatarFromImgPath();

        if (mRevealBackgroundView.STATE_FINISHED == state) {
            mUserProfileRoot.setVisibility(View.VISIBLE);
            tUserProfileTabs.setVisibility(View.VISIBLE);
            mViewpager.setVisibility(View.VISIBLE);

            animateUserProfileOptions();
            animateUserProfileHeader();

        } else {
            mViewpager.setVisibility(View.INVISIBLE);

            mUserProfileRoot.setVisibility(View.INVISIBLE);
            tUserProfileTabs.setVisibility(View.INVISIBLE);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_SELECT_AVATAR && resultCode == RESULT_OK) {
            String imgAbsPath = data.getStringExtra(ScanImageFragment.KEY_IMAGE_ABS_PATH);
            if (imgAbsPath != null) {
                ImageLoader.getInstance().loadImageWithPath(imgAbsPath, mAvatarView);
                setHeaderAvatarWithImgPath(imgAbsPath);

                //Upload user avatar online
                Log.d(LOG_TAG, "Start to save the new avatar");
                LeanCloudServer.getInstance().uploadUserAvatar(imgAbsPath);

            } else {
                Log.e(LOG_TAG, "ImgAbsPath return null");
            }
        }
    }






    @OnClick(R.id.user_avatar_roundImgView)
    public void setAvatar() {

        Intent intent = new Intent(UserPro.this, ScanImageActivity.class);
//        startActivity(intent);
//        finish();
        startActivityForResult(intent, REQUEST_SELECT_AVATAR);

    }



    @Override
    public void getUserNameAndAvatarDone(String username, String imgUrl) {

        super.getUserNameAndAvatarDone(username, imgUrl);

        ImageLoader.getInstance().loadImageWithUrl(imgUrl, mAvatarView);
        mUserNameTextView.setText(username);

    }



    public static void startUserProfileFromLocation(int[] startLocation, Activity startActivity) {

        Intent intent = new Intent(startActivity, UserPro.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startLocation);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity.startActivity(intent);

    }



    private void setupTabs() {

        TestFragment testFramgnert1 = new TestFragment();
        TestFragment testFramgnert2 = new TestFragment();
        TestFragment testFramgnert3 = new TestFragment();

        list.add(testFramgnert1);
        list.add(testFramgnert2);
        list.add(testFramgnert3);

        List<String> listtilte = new ArrayList<String>();
        listtilte.add("Billing Address");
        listtilte.add("History");
        listtilte.add("Favourite");

        FragmentPagerAdapter fragmentPagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), list, listtilte);
        mViewpager.setAdapter(fragmentPagerAdapter);
        tUserProfileTabs.setupWithViewPager(mViewpager);

    }



    private void setUpRevealBackground() {

        mRevealBackgroundView.setOnStateChangeListener(this);
        final int[] startLocation = new int[2];
        mRevealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                mRevealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRevealBackgroundView.startFromLocation(startLocation);
                return true;
            }
        });

    }



    private void animateUserProfileHeader() {

        mUserProfileRoot.setTranslationY(-mUserProfileRoot.getHeight());
        mUserProfileRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
    }



    private void animateUserProfileOptions() {

        tUserProfileTabs.setTranslationY(-tUserProfileTabs.getHeight());
        tUserProfileTabs.animate().translationY(0).setDuration(300).setStartDelay(300).setInterpolator(INTERPOLATOR);
    }
}
