package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.ArrayList;

import untils.DisPlayUtils;

/**
 * Created by tianyunchen on 16/4/17.
 */
public class ColorsView extends LinearLayout implements CombomViews {
    private ArrayList<Integer> colors;
    private Context context;
    public ColorsView(Context context) {
        super(context);
        init(context);
    }

    public ColorsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void  init(Context context)
    {
        this.context =context;
         this.setOrientation(HORIZONTAL);
        colors = new ArrayList<>();
        colors.add(1);
        colors.add(1);
        colors.add(1);
        colors.add(1);
//        addColorViews();
addViews();
    }
    public void setColors(ArrayList<Integer> integers)
    {
        colors = integers;
    }

//    public void addColorViews()
//    {
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(44,44);
//        layoutParams.setMargins(10,0,0,0);
//       int color[] = {getContext().getResources().getColor(R.color.production_blue),
//               getContext().getResources().getColor(R.color.production_pink),
//               getContext().getResources().getColor(R.color.production_red),
//               getContext().getResources().getColor(R.color.production_yellow),};
//        //Log.d("linearlayou","addview");
//        for(int i =0;i<colors.size();i++)
//        {
//            ColorButtonView colorButtonView = new ColorButtonView(context);
//            colorButtonView.setCallback(this);
//            colorButtonView.setColr(color[i]);
//            colorButtonView.invalidate();
//            this.addView(colorButtonView,layoutParams);
//            Log.d("linearlayou", "addview");
//        }
//    }

//    @Override
//    public void changestatus(View view) {
//        ColorButtonView colorButtonView;
////        ColorButtonView currentview = (ColorButtonView)view;
//        for(int i = 0;i<this.getChildCount();i++)
//        {
//            colorButtonView = (ColorButtonView)this.getChildAt(i);
//            if(colorButtonView.getStatus()==1)
//            {
//                colorButtonView.setStatus(0);
//                colorButtonView.invalidate();
//            }
//            ((ColorButtonView) view).setStatus(1);
//            ( (ColorButtonView) view).invalidate();
//
//        }
//
//    }

    @Override
    public void changeStatus(View view) {
        ColorButtonView colorButtonView;
//        ColorButtonView currentview = (ColorButtonView)view;
        for(int i = 0;i<this.getChildCount();i++)
        {
            colorButtonView = (ColorButtonView)this.getChildAt(i);
            if(colorButtonView.getStatus()==1)
            {
                colorButtonView.setStatus(0);
                colorButtonView.invalidate();
            }
            ((ColorButtonView) view).setStatus(1);
            ( (ColorButtonView) view).invalidate();

        }
    }



    @Override
    public void addViews() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisPlayUtils.DipToPx(context,
                35.0f),DisPlayUtils.DipToPx(context,35.0f));
        layoutParams.setMargins(10,0,0,0);
        int color[] = {getContext().getResources().getColor(R.color.production_blue),
                getContext().getResources().getColor(R.color.production_pink),
                getContext().getResources().getColor(R.color.production_red),
                getContext().getResources().getColor(R.color.production_yellow),};
        //Log.d("linearlayou","addview");
        for(int i =0;i<colors.size();i++)
        {
            ColorButtonView colorButtonView = new ColorButtonView(context);
            colorButtonView.setCallback(this);
            colorButtonView.setColr(color[i]);
            colorButtonView.invalidate();
            this.addView(colorButtonView,layoutParams);
            Log.d("linearlayou", "addview");
        }
    }
}
