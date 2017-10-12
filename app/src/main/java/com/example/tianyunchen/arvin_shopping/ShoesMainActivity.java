//package com.example.tianyunchen.arvin_shopping;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.LinearLayout;
//
//import butterknife.Bind;
//import butterknife.OnClick;
//
///**
// * Created by Dilyar on 5/29/16.
// */
//public class ShoesMainActivity extends BaseDrawerActivity {
//    @Bind(R.id.shoes_original_layout)
//    LinearLayout mLayout;
//
//    final static String EXTRA_KEY_TABLE_NAME = "tableName";
//    final static int ORIGINAL = 1;
//    final static int SOCCER = 2;
//    final static int BASKETBNALL = 3;
//    final static int RUNNING = 4;
//    final static int SLIDES = 5;
//    final static int BASEBALL = 6;
//    final static int TENNIS = 7;
//    final static int OUTDOOR = 8;
//    final static int CUSTOMIZE = 9;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_shoes_main);
//
//    }
//
//    @OnClick(R.id.shoes_original_layout)
//    public void clickOriginal() {
//
//        startActivityWithTableName("AdidasShoes");
//    }
//
//    @OnClick(R.id.shoes_soccer_layout)
//    public void clickSoccer() {
//        startActivityWithTableName("AdidasShoesSoccer");
//    }
//
//    @OnClick(R.id.shoes_basketball_layout)
//    public void clickBaskeball() {
//        startActivityWithTableName("AdidasShoesBasketball");
//
//    }
//
//    @OnClick(R.id.shoes_running_layout)
//    public void clickRunning() {
//        startActivityWithTableName("AdidasShoesRunning");
//    }
//
//    @OnClick(R.id.shoes_slides_layout)
//    public void clickSlides() {
//        startActivityWithTableName("AdidasShoesSlides");
//    }
//    @OnClick(R.id.shoes_baseball_layout)
//    public void clickBaseball() {
//        startActivityWithTableName("AdidasShoesBaseball");
//    }
//
//
//    private void startActivityWithTableName(String tableName) {
//        Intent intent = new Intent(this, ProductPreviewActivity.class);
//        intent.putExtra(EXTRA_KEY_TABLE_NAME, tableName);
//        startActivity(intent);
//    }
//
//}
