package com.example.tianyunchen.arvin_shopping;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import butterknife.ButterKnife;
import fragment.ScanImageFragment;

/**
 * Created by Dilyar on 4/9/16.
 */
public class ScanImageActivity extends FragmentActivity {



    public static final String LOG_TAG = SignUpActivity.class.getSimpleName();
    public static final String SCAN_IMAGE_FRAGMENT_TAG = "SIFT";
    public static final String SELECT_IMAGE_FRAGMENT_TAG = "SIFT";








    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_image_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.scan_image_container, new ScanImageFragment(), SCAN_IMAGE_FRAGMENT_TAG)
                    .commit();
        }


    }

    public void replaceFragment(Fragment newFragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.scan_image_container, newFragment, SELECT_IMAGE_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }

    //Pass permission to the Fragment
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }



}
