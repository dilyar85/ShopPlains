package com.example.tianyunchen.arvin_shopping;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.ScanImageFragment;
import utility.ImageLoader;

/**
 * Created by Dilyar on 3/17/16.
 */
public class SignUpActivity extends Activity {

    static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    static final int REQUEST_SELECT_AVATAR = 1;

    //    @Bind(R.id.sign_up_scrollView)
//    public ScrollView mScrollView;

    @Bind(R.id.sign_up_upload_avatar_button)
    public RoundedImageView mUploadAvatar;
    @Bind(R.id.sign_up_username_editText)
    public EditText mUsernameEditText;
    @Bind(R.id.sign_up_email_editText)
    public EditText mEmailEditText;
    @Bind(R.id.sign_up_password_editText)
    public EditText mPasswordEditText;
    @Bind(R.id.sign_up_password_confirm_editText)
    public EditText mConfirmPasswordEditText;

    private AlertDialog mVerifiedResultDialog;
    private String userAvatarImgPath;

    String inputUsername;
    String inputEmail;
    String inputPassword;
    String inputConfirmedPassword;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECT_AVATAR && resultCode == RESULT_OK) {
            userAvatarImgPath = data.getStringExtra(ScanImageFragment.KEY_IMAGE_ABS_PATH);
            if (userAvatarImgPath != null) {
                ImageLoader.getInstance().loadImageWithPath(userAvatarImgPath, mUploadAvatar);
                mUploadAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mUploadAvatar.setOval(true);

            }
        }

    }

//    private void initViews() {
//
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metrics);
//        ViewGroup.LayoutParams params = mLayout.getLayoutParams();
//        params.height = metrics.heightPixels;
//        mLayout.setLayoutParams(params);
//
//
//    }



    @OnClick(R.id.sign_up_upload_avatar_button)
    public void selectAvatar() {

        Intent intent = new Intent(this, ScanImageActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_AVATAR);

    }



    @OnClick(R.id.sign_up_button)
    public void signUpNewAccount() {

        inputUsername = mUsernameEditText.getText().toString();
        Log.e(LOG_TAG, inputUsername + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        inputEmail = mEmailEditText.getText().toString();
        inputPassword = mPasswordEditText.getText().toString();
        inputConfirmedPassword = mConfirmPasswordEditText.getText().toString();

        if (isCorrectInput()) {
            showLoadingDialog();
            AVUser user = new AVUser();
            user.setUsername(inputUsername);
            user.setEmail(inputEmail);
            user.setPassword(inputPassword);
            user.signUpInBackground(new SignUpCallback() {

                public void done(AVException e) {

                    if (e == null) {
//                        LeanCloudServer.getInstance().uploadUserAvatar(userAvatarImgPath);

                        mVerifiedResultDialog.dismiss();
                        showToast("注册新账户成功!");
                        TimerTask task = new TimerTask() {

                            @Override
                            public void run() {

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("email", inputEmail);
                                bundle.putString("password", inputPassword);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                startActivity(new Intent(SignUpActivity.this, SelectGenderActivity.class));
                                finish();
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 2500);

                    } else {
                        mVerifiedResultDialog.dismiss();
                        String message = e.getMessage();
                        Log.d(LOG_TAG, "Failed to create account. Message: " + message);
                        showToast(message);
                    }
                }
            });
        }

    }



    @OnClick(R.id.sign_up_return_textView)
    public void returnLogInActivity() {

        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);

    }





    private void showLoadingDialog() {

        View layout = LayoutInflater.from(this).inflate(R.layout.animation_loading, null);
        ImageView loadingImageview = (ImageView) layout.findViewById(R.id.loading_imageview);
        AnimationDrawable loadingAnimationDrawable = (AnimationDrawable) loadingImageview.getBackground();
        mVerifiedResultDialog = new AlertDialog.Builder(this).create();
        mVerifiedResultDialog.setView(layout);
        mVerifiedResultDialog.setCancelable(false);
        loadingAnimationDrawable.start();
        mVerifiedResultDialog.show();
        WindowManager.LayoutParams lp = mVerifiedResultDialog.getWindow().getAttributes();
        lp.height = 400;
        lp.width = 400;
        mVerifiedResultDialog.getWindow().setAttributes(lp);
    }



    private void showToast(String message) {

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 15);
        toast.show();
    }



    public boolean isCorrectInput() {

        if (inputUsername.isEmpty()) {
            showToast(getResources().getString(R.string.type_username_hint));
            return false;
        } else if (inputEmail.isEmpty()) {
            showToast(getResources().getString(R.string.type_email_hint));
            return false;
        } else if (inputPassword.isEmpty()) {
            showToast(getResources().getString(R.string.type_password_hint));
            return false;
        } else if (inputConfirmedPassword.isEmpty() || !inputConfirmedPassword.equals(inputPassword)) {
            showToast(getResources().getString(R.string.no_match_password_hint));
            return false;
        }
        return true;
    }
}
