package CustomFragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import adapter.ViewPagerAdapter;
import bean.Common;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class GoodsDetailsFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private View view;
    private ViewPager goods_vp;
    private LinearLayout dot_ll;
    private TextView goodsitemdetails_tv_name,goodsitemdetails_tv_price,goodsitemdetails_tv_original_price,goodsitemdetails_tv_hours,goodsitemdetails_tv_explain,goodsitemdetails_tv_details;
    private ImageView[] dot;
    private  String[] pics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.goodsdetails, container, false);
        initView();
        return view;
    }

    private void initView() {
        Common.WhichFragemntNeedViewPagerAdapter = "spacedetails";
        goods_vp = (ViewPager) view.findViewById(R.id.goods_vp);
        dot_ll = (LinearLayout) view.findViewById(R.id.dot);
        goodsitemdetails_tv_name = (TextView) view.findViewById(R.id.goodsitemdetails_tv_name);
        goodsitemdetails_tv_price = (TextView) view.findViewById(R.id.goodsitemdetails_tv_price);
        goodsitemdetails_tv_original_price = (TextView) view.findViewById(R.id.goodsitemdetails_tv_original_price);
        goodsitemdetails_tv_hours = (TextView) view.findViewById(R.id.goodsitemdetails_tv_hours);
        goodsitemdetails_tv_explain = (TextView) view.findViewById(R.id.goodsitemdetails_tv_explain);
        goodsitemdetails_tv_details = (TextView) view.findViewById(R.id.goodsitemdetails_tv_details);
        String img = Common.Goods.getImg();
        if (!img.equals("")){
            pics = img.split(";");
            dot = new ImageView[pics.length];
            for (int i = 0; i < pics.length; i++){
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                params.setMargins(10, 0, 10, 0);
                imageView.setLayoutParams(params);
                dot[i] = imageView;
                dot_ll.addView(dot[i]);
                if (i == 0){
                    dot[i].setBackgroundResource(R.mipmap.point_select);
                }else {
                    dot[i].setBackgroundResource(R.mipmap.page_select);
                }
            }
            goods_vp.setOnPageChangeListener(this);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), pics);
            goods_vp.setAdapter(adapter);
            goods_vp.setCurrentItem(0);
        }
        goodsitemdetails_tv_name.setText(Common.Goods.getName().toString());
        goodsitemdetails_tv_price.setText("现价：" + Common.Goods.getPrice());
        SpannableStringBuilder builder = new SpannableStringBuilder(goodsitemdetails_tv_price.getText().toString());
        ForegroundColorSpan txtcolor = new ForegroundColorSpan(Color.parseColor("#f6983e"));
        builder.setSpan(txtcolor, 2, (goodsitemdetails_tv_price.getText().toString().length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsitemdetails_tv_price.setText(builder);
        goodsitemdetails_tv_original_price.setText("原价：" + Common.Goods.getOriginal_price());
        goodsitemdetails_tv_original_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        String hours = Common.Goods.getHours().toString();
        if (!hours.equals("") && hours != null) {
            try {
                String [] time = hours.split(";");
                int starttime = Integer.valueOf(time[0]) / 60;
                int endtime = Integer.valueOf(time[1]) / 60;
                int startmiu = Integer.valueOf(time[0]) % 60;
                int endmiu = Integer.valueOf(time[1]) % 60;
                if (startmiu == 0 && endmiu == 0) {
                    goodsitemdetails_tv_hours.setText("使用时间: " + starttime + ":00" + "-" + endtime + ":00");
                }else {
                    if (startmiu == 0 && endmiu != 0) {
                        goodsitemdetails_tv_hours.setText("使用时间: " + starttime + ":00" + "-" + endtime + ":" + endmiu);
                    }else if (endmiu % 60 == 0 && startmiu != 0) {
                        goodsitemdetails_tv_hours.setText("使用时间: " + starttime + ":" + startmiu + "-" + endtime + ":00");
                    }else {
                        goodsitemdetails_tv_hours.setText("使用时间: " + starttime + ":" + startmiu + "-" + endtime + ":" + endmiu);
                    }
                }
            } catch (Exception e) {
                goodsitemdetails_tv_hours.setText("使用时间: 9:00-18:00");
            }
        }else {
            goodsitemdetails_tv_hours.setText("使用时间: 9:00-18:00");
        }
        int at_least_buy = Common.Goods.getAt_least_buy();
        int limit_buy = Common.Goods.getLimit_buy();
        if (at_least_buy != 0 && limit_buy != 0) {
            goodsitemdetails_tv_explain.setText("购买说明: " + at_least_buy + "份起购" + "限购" + limit_buy + "份");
        }else if (at_least_buy == 0 && limit_buy != 0) {
            goodsitemdetails_tv_explain.setText("购买说明: " + "限购" + limit_buy + "份");
        }else if (at_least_buy != 0 && limit_buy == 0) {
            goodsitemdetails_tv_explain.setText("购买说明: " + at_least_buy + "份起购");
        }else {
            goodsitemdetails_tv_explain.setText("购买说明: 无购买限制");
        }
        goodsitemdetails_tv_details.setText(Common.Goods.getDetails().toString());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < pics.length; i++) {
            if (i == position % pics.length) {
                dot[i].setBackgroundResource(R.mipmap.point_select);
            } else {
                dot[i].setBackgroundResource(R.mipmap.page_select);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
