package com.example.tianyunchen.arvin_shopping;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import server.LeanCloudServer;
import utility.ImageLoader;

/**
 * Created by tianyunchen on 16/3/15.
 */
public class BaseDrawerActivity extends BaseActivity implements LeanCloudServer.CallbackListener {


    static final String LOG_TAG = BaseDrawerActivity.class.getSimpleName();
    @Bind(R.id.drawer_drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_navigationView)
    NavigationView navigationView;

    private TextView mHeaderUserTextView;

    private RoundedImageView mHeaderUserAvatarView;






    @Override
    public void setContentView(int layoutResID) {

        super.setContentViewWithoutInject(R.layout.acitivty_base_drawer);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.drawer_frameLayout);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);

        ButterKnife.bind(this);
        initViews();




    }

    @Override
    public void onResume() {
        super.onResume();

        LeanCloudServer.getInstance().setCallbackListener(this);
        LeanCloudServer.getInstance().downloadUserInfoFromCloud();

    }

    @Override
    protected void initViews() {

        super.initViews();

        setUpToolBar();
        initHeader();
        initMenuItem();

    }









    @Override
    public void getDataDone(List<AVObject> data) {
    }









    @Override
    public void getUserNameAndAvatarDone(String username, String imgUrl) {

        if (mHeaderUserAvatarView != null && mHeaderUserTextView != null) {
            mHeaderUserTextView.setText(username);
            ImageLoader.getInstance().loadImageWithUrl(imgUrl, mHeaderUserAvatarView);
            Log.d(LOG_TAG, "Finish setting drawer avatar: " + imgUrl);
        }


    }


     void setHeaderAvatarWithImgPath(String imgPath) {

        ImageLoader.getInstance().loadImageWithPath(imgPath, mHeaderUserAvatarView);

    }






    private void initHeader() {

        View headView = navigationView.getHeaderView(0);

        mHeaderUserTextView = (TextView) headView.findViewById(R.id.menu_header_user_name);
        mHeaderUserAvatarView = (RoundedImageView) headView.findViewById(R.id.menu_header_user_avatar);

        headView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int[] location = new int[2];
                UserPro.startUserProfileFromLocation(location, BaseDrawerActivity.this);
                overridePendingTransition(0, 0);
            }
        });
    }



    private void initMenuItem() {

        Menu menu = navigationView.getMenu();
        MenuItem signOutItem = menu.findItem(R.id.menu_sign_out);
        MenuItem changeGenderItem = menu.findItem(R.id.menu_change_gender);

        signOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AVUser.logOut();
                if (AVUser.getCurrentUser() == null) {
                    startActivity(new Intent(BaseDrawerActivity.this, LogInActivity.class));
                    finish();
                    return true;
                } else {
                    Log.e(LOG_TAG, "用户退出登录失败");
                    return false;
                }
            }
        });

        changeGenderItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(BaseDrawerActivity.this, SelectGenderActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }




    public void onFlobaMenuHeaderClick(final View view) {

        drawerLayout.closeDrawer(Gravity.LEFT);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;

            }
        }, 200);
    }
}
