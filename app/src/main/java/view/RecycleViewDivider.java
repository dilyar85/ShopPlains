package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tianyunchen.arvin_shopping.R;

/**
 * Created by tianyunchen on 16/4/9.
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private Paint mPain;
    private Drawable mDivider;
    private int mDividerHeight =1;
    private int mOrientation;

    public RecycleViewDivider(Context context, int mOrientation) {
       if(mOrientation!= LinearLayoutManager.VERTICAL&& mOrientation!= LinearLayoutManager.HORIZONTAL)
       {
     throw  new IllegalArgumentException("erro argument");
       }
        this.mOrientation = mOrientation;
        mPain = new Paint(Paint.ANTI_ALIAS_FLAG);
        int colorid = context.getResources().getColor(R.color.divider_color);
        mPain.setColor(colorid);
        mPain.setStyle(Paint.Style.FILL);
    }

    public RecycleViewDivider(Context context, int mOrientation, int mDividerHeight) {
       this(context, mOrientation);
        this.mDividerHeight = mDividerHeight;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,mDividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontal(c,parent);
    }
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPain != null) {
                canvas.drawRect(left, top, right, bottom, mPain);
            }
        }
    }
}
