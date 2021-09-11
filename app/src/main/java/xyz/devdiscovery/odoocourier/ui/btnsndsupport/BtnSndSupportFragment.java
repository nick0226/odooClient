package xyz.devdiscovery.odoocourier.ui.btnsndsupport;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xyz.devdiscovery.odoocourier.R;
import xyz.devdiscovery.odoocourier.ui.home.HomeFragment;
import xyz.devdiscovery.odoocourier.ui.slideshow.SlideshowFragment;

import static android.app.Activity.RESULT_OK;


public class BtnSndSupportFragment extends Fragment {
    private final int RQS_LOAD_IMAGE = 0;
    private final int RQS_SEND_EMAIL = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 3;
    private  static final int REAUEST_CAMERA = 1;




    private ArrayList<Uri> mUriList = new ArrayList<>();
    private ArrayAdapter<Uri> mFileListAdapter;

    private ArrayList<Uri> mPhotoUriList = new ArrayList<>();
    private ArrayAdapter<Uri> mPhotoListAdapter;

    private Button buttonSend;
    private EditText textSubject;
    private EditText textMessage;
    private String odooAdress = "messages@pro100systems.com.ua";
    private Button TakePicture;
    static final int REQUEST_TAKE_PHOTO = 2;
    //static final int REQUEST_TAKE_PHOTO_2 = 2;
    private Uri photoURI;
    //private Uri photoURI_img2;
    private String mCurrentPhotoPath;
    private ImageView imageView;
    boolean cl = false;


    View.OnClickListener addFileOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RQS_LOAD_IMAGE);
        }
    };

    private BtnSndSupportViewModel btnSndSupportViewModel;
    private Object SlideshowFragment;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        btnSndSupportViewModel =
                ViewModelProviders.of(this).get(BtnSndSupportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_btnsndsupport, container, false);


        buttonSend = (Button) root.findViewById(R.id.send_mail);
        textSubject = (EditText) root.findViewById(R.id.edit_note);
        textMessage = (EditText) root.findViewById(R.id.edit_content);
        TakePicture = (Button) root.findViewById(R.id.take_a_photo);
        Button addFileButton = (Button) root.findViewById(R.id.openGallary);
        addFileButton.setOnClickListener(addFileOnClickListener);


        mFileListAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mUriList);

        mPhotoListAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mPhotoUriList);


        /* Метод для возврата в предыдущюю Activity "SlideshowFragmen" */
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.beginTransaction()
                    //        .replace(R.id.nav_host_fragment, new SlideshowFragment()).commit();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new SlideshowFragment()).addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        } );
        /* Метод для возврата в предыдущюю Activity "SlideshowFragmen"  END  */


        // Запуск камеры для снимка, получаем запрос на резрешение камеры и записи от пользователя //
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


                        //dispatchTakePictureIntent();
                        //Toast toast = Toast.makeText(getActivity(), "Запуск камеры для снимка", Toast.LENGTH_LONG);
                        //toast.show();

                   // if (cl = true){
                  //  dispatchTakePictureIntent();
                   // }


                }

            }
        });

        // Запуск камеры для снимка, получаем запрос на резрешение камеры и записи от пользователя. END //



        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String emailSubject = textSubject.getText().toString();
                String emailText = textMessage.getText().toString();

                Intent intent = new Intent();

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{odooAdress});
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                intent.putExtra(Intent.EXTRA_STREAM, photoURI);

                if (mPhotoUriList.isEmpty()) {
                    // посылаем письмо без прикрепленной картинки
                    intent.setAction(Intent.ACTION_SEND);
                    //intent.setType("plain/text");
                } else if (mPhotoUriList.size() == 1) {
                    // посылаем письмо с одной картинкой
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, mPhotoUriList.get(0));
                    //intent.setType("image/*");
                } else {
                    // посылаем письмо с несколькими картинками
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mPhotoUriList);
                    //intent.setType("image/*");
                }


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

                intent.setType("message/rfc822");

                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmailExternal");
                Toast toast = Toast.makeText(getActivity(), "Запускаю клиент Gmail", Toast.LENGTH_LONG);
                toast.show();
                try {
                    startActivity(intent);

                } catch (ActivityNotFoundException ex) {
                    // GMail not found
                }


                //mEmailTextEditText.setText("");
                mUriList.clear();

            }
        });



        return root;
    }


    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(getActivity(), permissions,123);
        }
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_LOAD_IMAGE:
                    Uri imageUri = data.getData();
                    mPhotoUriList.add(imageUri);
                    mPhotoListAdapter.notifyDataSetChanged();
                    break;
                case RQS_SEND_EMAIL:
                    break;
            }
        }

    }

  // Размещение изображения в объект ImageView
  /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
        }
    }
  */

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
                //photoURI_img2 = FileProvider.getUriForFile(getActivity(), "xyz.devdiscovery.odoocourier.FileProvider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI_img2);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_2);
            }



        }
    }

}
