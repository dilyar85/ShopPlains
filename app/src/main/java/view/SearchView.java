package view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tianyunchen.arvin_shopping.R;

import java.util.List;

/**
 * Created by tianyunchen on 16/4/10.
 */
public class SearchView extends LinearLayout  {

    private EditText etInput;
    private ImageView ivDelete;
    private Context context;
    private ListView listView;

    private ArrayAdapter<String> mHintAdapter;
    private ArrayAdapter<String> mAutoAdapter;
    private SearchViewListener searchViewListener;
    private PopupWindow popupWindow;

    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview(context);
    }

    private void initview(Context context)
    {
        View  view = View.inflate(context, R.layout.view_search,this);
       // View popview = View.inflate(context,R.layout.view_popupwindow,null);
       // listView = (ListView)popview.findViewById(R.id.search_lv_tips);
        etInput = (EditText)view.findViewById(R.id.serach_et_input);
        etInput.addTextChangedListener(new EditChangeListener());
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listView.setVisibility(GONE);
                }
                return true;
            }
        });
        //popupWindow = new PopupWindow(popview, WindowManager.LayoutParams.WRAP_CONTENT,
         //       WindowManager.LayoutParams.WRAP_CONTENT
        //,true);
       // popupWindow.showAsDropDown(etInput);
    }
    public void setTipsHintAdapter(ArrayAdapter<String> adapter)
 {
     mHintAdapter = adapter;
     if(listView.getAdapter()==null)
     {
         listView.setAdapter(mHintAdapter);
     }
 }
    public void setmAutoAdapter (ArrayAdapter<String> adapter)
    {
        mAutoAdapter = adapter;
    }

    public void setSearchViewListener(SearchViewListener searchViewListener)
    {
        this.searchViewListener = searchViewListener;
    }
  private class EditChangeListener implements TextWatcher{

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
       popupWindow.showAsDropDown(etInput);
          if(!s.toString().equals(""))
       {
           Log.d("nimama",s.toString());
           listView.setVisibility(VISIBLE);
           if(mAutoAdapter!=null &&listView.getAdapter()!=mAutoAdapter)
           {
               listView.setAdapter(mAutoAdapter);
           }
           if(searchViewListener!=null)
           {
               searchViewListener.onRefreshAutoComplete(s+"");
           }

       }else
       {
           if(mHintAdapter!=null)
           {
               listView.setAdapter(mHintAdapter);
           }
           listView.setVisibility(GONE);
           popupWindow.dismiss();
       }

      }

      @Override
      public void afterTextChanged(Editable s) {

      }
  }
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }


}
