package com.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    public int splashLoaded = 0;
    public int splashVisible = 0;
    private WebView webview;

    private class myWebViewClient extends WebViewClient {
        private myWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("file:///android_asset/noconnection.html");
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (MainActivity.this.splashLoaded == 0) {
                MainActivity.this.visible();
                MainActivity.this.splashVisible = 1;
            }
        }

        public void onPageFinished(WebView view, String url) {
            if (MainActivity.this.splashVisible == 1) {
                MainActivity.this.unvisible();
                MainActivity.this.splashVisible = 0;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.webview = (WebView) findViewById(R.id.webView1);
        WebSettings websettings = this.webview.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setSaveFormData(false);
        this.webview.loadUrl("http://mysitename.com/");
        this.webview.setHorizontalScrollBarEnabled(false);
        this.webview.setScrollBarStyle(33554432);
        this.webview.setBackgroundColor(128);
        this.webview.setWebViewClient(new myWebViewClient());
        this.webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                MainActivity.this.startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.splashLoaded = 1;
            }
        }, 3000);
    }

    public void onBackPressed() {
        if (this.webview.canGoBack()) {
            this.webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void visible() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
        ((WebView) findViewById(R.id.webView1)).setVisibility(10);
        bar.setVisibility(0);
    }

    public void unvisible() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
        ((WebView) findViewById(R.id.webView1)).setVisibility(0);
        bar.setVisibility(10);
    }
}
