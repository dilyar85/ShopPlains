package fragment;

/**
 * Created by tianyunchen on 16/4/9.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.tianyunchen.arvin_shopping.R;

import java.util.List;

import server.LeanCloudServer;
import view.RecycleViewDivider;
public class TestFragment extends  android.support.v4.app.Fragment implements LeanCloudServer.CallbackListener {
    private List<AVObject> arrayList;

    private  RecyclerView billing_recycle;
    // private ArrayList<AVObject> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test,container,false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        billing_recycle = (RecyclerView)view.findViewById(R.id.billing_recycle);

        billing_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        billing_recycle.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));

        LeanCloudServer.getInstance()
                .setQuery("BillingAddress")
                .getDataByRequirement(this, "userId", "570326a679bc440052728eab");

    }

    private void setuprecycleview()
    {
        Log.d("TestFragment","setuprecycle");
        billing_recycle.setAdapter(new BillingAddressAdapter());

    }

    @Override
    public void getDataDone(List<AVObject> data) {
        Log.d("Tester", data.get(0).getClassName());
        arrayList = data;
        billing_recycle.setAdapter(new BillingAddressAdapter());
    }



    @Override
    public void getUserNameAndAvatarDone(String username, String url) {

    }



    class BillingAddressAdapter extends RecyclerView.Adapter
    {

        MyViewHolder holder;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            holder  = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_billing_address
                    ,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyViewHolder holderview = (MyViewHolder)holder;
            holderview.address_text.setText(arrayList.get(position).getString("address"));
            holderview.receiver_text.setText(arrayList.get(position).getString("receiver"));
            holderview.phonenumber_text.setText(arrayList.get(position).getString("phone"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size() ;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView address_text;
            TextView receiver_text;
            TextView phonenumber_text;


            public MyViewHolder(View itemView) {
                super(itemView);
                address_text = (TextView)itemView.findViewById(R.id.address);
                receiver_text = (TextView)itemView.findViewById(R.id.receiver);
                phonenumber_text = (TextView)itemView.findViewById(R.id.phonenumber);
            }
        }
    }
}
