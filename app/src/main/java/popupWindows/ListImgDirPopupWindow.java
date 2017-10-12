package popupWindows;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.List;

import bean.ImageFolderBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import utility.ImageLoader;

/**
 * Created by Dilyar on 4/10/16.
 */
public class ListImgDirPopupWindow extends PopupWindow {



    static final String LOG_TAG = ListImgDirPopupWindow.class.getSimpleName();
    private int mWidth;
    private int mHeight;
    @Bind(R.id.popup_listView)
    ListView mListView;
    private List<ImageFolderBean> mFolders;
    private View mConvertView;

    public interface OnDirSelectedListener{
        void onSelected(ImageFolderBean imageFolderBean);
    }

    public OnDirSelectedListener mListener;



    public void setOnDirSelectedListener(OnDirSelectedListener onDirSelectedListener) {

        this.mListener = onDirSelectedListener;
    }



    public ListImgDirPopupWindow(Context context, List<ImageFolderBean> folders) {

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_img_dir_main, null);
        setContentView(mConvertView);
        mFolders = folders;

        initWidthAndHeight(context);
        setWidth(mWidth);
        setHeight(mHeight);

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        //Set adapter
        ButterKnife.bind(this, mConvertView);
        mListView.setAdapter(new ListImgDirAdapter(context, mFolders));

        setItemClickEvent();


    }

    private void setItemClickEvent() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mListener != null) {
                    mListener.onSelected(mFolders.get(position));

                }
            }
        });
    }



    private void initWidthAndHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        mWidth = displayMetrics.widthPixels;
        mHeight = (int) (displayMetrics.heightPixels * 0.7);

    }



    class ListImgDirAdapter extends ArrayAdapter<ImageFolderBean> {

        private LayoutInflater mInflater;
//        private List<ImageFolderBean> mFolderData;



        public ListImgDirAdapter(Context context, List<ImageFolderBean> data) {

            super(context, 0, data);
            mInflater = LayoutInflater.from(context);
//            mFolderData = data;

        }



        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.popup_img_dir_item, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            //Reset
            holder.dirImageView.setImageResource(R.drawable.empty_image);

            ImageFolderBean bean = getItem(position);

            ImageLoader.getInstance().loadImageWithPath(bean.getFirstImagePath(), holder.dirImageView);
            holder.dirNameTextView.setText(bean.getDirName());
            holder.imageCountTextView.setText(bean.getCount() + "");

            return view;

        }



        class ViewHolder {
            @Bind(R.id.popup_dir_img)
            ImageView dirImageView;
            @Bind(R.id.popup_dir_name_textView)
            TextView dirNameTextView;
            @Bind(R.id.popup_img_count_textView)
            TextView imageCountTextView;



            public ViewHolder(View view) {

                ButterKnife.bind(this, view);
            }

        }

    }

}
