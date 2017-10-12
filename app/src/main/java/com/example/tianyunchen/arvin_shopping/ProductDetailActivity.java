package com.example.tianyunchen.arvin_shopping;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewPagerAdapter;
import animation.ScalePagerTransformer;
import bean.ProductionDetailBean;
import butterknife.Bind;
import butterknife.OnClick;
import server.LeanCloudServer;

/**
 * Created by tianyunchen on 16/4/13.
 */
public class ProductDetailActivity extends BaseDrawerActivity implements LeanCloudServer.CallbackListener {
    @Bind(R.id.production_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.product_detail_name_textView)
    TextView mNameTextView;
    @Bind(R.id.product_detail_price_textView)
    TextView mPriceTextView;
    @Bind(R.id.detail_activity_count_textview)
    TextView mCountTextView;

    final static int MESSAGE_LOAD_DETAIL_IMAGE_URLS_DONE = 0;

    private ArrayList<ProductionDetailBean> mProductionDetailBeen;

    private ArrayList<String> mDetailImageUrls = new ArrayList<>();

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initView();
        initData();

    }



    private void initView() {

        mViewPager.setPageTransformer(true, new ScalePagerTransformer());
        mNameTextView.setText(getIntent().getStringExtra(ProductPreviewActivity.EXTRA_PRODUCT_NAME));
        mPriceTextView.setText(getIntent().getStringExtra(ProductPreviewActivity.EXTRA_PRODUCT_PRICE));
    }



    private void initData() {

//        mProductionDetailBeen = new ArrayList<>();
//        LeanCloudServer.getInstance()
//                .setQuery("ProductsnDetail").getDataByRequirement(this,
//                "produtsname", "Tipped Icon Polo");

        mProgressDialog = ProgressDialog.show(this, null, "正在加载");

        new Thread() {

            @Override
            public void run() {

                //Get the object using extra, and then get other detail images from AV Cloud.
                final String avObjectId = getIntent().getStringExtra(ProductPreviewActivity.EXTRA_PRODUCT_OBJECT_ID);
                AVQuery<AVObject> query = new AVQuery<>(getIntent().getStringExtra(MainActivity.EXTRA_KEY_TABLE_NAME));
                query.getInBackground(avObjectId, new GetCallback<AVObject>() {

                            @Override
                            public void done(AVObject avObject, AVException e) {

                                if (e == null) {
                                    int detailImageCount = avObject.getInt("detail_img_count");
                                    if (detailImageCount > 0) {
                                        for (int i = 1; i <= detailImageCount; i++) {
                                            mDetailImageUrls.add(avObject.getString("detail_img_url_" + i));
                                        }
                                    } else {
                                        mDetailImageUrls.add(avObject.getString("thumbnail_url"));
                                    }
                                    mHandler.sendEmptyMessage(MESSAGE_LOAD_DETAIL_IMAGE_URLS_DONE);
                                }
                            }
                        }
                );
            }
        }.start();
    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MESSAGE_LOAD_DETAIL_IMAGE_URLS_DONE) {
                passDataToView();
                mProgressDialog.dismiss();
            }
        }
    };



    private void passDataToView() {

        mViewPager.setAdapter(new ViewPagerAdapter(this, mDetailImageUrls));

    }



    @Override
    public void getDataDone(List<AVObject> data) {
//
//        Log.d("DetailAcitivty"
//                , data.size() + "");
//        for (int i = 0; i < data.size(); i++) {
//            String productname = data.get(i).getString("productsname");
//            String storename = data.get(i).getString("storename");
//            AVFile imageaddress = data.get(i).getAVFile("productimage");
//            ProductionDetailBean productionDetailBean = new ProductionDetailBean(storename, productname, "0", "0",
//                    imageaddress.getUrl());
//            mProductionDetailBeen.add(productionDetailBean);
//        }
////        mViewPager.setAdapter(new ViewPagerAdapter(mProductionDetailBeen, this));
//        mViewPager.setOffscreenPageLimit(mProductionDetailBeen.size());

    }



    @OnClick(R.id.detail_activity_minus_button)
    public void minusButton() {
        int count = Integer.parseInt(mCountTextView.getText().toString());
        count--;
        if (count < 0)
            count = 0;
        mCountTextView.setText(count + "");

    }
    @OnClick(R.id.detail_activity_plus_button)
    public void plusButton() {
        int count = Integer.parseInt(mCountTextView.getText().toString());
        count++;
        mCountTextView.setText(count + "");

    }
}
