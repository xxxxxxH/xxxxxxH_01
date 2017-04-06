package CustomFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidong.xh.ydboss.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AllBusinessAdapter;
import bean.Business;
import bean.Common;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/2/28.
 */

public class FavorBusinessFragment extends Fragment {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RelativeLayout main_bottom_rl,business_bottom_rl;
    private List<Business> mlist = new ArrayList<>();
    private int i = 0;
    private EditText title_et;
    private AllBusinessAdapter adapter;
    private SwipeRefreshLayout pull_refresh_f;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.resetAdapter(Common.favorable_business);
                    pull_refresh_f.setRefreshing(false);
                    Toast.makeText(getActivity(),"刷新完成", Toast.LENGTH_SHORT).show();
                    title_et.setText("");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorablebusiness_f, container, false);
        TextView title_tv = (TextView) getActivity().findViewById(R.id.title_tv);
        title_et = (EditText) getActivity().findViewById(R.id.title_et);
        final ImageView search = (ImageView) getActivity().findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBusiness(Common.favorable_business);
            }
        });
        title_tv.setVisibility(View.GONE);
        title_et.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);

        pull_refresh_f = (SwipeRefreshLayout) view.findViewById(R.id.pull_refresh_f);
        pull_refresh_f.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });

        Common.isNeedExit = "yes";
        RecyclerView listview_favorablebusiness = (RecyclerView) view.findViewById(R.id.listview_favorablebusiness);
        adapter = new AllBusinessAdapter(getActivity(), Common.favorable_business);
        listview_favorablebusiness.setAdapter(adapter);
        listview_favorablebusiness.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview_favorablebusiness.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.setOnItemClickListener(new AllBusinessAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                Common.business = null;
                if (i == 0){
                    Common.business = Common.favorable_business.get(position);
                }else {
                    i = 0;
                    Common.business = mlist.get(position);
                }
                title_et.setText("");
                transaction.replace(R.id.content, new BusinessInfoFragemnt());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        main_bottom_rl = (RelativeLayout) getActivity().findViewById(R.id.main_bottom_rl);
        business_bottom_rl = (RelativeLayout) getActivity().findViewById(R.id.business_bottom_rl);
        main_bottom_rl.setVisibility(View.VISIBLE);
        business_bottom_rl.setVisibility(View.GONE);
        return view;
    }
    private void searchBusiness(List<Business> list){
        i = 1;
        mlist.clear();
        String content = title_et.getText().toString();
        if (!content.equals("")){
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getName().contains(content)){
                    mlist.add(list.get(i));
                }
            }
            if (mlist.size() > 0){
                adapter.resetAdapter(mlist);
                Toast.makeText(getActivity() , "共查询到" + mlist.size() + "家商家", Toast.LENGTH_SHORT).show();
            }else {
                i = 0;
                Toast.makeText(getActivity(), "未查询到相关商家", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "请输入关键字", Toast.LENGTH_SHORT).show();
        }
    }
}
