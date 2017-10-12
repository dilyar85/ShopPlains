package view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.tianyunchen.arvin_shopping.R;

import fragment.SearchFragment;
import fragment.StorePreViewFragment;

/**
 * Created by tianyunchen on 16/4/13.
 */
public class NewSearchView extends EditText {
    private ArrayAdapter<String> autoAapater;
    private Context context;
    private FragmentManager fragmentManager;
    private SearchFragment searchFragment;

    public NewSearchView(Context context) {
        super(context);
        init(context);
    }

    public NewSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context)
    {
        searchFragment = new SearchFragment();
        this.addTextChangedListener(new EditeTesterWatcher());
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }



    private class EditeTesterWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //getFragmentshow
            if(!s.toString().equals(""))
            {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.drawer_frameLayout,searchFragment).commit();
            }
            else
            {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawer_frameLayout,new StorePreViewFragment()).commit();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void setAutoAapater(ArrayAdapter<String> autoAapater) {
        this.autoAapater = autoAapater;
    }


    interface Callback
    {

    }
}
