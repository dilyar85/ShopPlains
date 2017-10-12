package view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.ArrayList;

/**
 * Created by tianyunchen on 16/4/18.
 */
public class SizeTextViews extends LinearLayout implements CombomViews {
    private int sizes[] = {43,44,45,46};
    private  Context context;
    public SizeTextViews(Context context) {
        this(context,null);
    }

    public SizeTextViews(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SizeTextViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private  void init(Context context)
    {
        this.context =context;
        addViews();

    }
    @Override
    public void changeStatus(View view) {

        for(int i=0;i<this.getChildCount();i++)
        {
          if( ((SizeTextView)this.getChildAt(i)).getStatus()==1)
          {
              ((SizeTextView)this.getChildAt(i)).setBackgroundResource(R.drawable.size_text_background);

              ((SizeTextView)this.getChildAt(i)).setStatus(0);

              break;
          }


        }
        ( (SizeTextView)view).setBackgroundResource(R.drawable.size_text_clicked_background);
        ( (SizeTextView)view).setTextColor(Color.WHITE);
        ( (SizeTextView)view).setStatus(1);
    }

    @Override
    public void addViews() {
         LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                 , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,0,0);
//         SizeTextView textView = new SizeTextView(context);
        for(int i =0;i<sizes.length;i++)
        {
            SizeTextView textView = new SizeTextView(context);
            textView.setCallBack(this);
            textView.setText(sizes[i]+"");
            this.addView(textView, params);
        }
    }
}
