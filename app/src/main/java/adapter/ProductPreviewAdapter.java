package adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.ArrayList;

import bean.ProductBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import utility.ImageLoader;

/**
 * Created by Dilyar on 5/21/16.
 */
public class ProductPreviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProductBean> mProductPreviewBeen;
    private LayoutInflater mInflater;

    public ProductPreviewAdapter(Context context, ArrayList<ProductBean> mProductPreviewBeen) {

        this.mInflater = LayoutInflater.from(context);
        this.mProductPreviewBeen = mProductPreviewBeen;
    }



    @Override
    public int getCount() {

        return mProductPreviewBeen.size();
    }



    @Override
    public Object getItem(int position) {

        return mProductPreviewBeen.get(position);
    }



    @Override
    public long getItemId(int position) {

        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_storeitems,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Reset status
        viewHolder.mItemImageView.setImageResource(R.drawable.empty_image);

        ImageLoader.getInstance().loadImageWithUrl(mProductPreviewBeen.get(position).getThumbnailUrl(),viewHolder.mItemImageView);

        viewHolder.mItemNameTextview.setText(mProductPreviewBeen.get(position).getName());
        viewHolder.mItemPriceTextview.setText(mProductPreviewBeen.get(position).getPrice());
        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.storeitems_item_imageview)
        ImageView mItemImageView;
        @Bind(R.id.storeitems_item_name_textview)
        TextView mItemNameTextview;
        @Bind(R.id.storeitems_item_price_textview)
        TextView mItemPriceTextview;

        public ViewHolder(View view) {

            ButterKnife.bind(this, view);

        }
    }

}
