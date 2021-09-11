package xyz.devdiscovery.odoocourier.ui.slideshow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.devdiscovery.odoocourier.MainActivity;
import xyz.devdiscovery.odoocourier.R;
import xyz.devdiscovery.odoocourier.ui.btnsndsupport.BtnSndSupportFragment;
import xyz.devdiscovery.odoocourier.ui.home.HomeFragment;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private SwipeRefreshLayout mSwipeRefresh;

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";

    private WebView mWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;


    public SlideshowFragment() {


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mWebView = (WebView) root.findViewById(R.id.webView);

       setUpWebViewDefaults(mWebView);

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if(mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if(takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });



        mWebView.setInitialScale(1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
                handler.proceed();
                // Ignore SSL certificate errors
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Toast.makeText(getActivity(), "Загружаю", Toast.LENGTH_SHORT).show();
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('o_sub_menu')[0].style.display='none'; " +
                        "document.getElementsByClassName('navbar navbar-inverse')[0].style.display='none'; " +
                        //"var head = document.getElementsByClassName('footer-container')[0].style.display='none'; " +
                        "})()");
            }
        };

        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl("https://odoo.pro100systems.com.ua/web#view_type=kanban&model=helpdesk_lite.ticket&menu_id=466&action=643");
       // mWebView.loadUrl("https://odoo.pro100systems.com.ua/web#view_type=kanban&model=helpdesk_lite.ticket&menu_id=466&action=643");
       // mWebView.loadUrl("http://185.14.28.204:8079/web?#view_type=kanban&model=helpdesk_lite.ticket&menu_id=466&action=643");




        return root;

    }

    // Создаю меню для раздела "Расходы"
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu_from_web, MenuInflater inflater) {
        inflater.inflate(R.menu.wev_view_main, menu_from_web);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     //   Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.refresh_page:
                Fragment frag = null;
                mWebView.reload();

            case R.id.back_page:

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }


                return true;


            case R.id.snd_msg_support:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, new BtnSndSupportFragment()).commit();

            // Старый код для перехода, с Fragments на Fragments //
             //   fragment = new BtnSndSupportFragment();
             //   break;


        }



     //   FragmentManager fm = getFragmentManager();
     //   FragmentTransaction fragmentTransaction = fm.beginTransaction();
     //   fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
     //   fragmentTransaction.commit();

    // Старый код для перехода, с Fragments на Fragments. END //

        return super.onOptionsItemSelected(item);
    }

    // END Menu



    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);



        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("javascript:(function() { " +
                "document.getElementsByClassName('o_sub_menu')[0].style.display='none'; " +
                "document.getElementsByClassName('navbar navbar-inverse')[0].style.display='none'; " +
                "})()");

    }




    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if(resultCode == getActivity().RESULT_OK) {
            if(data == null) {
                // If there is not data, then we may have taken a photo
                if(mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    }

    


