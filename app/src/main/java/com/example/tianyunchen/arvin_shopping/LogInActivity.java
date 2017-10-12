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
import com.avos.avoscloud.LogInCallback;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dilyar on 3/17/16.
 */
public class LogInActivity extends Activity {

    static final  String LOG_TAG = LogInActivity.class.getSimpleName();

    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_USER_PASSWORD = "userPassword";


    @Bind(R.id.log_in_username_editText)
    public EditText mUserNameEditText;
    @Bind(R.id.log_in_password_editText)
    public EditText mPasswordEditText;

    public String inputUserName;
    public String inputPassword;

    private ImageView loadingImageview;
    private AnimationDrawable loadingAnimationDrawable;

    private AlertDialog verifiedResultDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.log_in_button)
    public void clickLogInButton() {

       if(checkEditTextBlank()) {
           showLoadingDialog();
           verifyAccount();
       }

    }



    private boolean checkEditTextBlank() {

        inputUserName = mUserNameEditText.getText().toString();
        inputPassword = mPasswordEditText.getText().toString();

        if (inputUserName.length() <=0) {
            showToast("请输入您的用户名");
            return false;
        }
        if (inputPassword.length()<=0) {
            showToast("请输入您的密码");
            return false;
        }
        return true;
    }



    @OnClick(R.id.log_in_sign_up_textView)
    public void clickSignUpTextView() {

        startActivity(new Intent(this, SignUpActivity.class));
    }






    private void verifyAccount() {

        AVUser.logInInBackground(inputUserName, inputPassword, new LogInCallback<AVUser>() {

            @Override
            public void done(AVUser avUser, AVException e) {

                if (e == null) {
                    verifiedResultDialog.dismiss();
                    showToast("登录成功");
                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            Intent intent = new Intent(LogInActivity.this, SelectGenderActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);
                } else {
                    verifiedResultDialog.dismiss();

                    String originalMessage = e.getMessage();
//                    int startIndexOfError = originalMessage.lastIndexOf("error") + 8;
//                    int lastIndexOfError = originalMessage.lastIndexOf(".");
//                    String errorMessage = originalMessage.substring(startIndexOfError, lastIndexOfError);
                    showToast(e.getMessage());
                    Log.d(LOG_TAG, "Fail to Log In. Full Message from Cloud: " + e.getMessage());

                }
            }
        });

    }



    private void showLoadingDialog() {

        View layout = LayoutInflater.from(this).inflate(R.layout.animation_loading, null);

        loadingImageview = (ImageView) layout.findViewById(R.id.loading_imageview);
        loadingAnimationDrawable = (AnimationDrawable) loadingImageview.getBackground();
        verifiedResultDialog = new AlertDialog.Builder(this).create();
        verifiedResultDialog.setView(layout);
        verifiedResultDialog.setCancelable(false);

        verifiedResultDialog.show();
        loadingAnimationDrawable.start();
        WindowManager.LayoutParams lp = verifiedResultDialog.getWindow().getAttributes();
        lp.height = 400;
        lp.width = 400;
        verifiedResultDialog.getWindow().setAttributes(lp);
    }



    private void showToast(String message) {

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.BOTTOM, 0, 15);
        toast.show();
    }
}
