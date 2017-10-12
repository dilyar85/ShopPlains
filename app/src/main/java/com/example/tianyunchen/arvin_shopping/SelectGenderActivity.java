package com.example.tianyunchen.arvin_shopping;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import butterknife.OnClick;
import server.LeanCloudServer;

public class SelectGenderActivity extends BaseDrawerActivity {

    public static final String LOG_TAG = SelectGenderActivity.class.getSimpleName();

    final static String EXTRA_KEY_GENDER_TYPE = "keyGender";
    final static String GENDER_MEN = "Men";
    final static String GENDER_WOMEN = "Women";
    final static String GENDER_KIDS = "Kids";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (AVUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Who are you?", Toast.LENGTH_SHORT).show();
        }

    }



    @OnClick(R.id.men_icon_imageview)
    public void enterMenCatogory() {

        startCategoryActivity(GENDER_MEN);
    }



    @OnClick(R.id.women_icon_imageview)
    public void enterWomenCatogory() {

        startCategoryActivity(GENDER_WOMEN);
    }



    @OnClick(R.id.kids_icon_imageview)
    public void enterKidsCatogory() {

        startCategoryActivity(GENDER_KIDS);
    }



    private void startCategoryActivity(String genderValues) {

        LeanCloudServer.getInstance().updateUserGender(genderValues);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_KEY_GENDER_TYPE, genderValues);
        startActivity(intent);
        finishAffinity();
    }

}
