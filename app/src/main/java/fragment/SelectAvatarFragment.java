package fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tianyunchen.arvin_shopping.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.ImageLoader;

/**
 * Created by Dilyar on 4/19/16.
 */
public class SelectAvatarFragment extends Fragment {

    @Bind(R.id.selected_imageView)
    ImageView mImageView;


    private String mImgAbsPath;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_select_one_image,container , false);
        ButterKnife.bind(this,rootView);

        initData();
        return rootView;
    }



    private void initData() {

        Bundle bundle = this.getArguments();
        mImgAbsPath = bundle.getString(ScanImageFragment.KEY_IMAGE_ABS_PATH);

        if (mImgAbsPath != null) {
            ImageLoader.getInstance().loadImageWithPath(mImgAbsPath,mImageView);
        }

    }

    @OnClick(R.id.confirm_avatar_button)
    public void returnResult() {

        if (mImgAbsPath != null) {
            Intent intent = new Intent();
            intent.putExtra(ScanImageFragment.KEY_IMAGE_ABS_PATH, mImgAbsPath);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }


    }



}
