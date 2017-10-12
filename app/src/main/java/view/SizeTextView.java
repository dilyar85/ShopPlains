package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;

import untils.DisPlayUtils;

/**
 * Created by tianyunchen on 16/4/18.
 */
public class SizeTextView extends TextView implements View.OnClickListener {
    private int status = 0;
    private final int IS_CLICKED = 0;
    private final int IS_NOT_CLICKED = 1;
    private CombomViews callBack;
    public SizeTextView(Context context) {
        this(context, null);
    }

    public SizeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setStatus(int status) {
        this.status = status;


    }

    public int getStatus() {
        return status;
    }

    private void init(Context context)
    {
     this.setOnClickListener(this);
        this.setTextSize((float) 18.0);
        this.setBackgroundResource(R.drawable.size_text_background);
        this.setGravity(Gravity.CENTER);
        this.setPadding(DisPlayUtils.DipToPx(context,20), DisPlayUtils.pxToDip(context,7),
                DisPlayUtils.DipToPx(context,20),DisPlayUtils.DipToPx(context,7));

    }

    public void setCallBack(CombomViews callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onClick(View v) {
        callBack.changeStatus(v);
    }

    public interface CallBack
    {
        void callback();
    }
}
