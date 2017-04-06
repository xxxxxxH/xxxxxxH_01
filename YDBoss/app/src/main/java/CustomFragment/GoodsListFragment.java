package CustomFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yidong.xh.ydboss.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.GoodsListAdapter;
import bean.Common;
import bean.Goods;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class GoodsListFragment extends Fragment {

    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/BusinessGoogsGetPK";
    private List<Goods> lits_goods = new ArrayList<Goods>();
    private RecyclerView goodslist_re;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goodslist, container, false);
        goodslist_re = (RecyclerView) view.findViewById(R.id.goodslist_re);
        getGoods();
        return view;
    }

    public void getGoods() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("strBusinessID", "" + Common.business.getId());
        params.addBodyParameter("strID", "");
        params.addBodyParameter("strName", "");
        HttpUtils httpUtils = new HttpUtils(5000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            Message msg = new Message();
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.contains("nodata") || responseInfo.result.contains("failed")){
                    msg.what = -1;
                }else {
                    msg.what = 1;
                    msg.obj = responseInfo.result;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                msg.what = -1;
                handler.sendMessage(msg);
            }
        });
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    try {
                        Gson gson = new Gson();
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<Goods>>() {}.getType();
                        lits_goods = gson.fromJson(result, type);
                        GoodsListAdapter adapter = new GoodsListAdapter(getActivity(), lits_goods);
                        goodslist_re.setAdapter(adapter);
                        goodslist_re.setLayoutManager(new LinearLayoutManager(getActivity()));
                        goodslist_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
                        adapter.setItemClickListener(new GoodsListAdapter.ItemClickListener() {
                            @Override
                            public void ItemClick(View view, int position) {
                                Toast.makeText(getActivity(), "点击的是" + position, Toast.LENGTH_SHORT).show();
                                Common.Goods = null;
                                Common.Goods = lits_goods.get(position);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.content, new GoodsDetailsFragment());
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });
                    }catch (Exception e){

                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "获取商品列表失败（原因：该商家未发布商品或者网络连接失败）", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
