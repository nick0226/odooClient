package xyz.devdiscovery.odoocourier.ui.infopanel;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import xyz.devdiscovery.odoocourier.R;

public class InfoPanelFragment extends Fragment {
private InfoPanelModel infoPanelModel;
private WebView infopanel;


    public InfoPanelFragment() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        infoPanelModel = ViewModelProviders.of(this).get(InfoPanelModel.class);
        View root = inflater.inflate(R.layout.fragment_infopanel, container, false);


        infopanel = (WebView) root.findViewById(R.id.infopanel_view);
        infopanel.getSettings().setJavaScriptEnabled(true);


        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Toast.makeText(getActivity(), "Страница загружена!", Toast.LENGTH_SHORT).show();
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('o_sub_menu')[0].style.display='none'; " +
                        "document.getElementsByClassName('navbar navbar-inverse')[0].style.display='none'; " +
                        "})()");
            }

        };

        infopanel.setWebViewClient(webViewClient);
        infopanel.loadUrl("https://odoo.pro100systems.com.ua/web#id=&view_type=form&model=board.board&menu_id=454&action=632");
        // infopanel.loadUrl("http://185.14.28.204:8079/web?#id=&view_type=form&model=board.board&menu_id=454&action=632");


        return root;
    }

    // Создаю меню для раздела "Информационная панель"
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu_from_web, MenuInflater inflater) {
        inflater.inflate(R.menu.info_panel_menu, menu_from_web);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refresh_page:

                infopanel.reload();



            case R.id.back_page:

                if (infopanel.canGoBack()) {
                    infopanel.goBack();

                }

                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    // END Menu

}
