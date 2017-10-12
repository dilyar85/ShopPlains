package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;

/**
 * Created by tianyunchen on 16/4/9.
 */
public class BadgeView extends FrameLayout{
  private int backgroundId;
    private Drawable drawable;
    private ImageView backImage;

    public BadgeView(Context context) {
        super(context);
        initview(context);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
       if(attrs!=null)
       {
           final TypedArray  a= context.obtainStyledAttributes(attrs,
               R.styleable.BadgeView);
           drawable = a.getDrawable(R.styleable.BadgeView_image);
       }

        initview(context);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void initview(Context context)
    {
      View view=  View.inflate(context, R.layout.item_bageview, this);
        backImage = (ImageView)view.findViewById(R.id.image);
        backImage.setBackground(drawable);

    }
}
