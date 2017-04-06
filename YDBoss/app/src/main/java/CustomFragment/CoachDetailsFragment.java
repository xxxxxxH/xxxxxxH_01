package CustomFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import bean.Common;
import tools.GlideRoundTransform;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachDetailsFragment extends Fragment{

    private ImageView  coachdetails_ima;
    private TextView coach_tv_name,coach_tv_sex,coach_tv_workyear,coach_tv_belong,coach_tv_geyan,coach_tv_details;
    private Button coach_btn;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coachdetails, container, false);
        initView();
        return view;
    }

    private void initView() {
        coachdetails_ima = (ImageView) view.findViewById(R.id.coachdetails_ima);
        coach_tv_name = (TextView) view.findViewById(R.id.coach_tv_name);
        coach_tv_sex = (TextView) view.findViewById(R.id.coach_tv_sex);
        coach_tv_workyear = (TextView) view.findViewById(R.id.coach_tv_workyear);
        coach_tv_belong = (TextView) view.findViewById(R.id.coach_tv_belong);
        coach_tv_geyan = (TextView) view.findViewById(R.id.coach_tv_geyan);
        coach_tv_details = (TextView) view.findViewById(R.id.coach_tv_details);
        coach_btn = (Button) view.findViewById(R.id.coach_btn);
        Glide.with(getActivity()).load(Common.CoachDetails.getImg().toString()).transform(new GlideRoundTransform(getActivity(), 100)).into(coachdetails_ima);
        coach_tv_name.setText("教练名称：" + Common.CoachDetails.getName());
        coach_tv_sex.setText("性别：" + Common.CoachDetails.getSex());
        coach_tv_workyear.setText("工作时间：" + Common.CoachDetails.getWorkyear() + "年");
        coach_tv_belong.setText("所属场馆：" + Common.CoachDetails.getB_name());
        coach_tv_geyan.setText(Common.CoachDetails.getMotto().toString());
        coach_tv_details.setText(Common.CoachDetails.getDetails().toString());
        coach_btn.setText(Common.CoachDetails.getName() + "的课程");
        coach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content, new CoachLessonListFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
