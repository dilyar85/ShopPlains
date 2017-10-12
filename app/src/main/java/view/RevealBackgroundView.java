package view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.tianyunchen.arvin_shopping.R;

/**
 * Created by tianyunchen on 16/3/15.
 */
public class RevealBackgroundView extends View{
    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_FILL_STARTED = 1;
    public static final int STATE_FINISHED = 2;

    private static final android.view.animation.Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static  final int FILL_TIME= 400;
    private int state = STATE_NOT_STARTED;
    private Paint fillPaint;
    private int currentRadius;

    private ObjectAnimator revealAnimator;
    private int startLocationX;
    private int startLocationY;

    private OnstateChangeListerner onstateChangeListerner;

    public RevealBackgroundView(Context context) {
        super(context);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

   private void init(){
     //  int colorid = getResources().getColor(R.color.colorAccent);
       fillPaint = new Paint();
       fillPaint.setStyle(Paint.Style.FILL);
       fillPaint.setColor(Color.WHITE);

   }
    public  void startFromLocation(int[] taplocationOnscreeen)
    {
        changeState(STATE_FILL_STARTED);
        startLocationX = taplocationOnscreeen[0];
        startLocationY  = taplocationOnscreeen[1];
        revealAnimator = ObjectAnimator.ofInt(this,"currentRadius",0,getWidth()+getHeight())
                .setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        revealAnimator.start();

    }

    public void setToFinishedFrame() {
        changeState(STATE_FINISHED);
        invalidate();
    }
    protected void onDraw(Canvas canvas) {
        if (state == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        } else {
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
        }
    }
    private void changeState(int state) {
        if (this.state == state) {
            return;
        }

        this.state = state;
        if (onstateChangeListerner != null) {
            onstateChangeListerner.onStateChange(state);
        }
    }
    public void setOnStateChangeListener(OnstateChangeListerner onStateChangeListener) {
        this.onstateChangeListerner = onStateChangeListener;
    }
    public  static  interface OnstateChangeListerner{
        void  onStateChange(int state);
    }

    public void setCurrentRadius(int currentRadius) {
        this.currentRadius = currentRadius;
        invalidate();
    }

    public int getCurrentRadius() {
        return currentRadius;

    }

}
