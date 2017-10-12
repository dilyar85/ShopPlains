package com.example.tianyunchen.arvin_shopping;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.HashSet;

import adapter.ProductPreviewAdapter;
import bean.ProductBean;
import butterknife.Bind;
import server.LeanCloudServer;

/**
 * Created by Dilyar on 5/21/16.
 */
public class ProductPreviewActivity extends BaseDrawerActivity implements LeanCloudServer.TableQueryListener {

    @Bind(R.id.storeitems_listview)
    ListView mListView;

    static final int DOWNLOAD_PRODUCT_INFO_DONE = 0;

    final static String EXTRA_PRODUCT_OBJECT_ID = "product_object_id_extra";
    final static String EXTRA_PRODUCT_NAME = "product_object_name_extra";
    final static String EXTRA_PRODUCT_PRICE = "product_object_price_extra";

    //Leancloud tables' names
    final static String QUERY_TABLE_NAME = "AdidasShoes";
    final static String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    final static String COLUMN_ITEM_TITLE = "item_title";
    final static String COLUMN_ITEM_PRICE = "price";

    private ProductPreviewAdapter mProductPreviewAdapter;
    private ArrayList<ProductBean> mProductBeans;

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_storeitems);
        initData();
        setListViewClickEvent();
    }



    //Get items information from AV Cloud.
    private void initData() {

        mProgressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.loading));

        LeanCloudServer.getInstance().setTableQueryListener(this);

        //Get data from AV Cloud service
        new Thread() {

            @Override
            public void run() {

                String tableName = getIntent().getStringExtra(MainActivity.EXTRA_KEY_TABLE_NAME);
                LeanCloudServer.getInstance().downloadDataFromTable(tableName);


            }
        }.start();

    }

    @Override
    public void getProductBeansDone(HashSet<ProductBean> beans) {

        mProductBeans = new ArrayList<>(beans);
        mHandler.sendEmptyMessage(DOWNLOAD_PRODUCT_INFO_DONE);
    }



    /**
     * Check the AVObject integrity according to its class name.
     */

    private boolean hasIntegrity(AVObject avObject, String type) {

        if (type.equals("shoes")) {
            String name = avObject.getString("ItemName");
            String price = avObject.getString("ItemPrice");
            AVFile thumbnailFile = avObject.getAVFile("Thumbnail");
            return (name != null && price != null && thumbnailFile != null);
        }

        return false;

    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == DOWNLOAD_PRODUCT_INFO_DONE) {
                mProgressDialog.dismiss();
                passDataToView();
            }
        }
    };



    private void passDataToView() {

        if (mProductBeans == null || mProductBeans.size() <= 0) {
            Toast.makeText(this, "无法加载商品信息,请重试", Toast.LENGTH_SHORT).show();

        } else {
            mProductPreviewAdapter = new ProductPreviewAdapter(this, mProductBeans);
            mListView.setAdapter(mProductPreviewAdapter);
        }

    }



    private void setListViewClickEvent() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mProductBeans != null) {
                    String productName = mProductBeans.get(position).getName();
                    String productPrice = mProductBeans.get(position).getPrice();
                    String objectID = mProductBeans.get(position).getObjectId();

                    Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                    intent.putExtra(MainActivity.EXTRA_KEY_TABLE_NAME,
                            getIntent().getStringExtra(MainActivity.EXTRA_KEY_TABLE_NAME));
                    intent.putExtra(EXTRA_PRODUCT_OBJECT_ID, objectID);
                    intent.putExtra(EXTRA_PRODUCT_NAME, productName);
                    intent.putExtra(EXTRA_PRODUCT_PRICE, productPrice);
                    startActivity(intent);
                }
            }
        });
    }




}
