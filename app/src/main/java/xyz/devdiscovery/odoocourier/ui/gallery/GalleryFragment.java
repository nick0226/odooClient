package xyz.devdiscovery.odoocourier.ui.gallery;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import xyz.devdiscovery.odoocourier.R;
import xyz.devdiscovery.odoocourier.ui.salles.SallesFragment;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements
        AdapterView.OnItemSelectedListener{

    private GalleryViewModel galleryViewModel;



    private final int RQS_LOAD_IMAGE = 0;
    private final int RQS_SEND_EMAIL = 1;
    //private EditText mEmailAddressEditText;
    //private EditText mEmailSubjectEditText;
    private EditText mEmailTextEditText;
    private ArrayList<Uri> mUriList = new ArrayList<>();
    private ArrayAdapter<Uri> mFileListAdapter;
    private String httpURL = "https://www.andr-discovery.xyz/csv_table/mail.channel.csv";
    private Spinner spMainSelectCategory;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private Button buttonSend;
    private String arrList;
    private String ch_common = "general@pro100systems.com.ua";
    private String ch_official = "tabel@pro100systems.com.ua";
    private String ch_operational = "messages@pro100systems.com.ua";
    private String ch_auto1 = "avto1@pro100systems.com.ua";
    private String ch_auto2 = "avto2@pro100systems.com.ua";
    private final String techaud = "techaudit@pro100systems.com.ua";

    View.OnClickListener addFileOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
                    requestPerms();
                }

                else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RQS_LOAD_IMAGE);
                }
            }


            // Intent intent = new Intent(Intent.ACTION_PICK,
            //        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // startActivityForResult(intent, RQS_LOAD_IMAGE);
        }
    };




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);



     //   final TextView textView = root.findViewById(R.id.text_gallery);
     //   galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
     //       @Override
     //       public void onChanged(@Nullable String s) {
     //           textView.setText(s);
     //       }
     //   });



     //   mEmailAddressEditText = (EditText) root.findViewById(R.id.email_address);
     //   mEmailSubjectEditText = (EditText) root.findViewById(R.id.email_subject);
        mEmailTextEditText = (EditText) root.findViewById(R.id.editNote);
        Button addFileButton = (Button) root.findViewById(R.id.openGallary);
        Button sendButton = (Button) root.findViewById(R.id.sendMassanges);
        addFileButton.setOnClickListener(addFileOnClickListener);
        buttonSend = (Button) root.findViewById(R.id.sendMassanges);
        spMainSelectCategory = (Spinner) root.findViewById(R.id.selectChannel);

        mFileListAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mUriList);
     //   ListView filesListView = (ListView) root.findViewById(R.id.filelist);
     //   filesListView.setAdapter(mFileListAdapter);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};
        try {
            URL url12 = new URL(httpURL);
            HttpURLConnection connection = (HttpURLConnection) url12.openConnection();
            InputStreamReader csvStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader buff = new BufferedReader(csvStreamReader);

            CSVReader reader = new CSVReader(csvStreamReader);
            for (;;) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1;  i < list.size(); i++) {
            categoryList.add(list.get(i)[1]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,
                categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        spMainSelectCategory.setAdapter(adapter);
        spMainSelectCategory.setOnItemSelectedListener(this);


        // Для добавления загрузки XML автоматической загрузкой, удалить этот код
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadPageTask().execute(httpURL);
        } else {
            Toast.makeText(getActivity(), "Не могу загрузить список, отсуствует интернет", Toast.LENGTH_LONG).show();
        }
        // --- END



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_LOAD_IMAGE:
                    Uri imageUri = data.getData();
                    mUriList.add(imageUri);
                    mFileListAdapter.notifyDataSetChanged();
                    break;
                case RQS_SEND_EMAIL:
                    break;
            }
        }
    }

    public void emptyField () {

        Toast.makeText(getActivity(), "Поле \"Примечание\" не может быть пустым, заполните его.", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onItemSelected(final AdapterView<?> arg0, final View arg1, final int arg2,
                               final long arg3) {
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (categoryList.get(arg2).equals("Общий")) {
                    arrList=ch_common;
                } else
                if (categoryList.get(arg2).equals("Табельный")) {
                    arrList=ch_official;
                } else
                if (categoryList.get(arg2).equals("Оперативный")) {
                    arrList=ch_operational;
                } else
                if (categoryList.get(arg2).equals("Автомобильный 1")){
                    arrList=ch_auto1;
                } else
                if (categoryList.get(arg2).equals("Автомобильный 2")){
                    arrList=ch_auto2;
                } else
                if (categoryList.get(arg2).equals("Техаудит")){
                    arrList=techaud;
                }



                //   String emailAddress = mEmailAddressEditText.getText().toString();
                //   String emailSubject = mEmailSubjectEditText.getText().toString();
                String emailText = mEmailTextEditText.getText().toString();
                if (mEmailTextEditText.getText().toString().isEmpty())
                {
                    emptyField();
                    return;
                }
                //   String[] emailAddressList = {emailAddress};

                Intent intent = new Intent();
                //   intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
                //   intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{arrList});
                intent.putExtra(Intent.EXTRA_TEXT, emailText);

                if (mUriList.isEmpty()) {
                    // посылаем письмо без прикрепленной картинки
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                } else if (mUriList.size() == 1) {
                    // посылаем письмо с одной картинкой
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, mUriList.get(0));
                    intent.setType("image/*");
                } else {
                    // посылаем письмо с несколькими картинками
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mUriList);
                    intent.setType("image/*");
                }
                // startActivity(Intent.createChooser(intent, "Выберите программу:"));

                intent.setType("message/rfc822");

                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmailExternal");
                Toast toast = Toast.makeText(getActivity(), "Запускаю клиент Gmail", Toast.LENGTH_LONG);
                toast.show();
                try {
                    startActivity(intent);

                } catch (ActivityNotFoundException ex) {
                    // GMail not found
                }


                mEmailTextEditText.setText("");
                mUriList.clear();

            }
        });
        }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(getActivity(), permissions,123);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Remount read XML file.
    private class DownloadPageTask extends
            AsyncTask<String, Void, List<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            try {
                   //return downloadOneUrl(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categoryList;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            spMainSelectCategory.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row, result));

            super.onPostExecute(result);
        }
    }

}
