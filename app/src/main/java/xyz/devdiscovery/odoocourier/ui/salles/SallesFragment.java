package xyz.devdiscovery.odoocourier.ui.salles;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import au.com.bytecode.opencsv.CSVReader;
import xyz.devdiscovery.odoocourier.JavaMailAPI;
import xyz.devdiscovery.odoocourier.JavaMailApi_Salles;
import xyz.devdiscovery.odoocourier.R;
import xyz.devdiscovery.odoocourier.ui.home.HomeFragment;


import static android.app.Activity.RESULT_OK;
import static xyz.devdiscovery.odoocourier.ui.settings.MySettingsFragment.APP_PREFERENCES;


public class SallesFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    private SallesViewModel sallesViewModel;
    private EditText editSummaryMassange;
    private EditText editNoteMassenge;
    private EditText howSendMail;
    private Button buttonSend;
    private String httpURL = "https://www.andr-discovery.xyz/csv_table/crm.team.csv";
    private Spinner spMainSelectCategory;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private Button TakePicture;
    private Button upload;

    // тест переменная для проверки почты
    public String test_mail = "devdiscovery@yahoo.com";

    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private ImageView imageView;
    private Uri photoURI;
    private String emailAdr = "@pro100systems.com.ua";

    private String production = "tehno";
    private String service = "infosup";

    private String arrList;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_TOOL = "Tool";
    public static String MY_EMAIL = "";
    TextView MyEmailView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sallesViewModel = ViewModelProviders.of(this).get(SallesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_salles, container, false);

        buttonSend = (Button) root.findViewById(R.id.sendMassanges);
        editSummaryMassange = (EditText) root.findViewById(R.id.editSummary);
        editNoteMassenge = (EditText) root.findViewById(R.id.editNote);
        imageView = root.findViewById(R.id.imageViewSalles);
        spMainSelectCategory = (Spinner) root.findViewById(R.id.selectChannel);
        TakePicture = (Button) root.findViewById(R.id.buttonCamera);
        MyEmailView = root.findViewById(R.id.my_mail);

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(APP_PREFERENCES_SETTINGS)) {
            // выводим данные в TextView
            MyEmailView.setText(mSettings.getString(APP_PREFERENCES_SETTINGS,
                    ""));
        }
        if (MyEmailView!=null){
            MY_EMAIL = MyEmailView.getText().toString();
        }


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




        TakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
                        requestPerms();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
                        requestPerms();
                    }

                    else {
                        dispatchTakePictureIntent();
                        //Toast toast = Toast.makeText(getActivity(), "Запуск камеры для снимка", Toast.LENGTH_LONG);
                        //toast.show();

                    }
                }

                // dispatchTakePictureIntent();
                // Toast toast = Toast.makeText(getActivity(), "Запуск камеры для снимка",Toast.LENGTH_LONG);
                // toast.show();
            }
        });


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

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(getActivity(), permissions,123);
        }
    }

    public void onItemSelected(final AdapterView<?> arg0, final View arg1, final int arg2,
                               final long arg3) {


        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (categoryList.get(arg2).equals("Производственная группа")) {
                    arrList=production;
                } else
                if (categoryList.get(arg2).equals("Группа информационно-технической поддержки")) {
                    arrList=service;
                }

                // вызов метода, для отправки сообщения
                sendMail();

                // старый код для работы с почтой Gmail
/*
                String subject = editSummaryMassange.getText().toString();
                if (subject.isEmpty())
                {
                    emptyFieldResume();
                    return;
                }
                String message = editNoteMassenge.getText().toString();
                if (message.isEmpty())
                {
                    emptyFieldNote();
                    return;
                }



                if (categoryList.get(arg2).equals("Производственная группа")) {
                    arrList=production;
                } else
                if (categoryList.get(arg2).equals("Группа информационно-технической поддержки")) {
                    arrList=service;
                }

                //email.putExtra(Intent.EXTRA_EMAIL,  arrList + emailAdr);
                //email.putExtra(Intent.EXTRA_EMAIL, new String[]{arrList+emailAdr});

                Intent email = new Intent(Intent.ACTION_SEND);
                //email.putExtra(Intent.EXTRA_EMAIL, new String[]{"expense@pro100systems.com.ua"});
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{arrList+emailAdr});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.putExtra(Intent.EXTRA_STREAM, photoURI);

                email.setType("message/rfc822");

                email.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmailExternal");
                Toast toast = Toast.makeText(getActivity(), "Запускаю клиент Gmail", Toast.LENGTH_LONG);
                toast.show();
                try {
                    startActivity(email);

                } catch (ActivityNotFoundException ex) {
                    // GMail not found
                }





                // Сброс данных с полей
                editSummaryMassange.setText("");
                editNoteMassenge.setText("");
                // imageView.setImageDrawable(R.drawable.icon);
                // imageView.setImageResource(R.drawable.icon_odoo);
                imageView.setImageBitmap(null);
*/
            }
        });

    }


    // метод для отправки сообщений
    private void sendMail() {




        String mail = arrList+emailAdr;
        String message = editNoteMassenge.getText().toString();
        String subject = editSummaryMassange.getText().toString().trim();

        // условие на проверку заполненных полей, если поля не заполнены не даст отправить сообщение
        if (subject.isEmpty())
        {
            emptyFieldResume();
            return;
        }

        if (message.isEmpty())
        {
            emptyFieldNote();
            return;
        }

        //Send Mail
        JavaMailApi_Salles javaMailApi_salles = new JavaMailApi_Salles(getActivity(),mail,subject,message);

        javaMailApi_salles.execute();
        // editNoteMassenge.setText("");
        // editSummaryMassange.setText("");

    }


    public void emptyFieldResume (){
        Toast.makeText(getActivity(), "Заполните поле \"Резюме\" ", Toast.LENGTH_LONG).show();
    }

    public void emptyFieldNote (){
        Toast.makeText(getActivity(), "Заполните поле \"Содержимое\" ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    // Размещение изображения в объект ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //  Error occurred while creating the File
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(), "xyz.devdiscovery.odoocourier.FileProvider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // Remount read XML file.
    private class DownloadPageTask extends
            AsyncTask<String, Void, List<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            try {
             //   return downloadOneUrl(urls[0]);
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
/*
    private ArrayList<String> downloadOneUrl(String myurl) throws
            Exception {
        InputStream inputStream = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();

                InputSource inputSource = new InputSource(inputStream);
                // Создаем экземпляр XPath
                XPath xpath = XPathFactory.newInstance().newXPath();
                // задаем выражение для разбора
                String expression = "//title";
                // список полученных узлов
                NodeList nodes = (NodeList) xpath.evaluate(expression,
                        inputSource, XPathConstants.NODESET);


                // если узел найден
                if (nodes != null && nodes.getLength() > 0) {
                    categoryList.clear();
                    int nodesLength = nodes.getLength();
                    for (int i = 0; i < nodesLength; ++i) {
                        // формируем списочный массив
                        Node node = nodes.item(i);
                        categoryList.add(node.getTextContent());
                    }
                }
            } else {
                String data = connection.getResponseMessage()
                        + " . Error Code : " + responseCode;
            }
            connection.disconnect();
            // return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return categoryList;
    }
*/
}