package com.example.tianyunchen.arvin_shopping;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import butterknife.OnClick;
import server.LeanCloudServer;

public class MainActivity extends BaseDrawerActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    final static String EXTRA_KEY_TABLE_NAME = "tableName";
    final static String EXTRA_VALUE_SHOES = "Shoes";
    final static String EXTRA_VALUE_APPAREL = "Apparel";
    final static String EXTRA_VALUE_ACCESSORIES = "Accessories";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (AVUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_category);
            Toast.makeText(this, "Click Your Favorite", Toast.LENGTH_LONG).show();
        }

    }



    @OnClick(R.id.image1)
    public void enterApparel() {

        startProductPreviewActivity(EXTRA_VALUE_APPAREL);
    }



    @OnClick(R.id.image2)
    public void enterAccessories() {

        startProductPreviewActivity(EXTRA_VALUE_ACCESSORIES);

    }



    @OnClick(R.id.image3)
    public void enterShoes() {

        startProductPreviewActivity(EXTRA_VALUE_SHOES);

    }



    private void startProductPreviewActivity(String category) {

        String genderType = getGenderType();
        Intent intent = new Intent(this, ProductPreviewActivity.class);
        intent.putExtra(EXTRA_KEY_TABLE_NAME, genderType + category);
        startActivity(intent);
    }

    //First try to get gender type from former activity. If it is not found, then turn to Leanclooud
    //This is to avoid get null from Leancloud (It took some time to update)
    private String getGenderType() {

        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            String genderType = user.getString(LeanCloudServer.AV_USER_GENDER_KEY);
            if (genderType != null) {
                return genderType;
            }
        }
        return getIntent().getStringExtra(SelectGenderActivity.EXTRA_KEY_GENDER_TYPE);

    }

}
