package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.tianyunchen.arvin_shopping.ProductDetailActivity;
import com.example.tianyunchen.arvin_shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import adapter.StoreListViewAdapter;
import bean.StorePreViewBean;
import server.LeanCloudServer;

/**
 * Created by tianyunchen on 16/4/12.
 */
public class StorePreViewFragment extends Fragment implements LeanCloudServer.CallbackListener {
    private List<AVObject> data;
    private static final String TABLENAME_ACTIVITY_STORE="ActivityStore";
    private static final String TABLENAME_STORE_PREVIEW ="StorePreview";
    private ImageView actvity_image;
    private TextView  activity_name;
    private TextView activity_des;
    private ListView  store_list;
    private ArrayList<StorePreViewBean>  store_array;
    private View headView;
    DisplayImageOptions options;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.fragment_storepreview,null);
        headView = View.inflate(getActivity(),R.layout.view_store_list_header,null);
        initview(view);
        initdata();
        return view;
    }


    private void initdata()
    {
        store_array = new ArrayList<StorePreViewBean>();
        options = new DisplayImageOptions.Builder()

                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .build();
         LeanCloudServer.getInstance().setQuery("ActivityStore").
                 getDataByRequirement(this, "category", "1");
         LeanCloudServer.getInstance().setQuery("StorePreview")
                 .getDataByRequirement(this, "category", "1");

    }


    private  void initview(View view)
    {
          actvity_image = (ImageView)headView.findViewById(R.id.image);
          activity_des = (TextView)headView.findViewById(R.id.descpction_activity_store);
          activity_name = (TextView)headView.findViewById(R.id.name_activity_store);
          store_list = (ListView)view.findViewById(R.id.listview_store);
        store_list.addHeaderView(headView);


    }

    @Override
    public void getDataDone(List<AVObject> data) {

        if(data.get(0).getClassName().equals(TABLENAME_ACTIVITY_STORE))
        {
            ImageLoader.getInstance().displayImage(data.get(0).getAVFile("storeimage").getUrl(),actvity_image,options);
            activity_des.setText(data.get(0).getString("storedes"));
            activity_name.setText(data.get(0).getString("storename"));
        }
        if(data.get(0).getClassName().equals(TABLENAME_STORE_PREVIEW))
        {
            for(int i =0;i< data.size();i++)
            {
                store_array.add(new StorePreViewBean(data.get(i).getString("storename"),
                        data.get(i).getString("storedes"),
                        data.get(i).getString("currentprice"),
                        data.get(i).getString("originalprice"),
                        data.get(i).getAVFile("storeimage").getUrl()));

            }
            store_list.setAdapter(new StoreListViewAdapter(getActivity(),store_array));
            store_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    startActivity(intent);
                }
            });

        }
    }



    @Override
    public void getUserNameAndAvatarDone(String username, String url) {

    }
}
