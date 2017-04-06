package CustomFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import adapter.AllBusinessAdapter;
import com.yidong.xh.ydboss.R;

import java.util.ArrayList;
import java.util.List;

import bean.Business;
import bean.Common;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/2/27.
 */

public class AllBusinessFragment extends Fragment {

    private RecyclerView listview_allbusiness;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RelativeLayout main_bottom_rl,business_bottom_rl;
    private SwipeRefreshLayout pull_refresh;
    private int lastVisibleItem;
    private List<Business> mlist = new ArrayList<>();
    private EditText title_et;
    private AllBusinessAdapter adapter;
    private int i = 0;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.resetAdapter(Common.list_business);
                    pull_refresh.setRefreshing(false);
                    Toast.makeText(getActivity(),"刷新完成", Toast.LENGTH_SHORT).show();
                    title_et.setText("");
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allbusiness_f, container, false);
        pull_refresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_refresh);
        pull_refresh.setColorSchemeResources(R.color.titlebg, R.color.yellowstr);
        pull_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
        title_et = (EditText) getActivity().findViewById(R.id.title_et);
        TextView title_tv = (TextView) getActivity().findViewById(R.id.title_tv);
        ImageView search = (ImageView) getActivity().findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBusiness(Common.list_business);
            }
        });
        title_et.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        title_tv.setVisibility(View.GONE);
        Common.isNeedExit = "yes";
        listview_allbusiness = (RecyclerView) view.findViewById(R.id.listview_allbusiness);
        adapter = new AllBusinessAdapter(getActivity(), Common.list_business);
        listview_allbusiness.setAdapter(adapter);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listview_allbusiness.setLayoutManager(layoutManager);
//        listview_allbusiness.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        listview_allbusiness.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                pull_refresh.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        adapter.setOnItemClickListener(new AllBusinessAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                Common.business = null;
                if (i == 0){
                    Common.business = Common.list_business.get(position);
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
