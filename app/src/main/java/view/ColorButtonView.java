package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tianyunchen.arvin_shopping.R;

/**
 * Created by tianyunchen on 16/4/17.
 */
public class ColorButtonView extends View implements View.OnClickListener{
   private Paint circlepaint;
    private Paint avlpaint;
    private int status=0;
    private final int IS_CLIKCED =1;
    private final int IS_NOT_CLICKED=0;
    private CombomViews callback;
    public ColorButtonView(Context context) {
        this(context, null);

    }

    public ColorButtonView(Context context, AttributeSet attrs) {
       this(context, attrs, 0);

    }

    public ColorButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureHeight(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }
    private int measureHeight(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(specMode==MeasureSpec.EXACTLY)
        {
            result = size;
        }
        return  result;
    }
    private void init()
    {
        circlepaint = new Paint();
        avlpaint = new Paint();
        circlepaint.setColor(Color.BLUE);

        this.setOnClickListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        RectF rectF = new RectF(4,4,getHeight()-4,getWidth()-4);
        circlepaint.setAntiAlias(true);
        canvas.drawOval(rectF, circlepaint);
        if(status==IS_CLIKCED) {
            avlpaint.setStrokeWidth((float) 4.0);
            avlpaint.setStyle(Paint.Style.STROKE);
            avlpaint.setAntiAlias(true);
            RectF rectF1 = new RectF(4, 4, getHeight()-4, getWidth()-4);
            int color = getContext().getResources().getColor(R.color.colorAccent);
            avlpaint.setColor(color);
            canvas.drawArc(rectF1, 0, 360, false, avlpaint);
        }

        Log.d("Button","drawer");

    }

    @Override
    public void onClick(View v) {
//        if (status == IS_NOT_CLICKED)
//        {
//            status= IS_CLIKCED;
//        }
//       invalidate();
        callback.changeStatus(v);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCallback(CombomViews callback) {
        this.callback = callback;
    }

    public void setColr(int color)
    {
        circlepaint.setColor(color);
    }

    public  interface Callback{
        void changestatus(View view);

    }


}
