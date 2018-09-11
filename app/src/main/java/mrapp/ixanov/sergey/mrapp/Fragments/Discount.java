package mrapp.ixanov.sergey.mrapp.Fragments;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import mrapp.ixanov.sergey.mrapp.OnBackPressedListener;
import mrapp.ixanov.sergey.mrapp.R;
import mrapp.ixanov.sergey.mrapp.WebClient.WebClient;

public class Discount extends Fragment implements FireworkyPullToRefreshLayout.OnRefreshListener, OnBackPressedListener {

    WebView webView;
    private FireworkyPullToRefreshLayout mPullToRefreshLayout;

    public static Discount newInstance() {
        Discount fragment = new Discount();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discounts, container, false);
        mPullToRefreshLayout = (FireworkyPullToRefreshLayout) v.findViewById(R.id.pullToRefresh);
        webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        if (savedInstanceState != null){
            webView.restoreState(savedInstanceState);
        } else {

        webView.loadUrl("http://mrcreative.ru");
        webView.setWebViewClient(new WebClient());
        mPullToRefreshLayout.setOnRefreshListener(this);
        }
        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Останавливаем обновление:
                mPullToRefreshLayout.setRefreshing(false);
                webView.loadUrl(webView.getUrl());
                ;
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }
}