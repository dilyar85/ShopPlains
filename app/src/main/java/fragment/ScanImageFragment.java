package fragment;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tianyunchen.arvin_shopping.ScanImageActivity;
import com.example.tianyunchen.arvin_shopping.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.ScanImageAdapter;
import bean.ImageFolderBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import popupWindows.ListImgDirPopupWindow;
import utility.Permission;

/**
 * Created by Dilyar on 4/19/16.
 */
public class ScanImageFragment extends Fragment {

    static final String LOG_TAG = ScanImageFragment.class.getSimpleName();

    public static final String KEY_IMAGE_ABS_PATH = "imgAbsPath";

    static boolean gotPermission = true;

    @Bind(R.id.show_all_image_gridView)
    GridView mGridView;
    @Bind(R.id.bottom_dir_info_ly)
    RelativeLayout mBottomDirLayout;
    @Bind(R.id.dir_name_textView)
    TextView mDirNameTextView;
    @Bind(R.id.image_count_textView)
    TextView mImageCountTextView;


    private ScanImageAdapter mScanImageAdapter;

    private int mMaxCount = 0;

    private File mCurrentDir;
    private List<String> mImgPaths;
    private List<ImageFolderBean> mImageFolderBeans = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private ListImgDirPopupWindow mListImgDirPopupWindow;

    private static final int SCAN_DONE_MESSAGE = 0;

//    public interface OnImgSelectedListener {
//        void onSelected(String imageAbsPath);
//    }
//
//    public OnImgSelectedListener mListener;

//    public void setOnImgSelectedListener(OnImgSelectedListener onImgSelectedListener) {
//
//        this.mListener = onImgSelectedListener;
//    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_scan_all_image_main, container, false);
        ButterKnife.bind(this, rootView);

        if (Build.VERSION.SDK_INT >= 23 && Permission.needReadFilePermission(getActivity())) {
            Permission.requestReadFile(getActivity());

        } else {
            initData();
            setGridViewClickEvent();
        }

        return rootView;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case Permission.READ_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                    initData();
                    setGridViewClickEvent();
                } else {
                    // permission denied.
                    //这里需要处理!
                    Log.e(LOG_TAG, "cannot get permission");
                    Toast.makeText(getActivity(), getResources().getString(R.string.get_read_file_permission_fail), Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
            initData();
            setGridViewClickEvent();
        }
    }



    private void setGridViewClickEvent() {

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String imgAbsPath = mCurrentDir.getAbsolutePath() + "/" + mImgPaths.get(position);


                //Store the selected image byte
                SelectAvatarFragment selectAvatarFragment = new SelectAvatarFragment();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_IMAGE_ABS_PATH, imgAbsPath);
                selectAvatarFragment.setArguments(bundle);

                //Replace the fragment
                ((ScanImageActivity) getActivity()).replaceFragment(selectAvatarFragment);

            }
        });

    }



    private void initData() {

        //Toast error if sd card is unavailable
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.sd_card_error), Toast.LENGTH_SHORT)
                    .show();
        }

        mProgressDialog = ProgressDialog.show(getActivity(), null, getResources().getString(R.string.loading));

        //Scan all the images and store each directory with its first image path to 'mImageFolderBeans'.
        new Thread() {

            @Override
            public void run() {

                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String imagesSelections = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
                String[] imageArgs = new String[]{"image/jpeg", "image/png"};

                Cursor cursor = getActivity().getContentResolver().query(imageUri,
                        null,
                        imagesSelections,
                        imageArgs,
                        MediaStore.Audio.Media.DATE_MODIFIED);

                Set<String> dirPaths = new HashSet<>();

                if (cursor != null) {

                    while (cursor.moveToNext()) {

                        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        File parentFile = new File(imagePath).getParentFile();
                        //Avoid invalid situations
                        if (parentFile == null || parentFile.list() == null)
                            continue;

                        String dirPath = parentFile.getAbsolutePath();

                        //Continue when the dir was stored before
                        if (!dirPaths.contains(dirPath)) {

                            dirPaths.add(dirPath);

                            ImageFolderBean imageFolderBean = new ImageFolderBean();
                            imageFolderBean.setDirPath(dirPath);
                            imageFolderBean.setFirstImagePath(imagePath);
                            int dirSize = parentFile.list(new FilenameFilter() {

                                @Override
                                public boolean accept(File dir, String filename) {

                                    return (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.equals(".png"));

                                }
                            }).length;
                            imageFolderBean.setCount(dirSize);
                            mImageFolderBeans.add(imageFolderBean);

                            //Show the dir with maximum images inside first
                            if (dirSize > mMaxCount) {
                                mMaxCount = dirSize;
                                mCurrentDir = parentFile;
                            }
                        }

                    }
                    cursor.close();

                    //Notify handler scanning images done
                    mHandler.sendEmptyMessage(SCAN_DONE_MESSAGE);

                }
            }
        }.start();

    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == SCAN_DONE_MESSAGE) {
                mProgressDialog.dismiss();
                passDataToView();
                initDirPopupWindow();
            }
        }

    };



    private void passDataToView() {

        if (mCurrentDir == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.scan_image_filed), Toast.LENGTH_SHORT).show();
        } else {
            mImgPaths = Arrays.asList(mCurrentDir.list());

            mScanImageAdapter = new ScanImageAdapter(getActivity(), mCurrentDir.getAbsolutePath(), mImgPaths);
            mGridView.setAdapter(mScanImageAdapter);

            mDirNameTextView.setText(mCurrentDir.getName());
            mImageCountTextView.setText(mMaxCount + "");
        }

    }



    private void initDirPopupWindow() {

        mListImgDirPopupWindow = new ListImgDirPopupWindow(getActivity(), mImageFolderBeans);

        mListImgDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                lightOn();
            }
        });

        mListImgDirPopupWindow.setOnDirSelectedListener(new ListImgDirPopupWindow.OnDirSelectedListener() {

            @Override
            public void onSelected(ImageFolderBean imageFolderBean) {

                mCurrentDir = new File(imageFolderBean.getDirPath());
                mImgPaths = Arrays.asList(mCurrentDir.list(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String filename) {

                        return (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.equals(".png"));
                    }

                }));

                mScanImageAdapter = new ScanImageAdapter(getActivity(), mCurrentDir.getAbsolutePath(), mImgPaths);
                mGridView.setAdapter(mScanImageAdapter);

                mDirNameTextView.setText(imageFolderBean.getDirName());
                mImageCountTextView.setText(mImgPaths.size() + "");

                mListImgDirPopupWindow.dismiss();

            }
        });

    }



    @OnClick(R.id.bottom_dir_info_ly)
    public void clickBottomLayout() {

        mListImgDirPopupWindow.setAnimationStyle(R.style.dir_popup_window_anim);

        mListImgDirPopupWindow.showAsDropDown(mBottomDirLayout, 0, 0);
        lightOff();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("grid", "i am clicked");

            }
        });

    }



    //Make the content area bright again when popup window dismissed
    private void lightOn() {

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f;
        getActivity().getWindow().setAttributes(lp);

    }



    //Make the content area dark when the popup window shows
    private void lightOff() {

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.3f;
        getActivity().getWindow().setAttributes(lp);

    }

}
