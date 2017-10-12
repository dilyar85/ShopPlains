package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import bean.StorePreViewBean;

/**
 * Created by tianyunchen on 16/4/13.
 */
public class StoreListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<StorePreViewBean> arrayList;
    DisplayImageOptions options;



    public StoreListViewAdapter(Context context, ArrayList<StorePreViewBean> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        options = new DisplayImageOptions.Builder()

                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .build();
    }



    @Override
    public int getCount() {

        return arrayList.size();
    }



    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }



    @Override
    public long getItemId(int position) {

        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_store_preview, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.store_image);
            viewHolder.store_name = (TextView) convertView.findViewById(R.id.store_name);
            viewHolder.store_des = (TextView) convertView.findViewById(R.id.store_des);
            viewHolder.old_price = (TextView) convertView.findViewById(R.id.old_price);
            viewHolder.new_price = (TextView) convertView.findViewById(R.id.currentprice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        ImageLoader.getInstance().displayImage(arrayList.get(position).getImageaddress(), viewHolder.imageView, options);
        viewHolder.store_name.setText(arrayList.get(position).getStorename());
        viewHolder.store_des.setText(arrayList.get(position).getStoredes());
        viewHolder.old_price.setText(arrayList.get(position).getOldprice());
        viewHolder.new_price.setText(arrayList.get(position).getCurrentprice());

        return convertView;
    }



    private class ViewHolder {
        ImageView imageView;
        TextView store_name;
        TextView store_des;
        TextView old_price;
        TextView new_price;

    }
}
