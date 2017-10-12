package com.example.tianyunchen.arvin_shopping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import java.util.List;

import bean.Bean;
import butterknife.Bind;
import fragment.StorePreViewFragment;
import view.NewSearchView;

/**
 * Created by tianyunchen on 16/4/10.
 */
public class StorePreViewActivity extends BaseDrawerActivity {
   @Bind(R.id.searchview)
   NewSearchView searchView;

    private ArrayAdapter<String> hintAdapter;
    private ArrayAdapter<String> mAutoAdapter;
    private List<Bean> resultData;
    private static  int DEFAULT_HINT_SIZE =4;
    private static  int hintSize = DEFAULT_HINT_SIZE;
    private List<Bean> dbData;
    private List<String> hintData;
    private List<String> autoCompleteData;
    private FragmentManager fragmentManager;
   // private FragmentTransaction transaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepreview);

//        initData();
//        initViews();
        repaleFragment(new StorePreViewFragment());
      //  searchView.setFragmentTransaction(fragmentManager.beginTransaction());
        searchView.setFragmentManager(fragmentManager);
    }
//
//    private void initViews()
//    {
//       searchView.setSearchViewListener(this);
//        searchView.setTipsHintAdapter(hintAdapter);
//        searchView.setmAutoAdapter(mAutoAdapter);
//    }
//
//
//    private void initData()
//    {
//getData();
//        getHintData();
//        getAutoCompleteData(null);
//    }
//
//    private void getData()
//    {
//        int size = 100;
//        dbData = new ArrayList<>(size);
//        for(int i =0;i<size;i++)
//        {
//            dbData.add(new Bean(R.drawable.log_in_button,"android 开发技能"+(i+1),
//                    "Android自定义view——自定义搜索view", i * 20 + 2 + ""));
//        }
//    }
//
//    private void getHintData()
//    {
//         hintData = new ArrayList<>(hintSize);
//        for(int i =1;i<hintSize;i++)
//        {
//            hintData.add("热搜版" + i + "：Android自定义View");
//        }
//        hintAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hintData);
//    }
//
//    private void getAutoCompleteData(String text)
//    {
//        if(autoCompleteData == null)
//        {
//            autoCompleteData = new ArrayList<>(hintSize);
//        }else {
//            autoCompleteData.clear();
//            for(int i=0,count=0;i<dbData.size()&&count<hintSize;i++){
//                   if(dbData.get(i).getTitle().contains(text.trim()))
//                   {
//                       autoCompleteData.add(dbData.get(i).getTitle());
//                       count++;
//                   }
//            }
//        }
//        if(mAutoAdapter == null)
//        {
//            mAutoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
//                    autoCompleteData);
//        }
//        else{
//            mAutoAdapter.notifyDataSetChanged();
//        }
//    }
//    @Override
//    public void onRefreshAutoComplete(String text) {
//        getAutoCompleteData(text);
//    }
//
//    @Override
//    public void onSearch(String text) {
//
//    }


    private void repaleFragment(Fragment fragment)
    {
        fragmentManager =this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawer_frameLayout, fragment);
        transaction.commit();


    }
}
