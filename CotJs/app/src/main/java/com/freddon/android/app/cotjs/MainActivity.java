package com.freddon.android.app.cotjs;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWebView();

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled" })
    private void initWebView() {
         webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MainActivity",consoleMessage.message()+consoleMessage.lineNumber());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.addJavascriptInterface(new JavascriptInterfaceImpl(),"JSHandler");
        webView.loadUrl("file:///android_asset/html/resources/layouts/index.html");

    }



    private class JavascriptInterfaceImpl{

        @JavascriptInterface
        public void async(String config){
            //Current Thread is JavaBridge Thread ,Not UI Thread
            //SO，U can do tasks or return some things;
            final String res=Thread.currentThread().getId()+ Thread.currentThread().getName() + config;
            try {
                JSONObject con=new JSONObject(config);
                final int what=con.optInt("what");
             final  String result= request(con.optString("url"),con.optString("method"),null,null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:CotJSWEBHandler.callback("+what+","+result+",null);");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @JavascriptInterface
        public String sync(String config) {
            //Current Thread is JavaBridge Thread ,Not UI Thread
            //SO，U can do tasks or return some things;
           String result=null;
            try {
                JSONObject con=new JSONObject(config);
                result= request(con.optString("url"),con.optString("method"),null,null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String re=result;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,re,Toast.LENGTH_LONG).show();
                }
            });
            return result;
        }

    }



    public String request(String url, String method, JSONObject headers,JSONObject data){
        StringBuilder strBuild=new StringBuilder();
        InputStream in=null;
        BufferedInputStream bis=null;
        try {
            URL _url=new URL(url);
           HttpURLConnection connection= (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod(method);
            if(connection.getResponseCode()==200){
                in = connection.getInputStream();
                int SIZE=1024;
                bis=new BufferedInputStream(in,2*SIZE);
                byte[] byteArray=new byte[SIZE];
                int tmp=0;
                while((tmp=bis.read(byteArray))!=-1){
                    strBuild.append(new String(byteArray,0,tmp));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bis!=null){
                try {
                    bis.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return strBuild.toString();

    }

}
