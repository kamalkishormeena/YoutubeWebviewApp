package com.kkmeena;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.support.design.widget.CoordinatorLayout;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.webkit.WebSettings;
import static com.kkmeena.R.color.colorAccent;
import static com.kkmeena.R.color.colorPrimary;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener  {

    private static final int TIME_DELAY = 1000;
    private static long backpressed;
    private WebView mWebView;
    public SwipeRefreshLayout swipe;
    public CoordinatorLayout coordinatorLayout;
    private LayoutInflater inflater;

    private String mTrackUrlChange;
    private String mTitle = "";
    private String mUrl = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF0000")));

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            if(savedInstanceState!=null)
            {
                ((WebView)findViewById(R.id.webview)).restoreState(savedInstanceState);
            }
            mWebView = (WebView) findViewById(R.id.webview);
            swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
            swipe.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) MainActivity.this);
            swipe.setColorSchemeColors(getColor(colorPrimary));
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);
            mWebView.getSettings().setBuiltInZoomControls(false);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            //mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            //mWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            if (Build.VERSION.SDK_INT >= 19) {
                WebView webView = mWebView;
                WebView.setWebContentsDebuggingEnabled(true);
            }
            CookieManager.getInstance().setAcceptCookie(true);


            mWebView.loadUrl(getString(R.string.yt));
            mUrl= mWebView.getUrl();
            getSupportActionBar().setTitle("Kamal Kishor");

            //getSupportActionBar().setSubtitle(R.string.Youtube);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                       mUrl=view.getUrl();
                    //  getSupportActionBar().setSubtitle(mUrl);
                    swipe.setRefreshing(true);
                }

                public void onPageFinished(WebView view, String url) {
                       mUrl = view.getUrl();
                    mTitle = view.getTitle();
                    mTrackUrlChange = url;
                      //getSupportActionBar().setTitle(mTitle);
                     // getSupportActionBar().setSubtitle(mUrl);
                    swipe.setRefreshing(false);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    return super.shouldOverrideUrlLoading(view, url);
                }
            });

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setAppCacheEnabled(true);
            //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setSavePassword(true);
            webSettings.setSaveFormData(true);
            webSettings.setEnableSmoothTransition(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);

        }    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);


    }

    private void onUp(){

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mWebView.canGoBack()){
            mWebView.goBack();
        } else if (backpressed+TIME_DELAY>System.currentTimeMillis()){
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), R.string.back_tast,Toast.LENGTH_SHORT).show();
        }
        backpressed=System.currentTimeMillis();
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile!=null&&mobile.isConnectedOrConnecting())||(wifi!=null&&wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else {
            return false;
        }
    }

    public AlertDialog.Builder buildDialog(Context c){
        final AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle(getString(R.string.nonet));
        builder.setMessage(getString(R.string.nonet_msg));
        builder.setPositiveButton(getString(R.string.Retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!isConnected(MainActivity.this))
                    buildDialog(MainActivity.this).show();
                else {

                }
               // Intent intent=getIntent();
                //finish();
                //startActivity(intent);
            }
        });
        builder.setCancelable(true);
        return builder;
    }

    public AlertDialog.Builder buildDialog2(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle(getString(R.string.netLost));
        builder.setMessage(getString(R.string.netLost_msg));
        builder.setPositiveButton(getString(R.string.Retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
               if(!isConnected(MainActivity.this)) buildDialog2(MainActivity.this).show();
               else onRefresh();
            }
        });
        builder.setCancelable(false);
        return builder;
    }

    @SuppressLint("ResourceType")
    public AlertDialog.Builder buildDialog3(Context c){

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
               // builder.setIcon(R.drawable.nav_about);
                //builder.setTitle("About");
                builder.setView(inflater.inflate(R.layout.activity_about, null));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                builder.create();
                builder.setCancelable(true);
        return builder;
    }


    @Override
    public void onRefresh() {
        mWebView.reload();
        swipe.setRefreshing(true);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.Refresh), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings: return true;
            case R.id.action_share:
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType(getString(R.string.setType));
                sharingIntent.putExtra("android.intent.extra.SUBJECT", mTitle);
                sharingIntent.putExtra("android.intent.extra.TEXT", mTrackUrlChange);
                sharingIntent.putExtra("android.intent.extra.STREAM", mTrackUrlChange);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareactivity)));
                break;

            case R.id.action_refresh:
                if(isConnected(MainActivity.this))onRefresh();
                else buildDialog2(MainActivity.this).show();
                break;

            case R.id.about:
               buildDialog3(MainActivity.this).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

   // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_yt && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.yt));
             getSupportActionBar().setSubtitle(R.string.Youtube);


         } else if (id == R.id.nav_fb && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.fb));
             getSupportActionBar().setSubtitle(R.string.Facebook);


         } else if (id == R.id.nav_twitter && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.twitter));
             getSupportActionBar().setSubtitle(R.string.Twitter);


         } else if (id == R.id.nav_goplus && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.gplus));
             getSupportActionBar().setSubtitle(R.string.GooglePlus);


         } else if (id == R.id.nav_insta && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.insta));
             getSupportActionBar().setSubtitle(R.string.Instagram);


         } else if (id == R.id.nav_blog && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.blog));
             getSupportActionBar().setSubtitle(R.string.Blog);



         } else if (id == R.id.nav_web && (isConnected(MainActivity.this))) {
             mWebView.loadUrl(getString(R.string.website));
             getSupportActionBar().setSubtitle(R.string.Website);


         } else if (id == R.id.nav_share) {
             Intent sharingIntent = new Intent("android.intent.action.SEND");
             sharingIntent.setType(getString(R.string.setType));
             sharingIntent.putExtra("android.intent.extra.SUBJECT", (getString(R.string.share_sub)));
             sharingIntent.putExtra("android.intent.extra.TEXT", (getString(R.string.share_text)));
             startActivity(Intent.createChooser(sharingIntent, (getString(R.string.shareactivity))));
        } else {
             buildDialog(MainActivity.this).show();
         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
