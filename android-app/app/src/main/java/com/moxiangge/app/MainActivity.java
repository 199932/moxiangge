package com.moxiangge.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.util.TypedValue;

/* ============================================================
   墨香阁 APP - 主界面（网站容器）
   打开后直接加载线上网站地址
   ============================================================ */
public class MainActivity extends Activity {

    // ⚠️ 部署成功后请把这里替换为你的真实网址，如 https://moxiangge.onrender.com
    // 例如：https://moxiangge.onrender.com
    private static final String ONLINE_URL = "https://moxiangge.onrender.com";
    private static final String FALLBACK_URL = "https://199932.github.io/";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 主布局（竖屏文字背景）
        LinearLayout root = new LinearLayout(this);
        root.setBackgroundColor(Color.parseColor("#F5ECD9"));
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // 标题栏
        TextView titleBar = new TextView(this);
        titleBar.setText("墨 香 阁");
        titleBar.setTextColor(Color.parseColor("#8B2500"));
        titleBar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        titleBar.setGravity(Gravity.CENTER);
        titleBar.setPadding(0, dp(14), 0, dp(14));
        titleBar.setBackgroundColor(Color.parseColor("#E8DCC0"));
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(titleBar, titleLp);

        // WebView 加载线上网站
        webView = new WebView(this);
        LinearLayout.LayoutParams wvLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(wvLp);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setAllowFileAccess(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setBuiltInZoomControls(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError error) {
                super.onReceivedError(view, req, error);
                // 加载失败时显示错误信息
                runOnUiThread(() -> {
                    showErrorInfo();
                });
            }
        });

        root.addView(webView);
        setContentView(root);

        // 加载线上网站
        webView.loadUrl(ONLINE_URL);
    }

    private void showErrorInfo() {
        // 显示提示信息，告诉用户网站还没部署好
        String html =
            "<html><body style=\"background:#F5ECD9;color:#2C2416;font-family:serif;text-align:center;padding:40px 20px;\">" +
            "<h1 style=\"color:#8B2500;font-size:28px;margin-top:60px;\">墨香阁</h1>" +
            "<p style=\"font-size:16px;color:#5C4A32;line-height:2;margin-top:20px;\">" +
            "  APP 正在加载线上网站…<br><br>" +
            "  若长时间无法打开，请检查网络或联系管理员。<br><br>" +
            "  当前目标网址：<br>" +
            "<span style=\"color:#8B2500;font-size:14px;\">" + ONLINE_URL + "</span>" +
            "</p></body></html>";
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}
