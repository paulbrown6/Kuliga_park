package com.app.kuliga.ui.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class WebActivity extends AppCompatActivity {

    private String TAG = "WEB";
    private ImageButton buttonCancel;
    private WebView viewWeb;
    private ProgressBar progress;
    private boolean fail = false;

    @SuppressLint({"WrongViewCast", "SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_fulljava);
        buttonCancel = findViewById(R.id.webview_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewWeb = findViewById(R.id.webView);
        viewWeb.setVisibility(View.INVISIBLE);
        progress = findViewById(R.id.webView_progress);
        progress.setVisibility(View.VISIBLE);

        viewWeb.setInitialScale(100);
        LinearLayout errorLoad = findViewById(R.id.webview_load_error);
        WebSettings webSettings = viewWeb.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager.getInstance().setAcceptThirdPartyCookies(viewWeb, true);
        webSettings.setAllowFileAccess(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        viewWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    WebActivity.this.fail = true;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                final SslErrorHandler handlerFinal;
                handlerFinal = handler;
                int message ;
                switch(error.getPrimaryError()) {
                    case SslError.SSL_DATE_INVALID:
                        message = R.string.notification_error_ssl_date_invalid;
                        break;
                    case SslError.SSL_EXPIRED:
                        message = R.string.notification_error_ssl_expired;
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = R.string.notification_error_ssl_idmismatch;
                        break;
                    case SslError.SSL_INVALID:
                        message = R.string.notification_error_ssl_invalid;
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = R.string.notification_error_ssl_not_yet_valid;
                        break;
                    case SslError.SSL_UNTRUSTED:
                        message = R.string.notification_error_ssl_untrusted;
                        break;
                    default:
                        message = R.string.notification_error_ssl_cert_invalid;
                }

                Log.e(TAG, "OnReceivedSslError handel.proceed()");

                final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                builder.setMessage(message);

                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handlerFinal.proceed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handlerFinal.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
                if (fail) {
                    onError(view, errorLoad);
                } else {
                    viewWeb.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "onPageFinished URL: " + url);
                if (url.contains(ApiCall.getInstance().getReturnUrl())){
                    MainViewModel.setPayResultOk(1);
                    finish();
                }
                if (url.contains(ApiCall.getInstance().getFailUrl())){
                    MainViewModel.setPayResultOk(2);
                    finish();
                }
            }
        });
        viewWeb.loadUrl(MainViewModel.getPayURL());
        findViewById(R.id.webview_button_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fail = false;
                progress.setVisibility(View.VISIBLE);
                errorLoad.setVisibility(View.GONE);
                viewWeb.loadUrl(MainViewModel.getPayURL());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        viewWeb.onPause();
        viewWeb.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewWeb.resumeTimers();
        viewWeb.onResume();
    }

    @Override
    public void onBackPressed() {
        if (viewWeb.canGoBack() && !fail) {
            viewWeb.goBack();
            return;}
        super.onBackPressed();
    }

    private void onError(View view, LinearLayout errorLoad){
        view.setVisibility(View.GONE);
        errorLoad.setVisibility(View.VISIBLE);
    }
}