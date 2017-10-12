package adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import utility.ImageLoader;

/**
 * Created by Dilyar on 4/9/16.
 */
public class ScanImageAdapter extends BaseAdapter {

    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;



    public ScanImageAdapter(Context context, String dirPath, List<String> imgPaths) {

        this.mDirPath = dirPath;
        this.mImgPaths = imgPaths;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return mImgPaths.size();
    }



    @Override
    public Object getItem(int position) {

        return mImgPaths.get(position);
    }



    @Override
    public long getItemId(int position) {

        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageHolder imageHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_select_image, parent, false);
            imageHolder = new ImageHolder(convertView);
            convertView.setTag(imageHolder);
        }

        else {
            imageHolder = (ImageHolder) convertView.getTag();
        }

        //Reset status
        imageHolder.mImageView.setImageResource(R.drawable.empty_image);

        //Load image using ImageLoader
        ImageLoader.getInstance().loadImageWithPath(mDirPath + "/" + mImgPaths.get(position), imageHolder.mImageView);

        return convertView;
    }



    static class ImageHolder {
        @Bind(R.id.image_preview_imageView) ImageView mImageView;

        public ImageHolder(View view) {
            ButterKnife.bind(this,view);

        }
    }
}
