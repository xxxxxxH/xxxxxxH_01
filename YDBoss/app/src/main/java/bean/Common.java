package bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjkj__xh on 2017/2/27.
 */

public class Common {
    public static List<Business> list_business = new ArrayList<Business>();//所有商家数据
    public static String WhichBusiness = "";//商家类型（所有商家/优惠商家）
    public static List<Business> favorable_business = new ArrayList<Business>();//优惠商家数据
    public static Business business = null;//点击列表获取商家信息
    public static List<LessonSpace> list_lessonn = new ArrayList<LessonSpace>();//商家课程预约
    public static String[] list_space = new String[]{};//场地类商家预约
    public static String isNeedExit = "yes";//是否是需要退出的界面
    public static boolean isExit = true;//是否要退出
    public static String type = "";//预约类型
    public static SpaceList spaceList = null;//场地预约item
    public static String WhichFragemntNeedViewPagerAdapter = "";//区分使用ViewpagerAdapter的Fragment
    public static CoachList CoachDetails = null;//教练详细信息
    public static CoachLesson Coachlesson = null;//课程详情
    public static Goods Goods = null;//商品详情
    public static Recruit Recruit = null;//招聘详情
    public static String isBusinessInfo = "yes";//是否是商家主页
}
