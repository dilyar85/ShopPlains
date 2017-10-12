package adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tianyunchen.arvin_shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import utility.ImageLoader;

/**
 * Created by Dilyar on 5/25/16.
 */
public class ViewPagerAdapter extends RecyclingPagerAdapter {

    private  ArrayList<String> mList;
    private  Context mContext;

    DisplayImageOptions options = new DisplayImageOptions.Builder()

            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .build();



    public ViewPagerAdapter(Context context, ArrayList<String> list) {

        this.mContext = context;
        this.mList = list;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_viewpager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Reset status
        ImageView imageView = viewHolder.mImageView;
        imageView.setImageResource(R.drawable.empty_image);


        ImageLoader.getInstance().loadImageWithUrl(mList.get(position), imageView);
        //Scale detail images
        imageView.setScaleType(ImageView.ScaleType.FIT_START);


        return convertView;
    }



    @Override
    public int getCount() {

        return mList.size();
    }

    class ViewHolder {
        @Bind(R.id.viewPager_detail_imageView)
        ImageView mImageView;


        public ViewHolder(View view) {

            ButterKnife.bind(this, view);
        }
    }
}
