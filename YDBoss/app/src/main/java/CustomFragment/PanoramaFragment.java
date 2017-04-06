package CustomFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import bean.Common;

/**
 * Created by wjkj__xh on 2017/3/2.
 */

public class PanoramaFragment extends Fragment {

    private WebView panorama_webview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panorama, container, false);
        panorama_webview = (WebView) view.findViewById(R.id.panorama_webview);
        TextView textView = (TextView) getActivity().findViewById(R.id.title_tv);
        String url = Common.business.getWeb360().toString();
        panorama_webview.getSettings().setJavaScriptEnabled(true);
        panorama_webview.getSettings().setDomStorageEnabled(true);
        panorama_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        panorama_webview.loadUrl(url);
        return view;
    }

    @Override
    public void onDestroy() {
        panorama_webview.stopLoading();
        panorama_webview.destroy();
        super.onDestroy();
    }
}
