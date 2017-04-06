package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import bean.Common;

/**
 * Created by wjkj__xh on 2017/3/2.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] imgs;

    public ViewPagerAdapter(Context context, String[] imgs){
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int posi = position % imgs.length;
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(imgs[posi]).into(imageView);
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(100, 200);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(para);
        if (Common.WhichFragemntNeedViewPagerAdapter.equals("spacedetails")){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        container.addView(imageView, 0);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
