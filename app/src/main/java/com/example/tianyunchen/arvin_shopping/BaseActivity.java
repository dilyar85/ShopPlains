package com.example.tianyunchen.arvin_shopping;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tianyunchen on 16/3/10.
 */
public class BaseActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//    @Bind(R.id.logo)
//    ImageView logo
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initViews();
    }

    protected  void initViews()
    {
        ButterKnife.bind(this);
        setUpToolBar();
    }

    protected  void setUpToolBar()
    {
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }

    public void setContentViewWithoutInject(int layoutResID)
    {
        super.setContentView(layoutResID);
    }

  public Toolbar getToolbar()
  {
      return toolbar;

  }
}
