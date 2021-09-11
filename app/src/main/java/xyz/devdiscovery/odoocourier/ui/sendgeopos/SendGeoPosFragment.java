package xyz.devdiscovery.odoocourier.ui.sendgeopos;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.devdiscovery.odoocourier.BuildConfig;
import xyz.devdiscovery.odoocourier.JavaMailAPI;

import xyz.devdiscovery.odoocourier.R;
import xyz.devdiscovery.odoocourier.ui.settings.MySettingsFragment;

public class SendGeoPosFragment extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "";
    private SendGeoPosViewModel sendGeoPosViewModel;


    private static final double TARGET_LATITUDE = 17.893366;
    private static final double TARGET_LONGITUDE = 19.511868;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private TextView mLatitudeTextView, mLongitudeTextView;

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 1;      // в метрах
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;   // в мс
    final private String channelSneMassage = "tabel@pro100systems.com.ua";
    final private String test_email = "kit1datasvit@gmail.com";
    private Button buttonSendGeo;
    final private String myLocation = "Моё местоположение: ";
    private String position;
    private String UIShowPoint;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_TOOL = "Tool";
    SharedPreferences mSettings;
    TextView MyEmailView;
    TextView showMyLocation;
    public static String MY_EMAIL = "";

    public String dateText = "";
    public String timeText = "";


    // Геоданные для предприятий BUFET
    final private double latitude_office = 49.948141457227635;
    final private double longtitude_office = 36.291142163863704;

    final private double latitude_xtz = 49.94319274047868;
    final private double longtitude_xtz = 36.37026540250058;

    final private double latitude_shop = 50.00889269590871;
    final private double longtitude_shop = 36.22503070493355;

    final private double latitude_mia = 49.98173570618966;
    final private double longtitude_mia = 36.17792868781164;

    final private double latitude_smz = 49.98560587718573;
    final private double longtitude_smz = 36.18960781053583;

    final private double longtitude_fkh = 36.18960781053583;
    final private double latitude_fkh = 49.99583639795834;

    final private double longtitude_ven = 49.94788398969567;
    final private double latitude_ven = 36.26103464613018;

    final private double longtitude_haw = 50.03842911560552;
    final private double latitude_haw = 36.34831586151282;

    final private double longtitude_ibz = 50.01200353922473;
    final private double latitude_ibz = 36.2258854506041;

    final private double longtitude_ist = 50.023220619616374;
    final private double latitude_ist = 36.22465866592326;

    final private double longtitude_LGR = 50.00451362191355;
    final private double latitude_LGR = 36.18513699849836;

    final private double longtitude_LSV = 49.988362687222995;
    final private double latitude_LSV = 36.35902211715251;

    final private double latitude_MEL = 36.23398765301398;
    final private double longtitude_MEL = 49.98791333583366;

    final private double latitude_NIC = 49.966780151052724;
    final private double longtitude_NIC = 36.182937249483714;

    final private double latitude_PEK = 49.99413884661554;
    final private double longtitude_PEK = 36.20881953860199;

    final private double latitude_PRA = 50.0070538498254;
    final private double longtitude_PRA = 36.22577780473471;

    final private double latitude_RND = 50.00634109733516;
    final private double longtitude_RND = 36.250653325791504;

    final private double latitude_STK = 50.006116700403794;
    final private double longtitude_STK =  36.23585912474343;

    final private double latitude_SLD = 50.034660444413994;
    final private double longtitude_SLD =  36.22296641318561;

    final private double latitude_STD = 50.01754816605718;
    final private double longtitude_STD =  36.330478903674795;

    final private double latitude_TRG = 50.00929554044614;
    final private double longtitude_TRG =  36.32014139891309;

    final private double latitude_FIO = 49.99753479043383;
    final private double longtitude_FIO =  36.23970647508896;

    final private double latitude_JAM = 49.956868358870054;
    final private double longtitude_JAM =  36.361103255026244;

    final private double latitude_XAI = 50.04168097043926;
    final private double longtitude_XAI =  36.28241300571709;

    // Геоданные для предприятий GoodTime
    final private double latitude_ALK = 50.059582398766054;
    final private double longtitude_ALK =  36.2033303564255;

    final private double latitude_SSH = 49.99031297243401;
    final private double longtitude_SSH =  36.34958689795043;

    final private double latitude_GNM = 50.00557010398967;
    final private double longtitude_GNM =  36.244044656382215;

    final private double latitude_BUG = 50.046814339194114;
    final private double longtitude_BUG =  36.2900946196658;

    final private double latitude_LSG = 50.05501519564426;
    final private double longtitude_LSG =  36.204807103366186;

    final private double latitude_MOL = 50.05400882484593;
    final private double longtitude_MOL =  36.1966370437253;

    final private double latitude_OLM = 49.95864320753943;
    final private double longtitude_OLM =  36.3112089339153;

    final private double latitude_PUS = 49.99958744548937;
    final private double longtitude_PUS =  36.24366554683397;

    final private double latitude_SAL = 49.94434470963358;
    final private double longtitude_SAL =  36.2747067509095;

    final private double latitude_TAN = 49.95579974921055;
    final private double longtitude_TAN =  36.31673251694075;

    // Геоданные для предприятий Рестораны
    final private double latitude_TRA1 = 50.01325458129637;
    final private double longtitude_TRA1 =  36.231974711975965;

    final private double latitude_TRA2 = 50.01862710894986;
    final private double longtitude_TRA2 =  36.224078983010905;

    final private double latitude_TRA3 = 50.05026217438086;
    final private double longtitude_TRA3 =  36.19637176445008;

    final private double latitude_TRA5 = 50.060344951269784;
    final private double longtitude_TRA5 =  36.20334501069008;

    final private double latitude_FRZ = 49.96965666777154;
    final private double longtitude_FRZ =  36.27467816538718;

    final private double radius = 3;

    // Геоданные для предприятий Семко
    final private double latitude_GAR = 50.00764210811808;
    final private double longtitude_GAR =  36.35830023376418;

    final private double latitude_DEP = 50.0304710617081;
    final private double longtitude_DEP =  36.3647464810746;

    final private double latitude_COM = 49.94414435655272;
    final private double longtitude_COM =  36.2999646442375;

    final private double latitude_OKN = 50.027822648017846;
    final private double longtitude_OKN =  36.35346875678545;

    final private double latitude_X38 = 49.99778319865842;
    final private double longtitude_X38 =  36.32977144043237;

    public SendGeoPosFragment() {


    }



    public static boolean checkForAllowMockLocationsApps(Context context) {

        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Got exception " + e.getMessage());
            }
        }

        if (count > 6) {
            return true;
        } else {
            return false;
        }
    }



    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sendGeoPosViewModel = ViewModelProviders.of(this).get(SendGeoPosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sendgeopos, container, false);

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mLatitudeTextView = (TextView) root.findViewById(R.id.textViewLatitude);
        mLongitudeTextView = (TextView) root.findViewById(R.id.textViewLongitude);
        showMyLocation = (TextView) root.findViewById(R.id.showMyLocation);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        buttonSendGeo = (Button) root.findViewById(R.id.sendGeoDate);
        MyEmailView = root.findViewById(R.id.my_mail);



        // if (checkForAllowMockLocationsApps(getActivity()) == true){
        //      Toast toast = Toast.makeText(getActivity(), "ON", Toast.LENGTH_LONG);
        //      toast.show();
        // } if (checkForAllowMockLocationsApps(getActivity()) == false) {
        //    Toast.makeText(getActivity(), "OFF", Toast.LENGTH_LONG).show();
        // }


        // SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        // mapFragment.getMapAsync(this);


        // Показать точку на GoogleMaps OnCreate
        // SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        // FragmentTransaction fragmentTransaction =
        //        getChildFragmentManager().beginTransaction();
        // fragmentTransaction.add(R.id.mapView, mMapFragment);
        // fragmentTransaction.commit();
        // mMapFragment.getMapAsync(this);


        if (mSettings.contains(APP_PREFERENCES_SETTINGS)) {
            // выводим данные в TextView
            MyEmailView.setText(mSettings.getString(APP_PREFERENCES_SETTINGS,
                    ""));
        }
        if (MyEmailView!=null){
            MY_EMAIL = MyEmailView.getText().toString();
        }



        // Отправка сообщения через JavaMailAPI
        buttonSendGeo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {



                try {

                    if (checkForAllowMockLocationsApps(getActivity()) == true){
                        AlertDialog ad = new AlertDialog.Builder(getActivity())
                                .create();
                        ad.setCancelable(false);
                        ad.setTitle("Ошибка");
                        ad.setMessage("Не могу отправить сообщение с координатами, на устройстве установлены приложения которые могут быть использованы для передачи фиктивных GPS-координат");
                        ad.setButton("ОК", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ad.show();
                    } else
                        sendMail();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
        // --------------------------------------------

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeText = timeFormat.format(currentDate);

            return root;
    }


    // Метод для работы отправки сообщений SMTP
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMail() throws MalformedURLException {

        String latitudeGeo = mLatitudeTextView.getText().toString();
        String longTitudeGeo = mLongitudeTextView.getText().toString();

        String result_No_Point = "Без геопривязки объекта";
        // Константы Адресов предприятий.
        // Буфеты
         String result_office = "Офис, улица Киргизкая, 19";
        String result_xtz = "Буфет \"ХТЗ\",проспект Архитектора Альошина, 19";
        String result_shop = "Магазин \"Ельмир\", проспект Независимости, 5";
        String result_mia = "Буфет \"Майами\", улица Холодногорская, 2";
        String result_smz = "Буфет \"Симеиз\", улица Полтавский Шлях, 126";
        String result_fkh = "Производственная база \"Бали\", проспект Юбилейный, 50А";
        String result_ven = "Буфет \"Венеция\", проспект Гагарина, 177Б";
        String result_haw = "Буфет \"Гавайи\", улица Леся Сердюка, 44";
        String result_ibz = "Буфет \"Ибица\", улица Культуры, 23";
        String result_ist = "Буфет \"Источник\", проспект Науки, 38";
        String result_LGR = "Производственная база \"Куба\", улица Кривоконевская, 15";
        String result_LSV = "Буфет \"Лас-Вегас\", Салтовское Шоссе 262 Б";
        String result_MEL = "Буфет \"Мелодия\", переулок Костюринский, 3";
        String result_NIC = "Буфет \"Ницца\", проспект Любви Малой, 24";
        String result_PEK = "Производственная база \"Пекарня\", улица Мало-Панасовская, 2";
        String result_PRA = "Буфет \"Правда\", проспект Независимости, 5а";
        String result_RND = "Буфет \"Рандеву\", улица Пушкинская, 92/1";
        String result_STK = "Буфет \"Сан-Тропе\", улица Сумская, 41";
        String result_SLD = "Буфет \"Солдат\", улица 23-е Августа, 30";
        String result_STD = "Буфет \"Студенческая\", улица Валентиновская, 21";
        String result_TRG = "Буфет \"Таргет\", улица Академика Павлова, 120";
        String result_FIO = "Буфет \"Фиоль\", улица Пушкинская, 40";
        String result_JAM = "Буфет \"Ямайка\", Московский проспект, 256Б";
        String result_XAI = "Буфет \"ХАИ\", улица Чкалова, 2Д";

        // GoodTime
        String result_ALK = "GoodTime \"Алексеевка\", проспект Победы, 62";
        String result_SSH = "GoodTime \"Бутербродная\", Салтовское шоссе, 143";
        String result_GNM = "GoodTime \"Гном\", Алчевских, 40";
        String result_BUG = "GoodTime \"Жуковский\", ул. Академика Проскуры, 5";
        String result_LSG = "GoodTime \"Людвига Свободы\", проспект Людвига Свободы, 42";
        String result_MOL = "GoodTime \"Молодежная\", проспект Победы, 50";
        String result_OLM = "GoodTime \"Олимпийская\", ул. Олимпийская, 15а";
        String result_PUS = "GoodTime \"Пушкинская\", ул. Манизера, 1";
        String result_SAL = "GoodTime \"Салют\", проспект Героев Сталинграда, 12";
        String result_TAN = "GoodTime \"Танкопия\", улица Танкопия, 9/23";

        // Рестораны
        String result_TRA1 = "Ресторан \"Траттория Ломбардия (Траттория 1)\", улица Культуры, 22Б";
        String result_TRA2 = "Ресторан \"Траттория Сицилия (Траттория 2)\", проспект Науки, 19А";
        String result_TRA3 = "Ресторан \"Траттория Сардиния (Траттория 3)\", улица Ахсарова, 4/6A";
        String result_TRA5 = "Ресторан \"Траттория Калабрия (Траттория 5)\"";
        String result_FRZ = "Столовая \"Фрунзе\", улица Плехановская, 126а";

        // Предприятия Семко
        String result_GAR = "Пиццерия \"Гарибальди\", остановка ул. Гарибальди, напротив улица Валентиновская, 44";
        String result_DEP = "Производственная база \"Депо\", расположение на терретории Салтовского трамвайного депо";
        String result_COM = "Пиццерия \"Пицца.com\", проспект Героев Сталинграда, 155";
        String result_OKN = "Пиццерия \"Океан\", улица Бучмы";
        String result_X38 = "Пиццерия \"Пицца 38\", проспект Юбилейный";

        // Переменные, для вычисления попадания предприятия в радиусе координат gps
        // Буфеты
        double check_point_office;
        double check_point_xtz;
        double check_point_shop;
        double check_point_mia;
        double check_point_smz;
        double check_point_fkh;
        double check_point_ven;
        double check_point_haw;
        double check_point_ibz;
        double check_point_ist;
        double check_point_LGR;
        double check_point_LSV;
        double check_point_MEL;
        double check_point_NIC;
        double check_point_PEK;
        double check_point_PRA;
        double check_point_RND;
        double check_point_STK;
        double check_point_SLD;
        double check_point_STD;
        double check_point_TRG;
        double check_point_FIO;
        double check_point_JAM;
        double check_point_XAI;

        // GoodTime
        double check_point_ALK;
        double check_point_SSH;
        double check_point_GNM;
        double check_point_BUG;
        double check_point_LSG;
        double check_point_MOL;
        double check_point_OLM;
        double check_point_PUS;
        double check_point_SAL;
        double check_point_TAN;

        // Рестораны
        double check_point_TRA1;
        double check_point_TRA2;
        double check_point_TRA3;
        double check_point_TRA5;
        double check_point_FRZ;

        // Семко
        double check_point_GAR;
        double check_point_DEP;
        double check_point_COM;
        double check_point_OKN;
        double check_point_X38;



        // Вычисление
        check_point_office = Math.sqrt(latitude_office*latitude_office + longtitude_office*longtitude_office);
        check_point_xtz = Math.sqrt(latitude_xtz*latitude_xtz + longtitude_xtz*longtitude_xtz);
        check_point_shop = Math.sqrt(latitude_shop*latitude_shop + longtitude_shop*longtitude_shop);
        check_point_mia = Math.sqrt(latitude_mia*latitude_mia + longtitude_mia*longtitude_mia);
        check_point_smz = Math.sqrt(latitude_smz*latitude_smz + longtitude_smz*longtitude_smz);
        check_point_fkh = Math.sqrt(latitude_fkh*latitude_fkh + longtitude_fkh*longtitude_fkh);
        check_point_ven = Math.sqrt(latitude_ven*latitude_ven + longtitude_ven*longtitude_ven);
        check_point_haw = Math.sqrt(latitude_haw*latitude_haw + longtitude_haw*longtitude_haw);
        check_point_ibz = Math.sqrt(latitude_ibz*latitude_ibz + longtitude_ibz*longtitude_ibz);
        check_point_ist = Math.sqrt(latitude_ist*latitude_ist + longtitude_ist*longtitude_ist);
        check_point_LGR = Math.sqrt(latitude_LGR*latitude_LGR + longtitude_LGR*longtitude_LGR);
        check_point_LSV = Math.sqrt(latitude_LSV*latitude_LSV + longtitude_LSV*longtitude_LSV);
        check_point_MEL = Math.sqrt(latitude_MEL*latitude_MEL + longtitude_MEL*longtitude_MEL);
        check_point_NIC = Math.sqrt(latitude_NIC*latitude_NIC + longtitude_NIC*longtitude_NIC);
        check_point_PEK = Math.sqrt(latitude_PEK*latitude_PEK + longtitude_PEK*longtitude_PEK);
        check_point_PRA = Math.sqrt(latitude_PRA*latitude_PRA + longtitude_PRA*longtitude_PRA);
        check_point_RND = Math.sqrt(latitude_RND*latitude_RND + longtitude_RND*longtitude_RND);
        check_point_STK = Math.sqrt(latitude_STK*latitude_STK + longtitude_STK*longtitude_STK);
        check_point_SLD = Math.sqrt(latitude_SLD*latitude_SLD + longtitude_SLD*longtitude_SLD);
        check_point_STD = Math.sqrt(latitude_STD*latitude_STD + longtitude_STD*longtitude_STD);
        check_point_TRG = Math.sqrt(latitude_TRG*latitude_TRG + longtitude_TRG*longtitude_TRG);
        check_point_FIO = Math.sqrt(latitude_FIO*latitude_FIO + longtitude_FIO*longtitude_FIO);
        check_point_JAM = Math.sqrt(latitude_JAM*latitude_JAM + longtitude_JAM*longtitude_JAM);
        check_point_XAI = Math.sqrt(latitude_XAI*latitude_XAI + longtitude_XAI*longtitude_XAI);

        check_point_ALK = Math.sqrt(latitude_ALK*latitude_ALK + longtitude_ALK*longtitude_ALK);
        check_point_SSH = Math.sqrt(latitude_SSH*latitude_SSH + longtitude_SSH*longtitude_SSH);
        check_point_GNM = Math.sqrt(latitude_GNM*latitude_GNM + longtitude_GNM*longtitude_GNM);
        check_point_BUG = Math.sqrt(latitude_BUG*latitude_BUG + longtitude_BUG*longtitude_BUG);
        check_point_LSG = Math.sqrt(latitude_LSG*latitude_LSG + longtitude_LSG*longtitude_LSG);
        check_point_MOL = Math.sqrt(latitude_MOL*latitude_MOL + longtitude_MOL*longtitude_MOL);
        check_point_OLM = Math.sqrt(latitude_OLM*latitude_OLM + longtitude_OLM*longtitude_OLM);
        check_point_PUS = Math.sqrt(latitude_PUS*latitude_PUS + longtitude_PUS*longtitude_PUS);
        check_point_SAL = Math.sqrt(latitude_SAL*latitude_SAL + longtitude_SAL*longtitude_SAL);
        check_point_TAN = Math.sqrt(latitude_TAN*latitude_TAN + longtitude_TAN*longtitude_TAN);

        check_point_TRA1 = Math.sqrt(latitude_TRA1*latitude_TRA1 + longtitude_TRA1*longtitude_TRA1);
        check_point_TRA2 = Math.sqrt(latitude_TRA2*latitude_TRA2 + longtitude_TRA2*longtitude_TRA2);
        check_point_TRA3 = Math.sqrt(latitude_TRA3*latitude_TRA3 + longtitude_TRA3*longtitude_TRA3);
        check_point_TRA5 = Math.sqrt(latitude_TRA5*latitude_TRA5 + longtitude_TRA5*longtitude_TRA5);
        check_point_FRZ = Math.sqrt(latitude_FRZ*latitude_FRZ + longtitude_FRZ*longtitude_FRZ);

        check_point_GAR = Math.sqrt(latitude_GAR*latitude_GAR + longtitude_GAR*longtitude_GAR);
        check_point_DEP = Math.sqrt(latitude_DEP*latitude_DEP + longtitude_DEP*longtitude_DEP);
        check_point_COM = Math.sqrt(latitude_COM*latitude_COM + longtitude_COM*longtitude_COM);
        check_point_OKN = Math.sqrt(latitude_OKN*latitude_OKN + longtitude_OKN*longtitude_OKN);
        check_point_X38 = Math.sqrt(latitude_X38*latitude_X38 + longtitude_X38*longtitude_X38);

        // Офис
        boolean checked_OFFICE_lat = latitudeGeo.startsWith("49.947") || latitudeGeo.startsWith("49.948");
        boolean checked_OFFICE_lon = longTitudeGeo.startsWith("36.290") || longTitudeGeo.startsWith("36.291") || longTitudeGeo.startsWith("36.292");
        // ХТЗ
        boolean checked_XTZ_lat = latitudeGeo.startsWith("49.943") || latitudeGeo.startsWith("49.942");
        boolean checked_XTZ_lon = longTitudeGeo.startsWith("36.370") || longTitudeGeo.startsWith("36.369");
        // Ельмир
        boolean checked_SHOP_lat = latitudeGeo.startsWith("50.008");
        boolean checked_SHOP_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.225");
        // Майами
        boolean checked_MIA_lat = latitudeGeo.startsWith("49.981");
        boolean checked_MIA_lon = longTitudeGeo.startsWith("36.175") || longTitudeGeo.startsWith("36.176")|| longTitudeGeo.startsWith("36.177");
        // Симеиз
        boolean checked_SMZ_lat = latitudeGeo.startsWith("49.985");
        boolean checked_SMZ_lon = longTitudeGeo.startsWith("36.189") || longTitudeGeo.startsWith("36.190");
        // Бали
        boolean checked_FKH_lat = latitudeGeo.startsWith("49.995");
        boolean checked_FKH_lon = longTitudeGeo.startsWith("36.333");
        // Венеция
        boolean checked_VEN_lat = latitudeGeo.startsWith("49.947") || latitudeGeo.startsWith("49.948");
        boolean checked_VEN_lon = longTitudeGeo.startsWith("36.261");
        // Гавайи
        boolean checked_HAW_lat = latitudeGeo.startsWith("50.038");
        boolean checked_HAW_lon = longTitudeGeo.startsWith("36.347") || longTitudeGeo.startsWith("36.348");
        // Ибица
        boolean checked_IBZ_lat = latitudeGeo.startsWith("50.011");
        boolean checked_IBZ_lon = longTitudeGeo.startsWith("36.225") || longTitudeGeo.startsWith("36.226");
        // Источник
        boolean checked_IST_lat = latitudeGeo.startsWith("50.022") || latitudeGeo.startsWith("50.023");
        boolean checked_IST_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.225");
        // Куба
        boolean checked_LGR_lat = latitudeGeo.startsWith("50.004");
        boolean checked_LGR_lon = longTitudeGeo.startsWith("36.184") || longTitudeGeo.startsWith("36.185");
        // Лас-Вегас
        boolean checked_LSV_lat = latitudeGeo.startsWith("49.987") || latitudeGeo.startsWith("49.988");
        boolean checked_LSV_lon = longTitudeGeo.startsWith("36.358") || longTitudeGeo.startsWith("36.359");
        // Мелодия
        boolean checked_MEL_lat = latitudeGeo.startsWith("49.987") || latitudeGeo.startsWith("49.988");
        boolean checked_MEL_lon = longTitudeGeo.startsWith("36.233") || longTitudeGeo.startsWith("36.234");
        // Ницца
        boolean checked_NIC_lat = latitudeGeo.startsWith("49.966") || latitudeGeo.startsWith("49.967");
        boolean checked_NIC_lon = longTitudeGeo.startsWith("36.182") || longTitudeGeo.startsWith("36.183");
        // Пекарня
        boolean checked_PEK_lat = latitudeGeo.startsWith("49.994");
        boolean checked_PEK_lon = longTitudeGeo.startsWith("36.208");
        // Правда
        boolean checked_PRA_lat = latitudeGeo.startsWith("50.006") || latitudeGeo.startsWith("50.007");
        boolean checked_PRA_lon = longTitudeGeo.startsWith("36.225");
        // Рандеву
        boolean checked_RND_lat = latitudeGeo.startsWith("50.006");
        boolean checked_RND_lon = longTitudeGeo.startsWith("36.250") || longTitudeGeo.startsWith("36.251");
        // Сан-Тропе
        boolean checked_STK_lat = latitudeGeo.startsWith("50.005") || latitudeGeo.startsWith("50.006");
        boolean checked_STK_lon = longTitudeGeo.startsWith("36.235") || longTitudeGeo.startsWith("36.236");
        // Солдат
        boolean checked_SLD_lat = latitudeGeo.startsWith("50.034");
        boolean checked_SLD_lon = longTitudeGeo.startsWith("36.222") || longTitudeGeo.startsWith("36.223");
        // Студенческая
        boolean checked_STD_lat = latitudeGeo.startsWith("50.017") || latitudeGeo.startsWith("50.018");
        boolean checked_STD_lon = longTitudeGeo.startsWith("36.329") || longTitudeGeo.startsWith("36.330") || longTitudeGeo.startsWith("36.331");
        // Таргет
        boolean checked_TRG_lat = latitudeGeo.startsWith("50.008") || latitudeGeo.startsWith("50.009");
        boolean checked_TRG_lon = longTitudeGeo.startsWith("36.319") || longTitudeGeo.startsWith("36.320");
        // Фиоль
        boolean checked_FIO_lat = latitudeGeo.startsWith("49.997");
        boolean checked_FIO_lon = longTitudeGeo.startsWith("36.239") || longTitudeGeo.startsWith("36.240");
        // Ямайка
        boolean checked_JAM_lat = latitudeGeo.startsWith("49.956") || latitudeGeo.startsWith("49.957");
        boolean checked_JAM_lon = longTitudeGeo.startsWith("36.360") || longTitudeGeo.startsWith("36.361");
        // ХАИ
        boolean checked_XAI_lat = latitudeGeo.startsWith("50.041");
        boolean checked_XAI_lon = longTitudeGeo.startsWith("36.281") || longTitudeGeo.startsWith("36.282") || longTitudeGeo.startsWith("36.283");


        // Алексеевка
        boolean checked_ALK_lat = latitudeGeo.startsWith("50.059");
        boolean checked_ALK_lon = longTitudeGeo.startsWith("36.203");
        // Бутербродная
        boolean checked_SSH_lat = latitudeGeo.startsWith("49.989") || latitudeGeo.startsWith("49.990");
        boolean checked_SSH_lon = longTitudeGeo.startsWith("36.349");
        // Гном
        boolean checked_GNM_lat = latitudeGeo.startsWith("50.005");
        boolean checked_GNM_lon = longTitudeGeo.startsWith("36.243") || longTitudeGeo.startsWith("36.244");
        // Жуковский
        boolean checked_BUG_lat = latitudeGeo.startsWith("50.046") || latitudeGeo.startsWith("50.047");
        boolean checked_BUG_lon = longTitudeGeo.startsWith("36.289") || longTitudeGeo.startsWith("36.290");
        // Людвига Свободы
        boolean checked_LSG_lat = latitudeGeo.startsWith("50.055");
        boolean checked_LSG_lon = longTitudeGeo.startsWith("36.204");
        // Молодежная
        boolean checked_MOL_lat = latitudeGeo.startsWith("50.054") || latitudeGeo.startsWith("50.053");
        boolean checked_MOL_lon = longTitudeGeo.startsWith("36.196");
        // Олимпийская
        boolean checked_OLM_lat = latitudeGeo.startsWith("49.958");
        boolean checked_OLM_lon = longTitudeGeo.startsWith("36.311") || longTitudeGeo.startsWith("36.310");
        // Пушкинская
        boolean checked_PUS_lat = latitudeGeo.startsWith("49.999");
        boolean checked_PUS_lon = longTitudeGeo.startsWith("36.243") || longTitudeGeo.startsWith("36.242");
        // Салют
        boolean checked_SAL_lat = latitudeGeo.startsWith("49.944") || latitudeGeo.startsWith("49.943");
        boolean checked_SAL_lon = longTitudeGeo.startsWith("36.274");
        // Танкопия
        boolean checked_TAN_lat = latitudeGeo.startsWith("49.955");
        boolean checked_TAN_lon = longTitudeGeo.startsWith("36.316") || longTitudeGeo.startsWith("36.317");


        // Траттория 1
        boolean checked_TRA1_lat = latitudeGeo.startsWith("50.013") || latitudeGeo.startsWith("50.012");
        boolean checked_TRA1_lon = longTitudeGeo.startsWith("36.230") || longTitudeGeo.startsWith("36.231") || longTitudeGeo.startsWith("36.232");
        // Траттория 2
        boolean checked_TRA2_lat = latitudeGeo.startsWith("50.018");
        boolean checked_TRA2_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.223");
        // Траттория 3
        boolean checked_TRA3_lat = latitudeGeo.startsWith("50.050") || latitudeGeo.startsWith("50.049");
        boolean checked_TRA3_lon = longTitudeGeo.startsWith("36.196");
        // Траттория 5
        boolean checked_TRA5_lat = latitudeGeo.startsWith("50.060") || latitudeGeo.startsWith("50.059");
        boolean checked_TRA5_lon = longTitudeGeo.startsWith("36.203") || longTitudeGeo.startsWith("36.202");
        // Фрунзе
        boolean checked_FRZ_lat = latitudeGeo.startsWith("49.969") || latitudeGeo.startsWith("49.968") || latitudeGeo.startsWith("49.967") || latitudeGeo.startsWith("49.966");
        boolean checked_FRZ_lon = longTitudeGeo.startsWith("36.274") || longTitudeGeo.startsWith("36.275") || longTitudeGeo.startsWith("36.273") || longTitudeGeo.startsWith("36.272");


        // Гарибальди
        boolean checked_GAR_lat = latitudeGeo.startsWith("50.007");
        boolean checked_GAR_lon = longTitudeGeo.startsWith("36.357") || longTitudeGeo.startsWith("36.358");
        // Депо
        boolean checked_DEP_lat = latitudeGeo.startsWith("50.030") || latitudeGeo.startsWith("50.055");
        boolean checked_DEP_lon = longTitudeGeo.startsWith("36.36") || longTitudeGeo.startsWith("36.204");
        // Пицца.com
        boolean checked_COM_lat = latitudeGeo.startsWith("49.943") || latitudeGeo.startsWith("49.944");
        boolean checked_COM_lon = longTitudeGeo.startsWith("36.299") || longTitudeGeo.startsWith("36.300");
        // Океан
        boolean checked_OKN_lat = latitudeGeo.startsWith("50.027") || latitudeGeo.startsWith("50.028");
        boolean checked_OKN_lon = longTitudeGeo.startsWith("36.353");
        // Пицца 38
        boolean checked_X38_lat = latitudeGeo.startsWith("49.997") || latitudeGeo.startsWith("49.998");
        boolean checked_X38_lon = longTitudeGeo.startsWith("36.329");

        if (checked_OFFICE_lat && checked_OFFICE_lon && check_point_office > radius) {
            position = result_office;
        }else if (checked_XTZ_lat && checked_XTZ_lon && check_point_xtz > radius) {
            position = result_xtz;
        }else if (checked_SHOP_lat && checked_SHOP_lon && check_point_shop > radius) {
            position = result_shop;
        }else if (checked_MIA_lat && checked_MIA_lon && check_point_mia > radius) {
            position = result_mia;
        }else if (checked_SMZ_lat && checked_SMZ_lon && check_point_smz > radius) {
            position = result_smz;
        }else if (checked_FKH_lat && checked_FKH_lon && check_point_fkh > radius) {
            position = result_fkh;
        }else if (checked_VEN_lat && checked_VEN_lon && check_point_ven > radius) {
            position = result_ven;
        }else if (checked_HAW_lat && checked_HAW_lon && check_point_haw > radius) {
            position = result_haw;
        }else if (checked_IBZ_lat && checked_IBZ_lon && check_point_ibz > radius) {
            position = result_ibz;
        }else if (checked_IST_lat && checked_IST_lon && check_point_ist > radius) {
            position = result_No_Point;
        }else if (checked_LGR_lat && checked_LGR_lon && check_point_LGR > radius) {
            position = result_LGR;
        }else if (checked_LSV_lat && checked_LSV_lon && check_point_LSV > radius) {
            position = result_LSV;
        }else if (checked_MEL_lat && checked_MEL_lon && check_point_MEL > radius) {
            position = result_MEL;
        }else if (checked_NIC_lat && checked_NIC_lon && check_point_NIC > radius) {
            position = result_NIC;
        }else if (checked_PEK_lat && checked_PEK_lon && check_point_PEK > radius) {
            position = result_PEK;
        }else if (checked_PRA_lat && checked_PRA_lon && check_point_PRA > radius) {
            position = result_PRA;
        }else if (checked_RND_lat && checked_RND_lon && check_point_RND > radius) {
            position = result_RND;
        }else if (checked_STK_lat && checked_STK_lon && check_point_STK > radius) {
            position = result_STK;
        }else if (checked_SLD_lat && checked_SLD_lon && check_point_SLD > radius) {
            position = result_SLD;
        }else if (checked_STD_lat && checked_STD_lon && check_point_STD > radius) {
            position = result_STD;
        }else if (checked_TRG_lat && checked_TRG_lon && check_point_TRG > radius) {
            position = result_TRG;
        }else if (checked_FIO_lat && checked_FIO_lon && check_point_FIO > radius) {
            position = result_FIO;
        }else if (checked_JAM_lat && checked_JAM_lon && check_point_JAM > radius) {
            position = result_JAM;
        }else if (checked_XAI_lat && checked_XAI_lon && check_point_XAI > radius) {
            position = result_XAI;
        }else if (checked_ALK_lat && checked_ALK_lon && check_point_ALK > radius) {
            position = result_ALK;
        }else if (checked_SSH_lat && checked_SSH_lon && check_point_SSH > radius) {
            position = result_SSH;
        }else if (checked_GNM_lat && checked_GNM_lon && check_point_GNM > radius) {
            position = result_GNM;
        }else if (checked_BUG_lat && checked_BUG_lon && check_point_BUG > radius) {
            position = result_BUG;
        }else if (checked_LSG_lat && checked_LSG_lon && check_point_LSG > radius) {
            position = result_LSG;
        }else if (checked_MOL_lat && checked_MOL_lon && check_point_MOL > radius) {
            position = result_MOL;
        }else if (checked_OLM_lat && checked_OLM_lon && check_point_OLM > radius) {
            position = result_OLM;
        }else if (checked_PUS_lat && checked_PUS_lon && check_point_PUS > radius) {
            position = result_PUS;
        }else if (checked_SAL_lat && checked_SAL_lon && check_point_SAL > radius) {
            position = result_SAL;
        }else if (checked_TAN_lat && checked_TAN_lon && check_point_TAN > radius) {
            position = result_TAN;
        }else if (checked_TRA1_lat && checked_TRA1_lon && check_point_TRA1 > radius) {
            position = result_TRA1;
        }else if (checked_TRA2_lat && checked_TRA2_lon && check_point_TRA2 > radius) {
            position = result_TRA2;
        }else if (checked_TRA3_lat && checked_TRA3_lon && check_point_TRA3 > radius) {
            position = result_TRA3;
        }else if (checked_TRA5_lat && checked_TRA5_lon && check_point_TRA5 > radius) {
            position = result_TRA5;
        }else if (checked_FRZ_lat && checked_FRZ_lon && check_point_FRZ > radius) {
            position = result_FRZ;
        }else if (checked_GAR_lat && checked_GAR_lon && check_point_GAR > radius) {
            position = result_GAR;
        }else if (checked_DEP_lat && checked_DEP_lon && check_point_DEP > radius) {
            position = result_DEP;
        }else if (checked_COM_lat && checked_COM_lon && check_point_COM > radius) {
            position = result_COM;
        }else if (checked_OKN_lat && checked_OKN_lon && check_point_OKN > radius) {
            position = result_OKN;
        }else if (checked_X38_lat && checked_X38_lon && check_point_X38 > radius) {
            position = result_X38;
        }
        else position = result_No_Point;




        String mail = channelSneMassage;
        // String mail = test_email;
        URL aURL = new URL("https://www.google.com.ua/maps/place/" + latitudeGeo + "+" + longTitudeGeo);
        String message = myLocation + System.lineSeparator() + "Текущая дата: " + dateText + System.lineSeparator() + "Текущее время: " + timeText + System.lineSeparator() + " " + System.lineSeparator() + aURL + System.lineSeparator() + System.lineSeparator() + position;
        String subject = "Геоданные";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(),mail,subject,message);

        javaMailAPI.execute();
        //textSubject.setText("");
        //textMessage.setText("");

        // обнуления изображений
        //choise_pic_1.setImageBitmap(null);
        //choise_pic_1.setImageURI(null);
        //choise_pic_2.setImageBitmap(null);
        //choise_pic_2.setImageURI(null);
    }
    // --------------------------------------------


    @Override
    public void onResume() {
        super.onResume();

        String latitudeGeo = mLatitudeTextView.getText().toString();
        String longTitudeGeo = mLongitudeTextView.getText().toString();

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Просим пользователя включить GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Настройка");
            builder.setMessage("Сейчас GPS отлючён.\n" + "Включить?");
            builder.setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Не обязательно
                            finish();
                        }

                        private void finish() {
                        }
                    });
            builder.create().show();
        }

        // Регистрируемся для обновлений
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES,
                mLocationListener);
        // Получаем текущие координаты при запуске
        showCurrentLocation();


        if (latitudeGeo != null & longTitudeGeo != null) {

            // Обновление точки на GoogleMaps
            SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mapView, mMapFragment);
            fragmentTransaction.commit();
            mMapFragment.getMapAsync(this);
            // ----------

        }

        // Вставка и поиск ближайшего объекта на UI

        String result_No_Point = "Без геопривязки объекта";
        // Константы Адресов предприятий.
        // Буфеты
        String result_office = "Офис, улица Киргизкая, 19";
        String result_xtz = "Буфет \"ХТЗ\",проспект Архитектора Альошина, 19";
        String result_shop = "Магазин \"Ельмир\", проспект Независимости, 5";
        String result_mia = "Буфет \"Майами\", улица Холодногорская, 2";
        String result_smz = "Буфет \"Симеиз\", улица Полтавский Шлях, 126";
        String result_fkh = "Производственная база \"Бали\", проспект Юбилейный, 50А";
        String result_ven = "Буфет \"Венеция\", проспект Гагарина, 177Б";
        String result_haw = "Буфет \"Гавайи\", улица Леся Сердюка, 44";
        String result_ibz = "Буфет \"Ибица\", улица Культуры, 23";
        String result_ist = "Буфет \"Источник\", проспект Науки, 38";
        String result_LGR = "Производственная база \"Куба\", улица Кривоконевская, 15";
        String result_LSV = "Буфет \"Лас-Вегас\", Салтовское Шоссе 262 Б";
        String result_MEL = "Буфет \"Мелодия\", переулок Костюринский, 3";
        String result_NIC = "Буфет \"Ницца\", проспект Любви Малой, 24";
        String result_PEK = "Производственная база \"Пекарня\", улица Мало-Панасовская, 2";
        String result_PRA = "Буфет \"Правда\", проспект Независимости, 5а";
        String result_RND = "Буфет \"Рандеву\", улица Пушкинская, 92/1";
        String result_STK = "Буфет \"Сан-Тропе\", улица Сумская, 41";
        String result_SLD = "Буфет \"Солдат\", улица 23-е Августа, 30";
        String result_STD = "Буфет \"Студенческая\", улица Валентиновская, 21";
        String result_TRG = "Буфет \"Таргет\", улица Академика Павлова, 120";
        String result_FIO = "Буфет \"Фиоль\", улица Пушкинская, 40";
        String result_JAM = "Буфет \"Ямайка\", Московский проспект, 256Б";
        String result_XAI = "Буфет \"ХАИ\", улица Чкалова, 2Д";

        // GoodTime
        String result_ALK = "GoodTime \"Алексеевка\", проспект Победы, 62";
        String result_SSH = "GoodTime \"Бутербродная\", Салтовское шоссе, 143";
        String result_GNM = "GoodTime \"Гном\", Алчевских, 40";
        String result_BUG = "GoodTime \"Жуковский\", ул. Академика Проскуры, 5";
        String result_LSG = "GoodTime \"Людвига Свободы\", проспект Людвига Свободы, 42";
        String result_MOL = "GoodTime \"Молодежная\", проспект Победы, 50";
        String result_OLM = "GoodTime \"Олимпийская\", ул. Олимпийская, 15а";
        String result_PUS = "GoodTime \"Пушкинская\", ул. Манизера, 1";
        String result_SAL = "GoodTime \"Салют\", проспект Героев Сталинграда, 12";
        String result_TAN = "GoodTime \"Танкопия\", улица Танкопия, 9/23";

        // Рестораны
        String result_TRA1 = "Ресторан \"Траттория Ломбардия (Траттория 1)\", улица Культуры, 22Б";
        String result_TRA2 = "Ресторан \"Траттория Сицилия (Траттория 2)\", проспект Науки, 19А";
        String result_TRA3 = "Ресторан \"Траттория Сардиния (Траттория 3)\", улица Ахсарова, 4/6A";
        String result_TRA5 = "Ресторан \"Траттория Калабрия (Траттория 5)\"";
        String result_FRZ = "Столовая \"Фрунзе\", улица Плехановская, 126а";

        // Предприятия Семко
        String result_GAR = "Пиццерия \"Гарибальди\", остановка ул. Гарибальди, напротив улица Валентиновская, 44";
        String result_DEP = "Производственная база \"Депо\", расположение на терретории Салтовского трамвайного депо";
        String result_COM = "Пиццерия \"Пицца.com\", проспект Героев Сталинграда, 155";
        String result_OKN = "Пиццерия \"Океан\", улица Бучмы";
        String result_X38 = "Пиццерия \"Пицца 38\", проспект Юбилейный";

        // Переменные, для вычисления попадания предприятия в радиусе координат gps
        // Буфеты
        double check_point_office;
        double check_point_xtz;
        double check_point_shop;
        double check_point_mia;
        double check_point_smz;
        double check_point_fkh;
        double check_point_ven;
        double check_point_haw;
        double check_point_ibz;
        double check_point_ist;
        double check_point_LGR;
        double check_point_LSV;
        double check_point_MEL;
        double check_point_NIC;
        double check_point_PEK;
        double check_point_PRA;
        double check_point_RND;
        double check_point_STK;
        double check_point_SLD;
        double check_point_STD;
        double check_point_TRG;
        double check_point_FIO;
        double check_point_JAM;
        double check_point_XAI;

        // GoodTime
        double check_point_ALK;
        double check_point_SSH;
        double check_point_GNM;
        double check_point_BUG;
        double check_point_LSG;
        double check_point_MOL;
        double check_point_OLM;
        double check_point_PUS;
        double check_point_SAL;
        double check_point_TAN;

        // Рестораны
        double check_point_TRA1;
        double check_point_TRA2;
        double check_point_TRA3;
        double check_point_TRA5;
        double check_point_FRZ;

        // Семко
        double check_point_GAR;
        double check_point_DEP;
        double check_point_COM;
        double check_point_OKN;
        double check_point_X38;



        // Вычисление
        check_point_office = Math.sqrt(latitude_office*latitude_office + longtitude_office*longtitude_office);
        check_point_xtz = Math.sqrt(latitude_xtz*latitude_xtz + longtitude_xtz*longtitude_xtz);
        check_point_shop = Math.sqrt(latitude_shop*latitude_shop + longtitude_shop*longtitude_shop);
        check_point_mia = Math.sqrt(latitude_mia*latitude_mia + longtitude_mia*longtitude_mia);
        check_point_smz = Math.sqrt(latitude_smz*latitude_smz + longtitude_smz*longtitude_smz);
        check_point_fkh = Math.sqrt(latitude_fkh*latitude_fkh + longtitude_fkh*longtitude_fkh);
        check_point_ven = Math.sqrt(latitude_ven*latitude_ven + longtitude_ven*longtitude_ven);
        check_point_haw = Math.sqrt(latitude_haw*latitude_haw + longtitude_haw*longtitude_haw);
        check_point_ibz = Math.sqrt(latitude_ibz*latitude_ibz + longtitude_ibz*longtitude_ibz);
        check_point_ist = Math.sqrt(latitude_ist*latitude_ist + longtitude_ist*longtitude_ist);
        check_point_LGR = Math.sqrt(latitude_LGR*latitude_LGR + longtitude_LGR*longtitude_LGR);
        check_point_LSV = Math.sqrt(latitude_LSV*latitude_LSV + longtitude_LSV*longtitude_LSV);
        check_point_MEL = Math.sqrt(latitude_MEL*latitude_MEL + longtitude_MEL*longtitude_MEL);
        check_point_NIC = Math.sqrt(latitude_NIC*latitude_NIC + longtitude_NIC*longtitude_NIC);
        check_point_PEK = Math.sqrt(latitude_PEK*latitude_PEK + longtitude_PEK*longtitude_PEK);
        check_point_PRA = Math.sqrt(latitude_PRA*latitude_PRA + longtitude_PRA*longtitude_PRA);
        check_point_RND = Math.sqrt(latitude_RND*latitude_RND + longtitude_RND*longtitude_RND);
        check_point_STK = Math.sqrt(latitude_STK*latitude_STK + longtitude_STK*longtitude_STK);
        check_point_SLD = Math.sqrt(latitude_SLD*latitude_SLD + longtitude_SLD*longtitude_SLD);
        check_point_STD = Math.sqrt(latitude_STD*latitude_STD + longtitude_STD*longtitude_STD);
        check_point_TRG = Math.sqrt(latitude_TRG*latitude_TRG + longtitude_TRG*longtitude_TRG);
        check_point_FIO = Math.sqrt(latitude_FIO*latitude_FIO + longtitude_FIO*longtitude_FIO);
        check_point_JAM = Math.sqrt(latitude_JAM*latitude_JAM + longtitude_JAM*longtitude_JAM);
        check_point_XAI = Math.sqrt(latitude_XAI*latitude_XAI + longtitude_XAI*longtitude_XAI);

        check_point_ALK = Math.sqrt(latitude_ALK*latitude_ALK + longtitude_ALK*longtitude_ALK);
        check_point_SSH = Math.sqrt(latitude_SSH*latitude_SSH + longtitude_SSH*longtitude_SSH);
        check_point_GNM = Math.sqrt(latitude_GNM*latitude_GNM + longtitude_GNM*longtitude_GNM);
        check_point_BUG = Math.sqrt(latitude_BUG*latitude_BUG + longtitude_BUG*longtitude_BUG);
        check_point_LSG = Math.sqrt(latitude_LSG*latitude_LSG + longtitude_LSG*longtitude_LSG);
        check_point_MOL = Math.sqrt(latitude_MOL*latitude_MOL + longtitude_MOL*longtitude_MOL);
        check_point_OLM = Math.sqrt(latitude_OLM*latitude_OLM + longtitude_OLM*longtitude_OLM);
        check_point_PUS = Math.sqrt(latitude_PUS*latitude_PUS + longtitude_PUS*longtitude_PUS);
        check_point_SAL = Math.sqrt(latitude_SAL*latitude_SAL + longtitude_SAL*longtitude_SAL);
        check_point_TAN = Math.sqrt(latitude_TAN*latitude_TAN + longtitude_TAN*longtitude_TAN);

        check_point_TRA1 = Math.sqrt(latitude_TRA1*latitude_TRA1 + longtitude_TRA1*longtitude_TRA1);
        check_point_TRA2 = Math.sqrt(latitude_TRA2*latitude_TRA2 + longtitude_TRA2*longtitude_TRA2);
        check_point_TRA3 = Math.sqrt(latitude_TRA3*latitude_TRA3 + longtitude_TRA3*longtitude_TRA3);
        check_point_TRA5 = Math.sqrt(latitude_TRA5*latitude_TRA5 + longtitude_TRA5*longtitude_TRA5);
        check_point_FRZ = Math.sqrt(latitude_FRZ*latitude_FRZ + longtitude_FRZ*longtitude_FRZ);

        check_point_GAR = Math.sqrt(latitude_GAR*latitude_GAR + longtitude_GAR*longtitude_GAR);
        check_point_DEP = Math.sqrt(latitude_DEP*latitude_DEP + longtitude_DEP*longtitude_DEP);
        check_point_COM = Math.sqrt(latitude_COM*latitude_COM + longtitude_COM*longtitude_COM);
        check_point_OKN = Math.sqrt(latitude_OKN*latitude_OKN + longtitude_OKN*longtitude_OKN);
        check_point_X38 = Math.sqrt(latitude_X38*latitude_X38 + longtitude_X38*longtitude_X38);

        // Офис
        boolean checked_OFFICE_lat = latitudeGeo.startsWith("49.947") || latitudeGeo.startsWith("49.948");
        boolean checked_OFFICE_lon = longTitudeGeo.startsWith("36.290") || longTitudeGeo.startsWith("36.291") || longTitudeGeo.startsWith("36.292");
        // ХТЗ
        boolean checked_XTZ_lat = latitudeGeo.startsWith("49.943") || latitudeGeo.startsWith("49.942");
        boolean checked_XTZ_lon = longTitudeGeo.startsWith("36.370") || longTitudeGeo.startsWith("36.369");
        // Ельмир
        boolean checked_SHOP_lat = latitudeGeo.startsWith("50.008");
        boolean checked_SHOP_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.225");
        // Майами
        boolean checked_MIA_lat = latitudeGeo.startsWith("49.981");
        boolean checked_MIA_lon = longTitudeGeo.startsWith("36.175") || longTitudeGeo.startsWith("36.176")|| longTitudeGeo.startsWith("36.177");
        // Симеиз
        boolean checked_SMZ_lat = latitudeGeo.startsWith("49.985");
        boolean checked_SMZ_lon = longTitudeGeo.startsWith("36.189") || longTitudeGeo.startsWith("36.190");
        // Бали
        boolean checked_FKH_lat = latitudeGeo.startsWith("49.995");
        boolean checked_FKH_lon = longTitudeGeo.startsWith("36.333");
        // Венеция
        boolean checked_VEN_lat = latitudeGeo.startsWith("49.947") || latitudeGeo.startsWith("49.948");
        boolean checked_VEN_lon = longTitudeGeo.startsWith("36.261");
        // Гавайи
        boolean checked_HAW_lat = latitudeGeo.startsWith("50.038");
        boolean checked_HAW_lon = longTitudeGeo.startsWith("36.347") || longTitudeGeo.startsWith("36.348");
        // Ибица
        boolean checked_IBZ_lat = latitudeGeo.startsWith("50.011");
        boolean checked_IBZ_lon = longTitudeGeo.startsWith("36.225") || longTitudeGeo.startsWith("36.226");
        // Источник
        boolean checked_IST_lat = latitudeGeo.startsWith("50.022") || latitudeGeo.startsWith("50.023");
        boolean checked_IST_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.225");
        // Куба
        boolean checked_LGR_lat = latitudeGeo.startsWith("50.004");
        boolean checked_LGR_lon = longTitudeGeo.startsWith("36.184") || longTitudeGeo.startsWith("36.185");
        // Лас-Вегас
        boolean checked_LSV_lat = latitudeGeo.startsWith("49.987") || latitudeGeo.startsWith("49.988");
        boolean checked_LSV_lon = longTitudeGeo.startsWith("36.358") || longTitudeGeo.startsWith("36.359");
        // Мелодия
        boolean checked_MEL_lat = latitudeGeo.startsWith("49.987") || latitudeGeo.startsWith("49.988");
        boolean checked_MEL_lon = longTitudeGeo.startsWith("36.233") || longTitudeGeo.startsWith("36.234");
        // Ницца
        boolean checked_NIC_lat = latitudeGeo.startsWith("49.966") || latitudeGeo.startsWith("49.967");
        boolean checked_NIC_lon = longTitudeGeo.startsWith("36.182") || longTitudeGeo.startsWith("36.183");
        // Пекарня
        boolean checked_PEK_lat = latitudeGeo.startsWith("49.994");
        boolean checked_PEK_lon = longTitudeGeo.startsWith("36.208");
        // Правда
        boolean checked_PRA_lat = latitudeGeo.startsWith("50.006") || latitudeGeo.startsWith("50.007");
        boolean checked_PRA_lon = longTitudeGeo.startsWith("36.225");
        // Рандеву
        boolean checked_RND_lat = latitudeGeo.startsWith("50.006");
        boolean checked_RND_lon = longTitudeGeo.startsWith("36.250") || longTitudeGeo.startsWith("36.251");
        // Сан-Тропе
        boolean checked_STK_lat = latitudeGeo.startsWith("50.005") || latitudeGeo.startsWith("50.006");
        boolean checked_STK_lon = longTitudeGeo.startsWith("36.235") || longTitudeGeo.startsWith("36.236");
        // Солдат
        boolean checked_SLD_lat = latitudeGeo.startsWith("50.034");
        boolean checked_SLD_lon = longTitudeGeo.startsWith("36.222") || longTitudeGeo.startsWith("36.223");
        // Студенческая
        boolean checked_STD_lat = latitudeGeo.startsWith("50.017") || latitudeGeo.startsWith("50.018");
        boolean checked_STD_lon = longTitudeGeo.startsWith("36.329") || longTitudeGeo.startsWith("36.330") || longTitudeGeo.startsWith("36.331");
        // Таргет
        boolean checked_TRG_lat = latitudeGeo.startsWith("50.008") || latitudeGeo.startsWith("50.009");
        boolean checked_TRG_lon = longTitudeGeo.startsWith("36.319") || longTitudeGeo.startsWith("36.320");
        // Фиоль
        boolean checked_FIO_lat = latitudeGeo.startsWith("49.997");
        boolean checked_FIO_lon = longTitudeGeo.startsWith("36.239") || longTitudeGeo.startsWith("36.240");
        // Ямайка
        boolean checked_JAM_lat = latitudeGeo.startsWith("49.956") || latitudeGeo.startsWith("49.957");
        boolean checked_JAM_lon = longTitudeGeo.startsWith("36.360") || longTitudeGeo.startsWith("36.361");
        // ХАИ
        boolean checked_XAI_lat = latitudeGeo.startsWith("50.041");
        boolean checked_XAI_lon = longTitudeGeo.startsWith("36.281") || longTitudeGeo.startsWith("36.282") || longTitudeGeo.startsWith("36.283");


        // Алексеевка
        boolean checked_ALK_lat = latitudeGeo.startsWith("50.059");
        boolean checked_ALK_lon = longTitudeGeo.startsWith("36.203");
        // Бутербродная
        boolean checked_SSH_lat = latitudeGeo.startsWith("49.989") || latitudeGeo.startsWith("49.990");
        boolean checked_SSH_lon = longTitudeGeo.startsWith("36.349");
        // Гном
        boolean checked_GNM_lat = latitudeGeo.startsWith("50.005");
        boolean checked_GNM_lon = longTitudeGeo.startsWith("36.243") || longTitudeGeo.startsWith("36.244");
        // Жуковский
        boolean checked_BUG_lat = latitudeGeo.startsWith("50.046") || latitudeGeo.startsWith("50.047");
        boolean checked_BUG_lon = longTitudeGeo.startsWith("36.289") || longTitudeGeo.startsWith("36.290");
        // Людвига Свободы
        boolean checked_LSG_lat = latitudeGeo.startsWith("50.055");
        boolean checked_LSG_lon = longTitudeGeo.startsWith("36.204");
        // Молодежная
        boolean checked_MOL_lat = latitudeGeo.startsWith("50.054") || latitudeGeo.startsWith("50.053");
        boolean checked_MOL_lon = longTitudeGeo.startsWith("36.196");
        // Олимпийская
        boolean checked_OLM_lat = latitudeGeo.startsWith("49.958");
        boolean checked_OLM_lon = longTitudeGeo.startsWith("36.311") || longTitudeGeo.startsWith("36.310");
        // Пушкинская
        boolean checked_PUS_lat = latitudeGeo.startsWith("49.999");
        boolean checked_PUS_lon = longTitudeGeo.startsWith("36.243") || longTitudeGeo.startsWith("36.242");
        // Салют
        boolean checked_SAL_lat = latitudeGeo.startsWith("49.944") || latitudeGeo.startsWith("49.943");
        boolean checked_SAL_lon = longTitudeGeo.startsWith("36.274");
        // Танкопия
        boolean checked_TAN_lat = latitudeGeo.startsWith("49.955");
        boolean checked_TAN_lon = longTitudeGeo.startsWith("36.316") || longTitudeGeo.startsWith("36.317");


        // Траттория 1
        boolean checked_TRA1_lat = latitudeGeo.startsWith("50.013") || latitudeGeo.startsWith("50.012");
        boolean checked_TRA1_lon = longTitudeGeo.startsWith("36.230") || longTitudeGeo.startsWith("36.231") || longTitudeGeo.startsWith("36.232");
        // Траттория 2
        boolean checked_TRA2_lat = latitudeGeo.startsWith("50.018");
        boolean checked_TRA2_lon = longTitudeGeo.startsWith("36.224") || longTitudeGeo.startsWith("36.223");
        // Траттория 3
        boolean checked_TRA3_lat = latitudeGeo.startsWith("50.050") || latitudeGeo.startsWith("50.049");
        boolean checked_TRA3_lon = longTitudeGeo.startsWith("36.196");
        // Траттория 5
        boolean checked_TRA5_lat = latitudeGeo.startsWith("50.060") || latitudeGeo.startsWith("50.059");
        boolean checked_TRA5_lon = longTitudeGeo.startsWith("36.203") || longTitudeGeo.startsWith("36.202");
        // Фрунзе
        boolean checked_FRZ_lat = latitudeGeo.startsWith("49.969") || latitudeGeo.startsWith("49.968") || latitudeGeo.startsWith("49.967") || latitudeGeo.startsWith("49.966");
        boolean checked_FRZ_lon = longTitudeGeo.startsWith("36.274") || longTitudeGeo.startsWith("36.275") || longTitudeGeo.startsWith("36.273") || longTitudeGeo.startsWith("36.272");


        // Гарибальди
        boolean checked_GAR_lat = latitudeGeo.startsWith("50.007");
        boolean checked_GAR_lon = longTitudeGeo.startsWith("36.357") || longTitudeGeo.startsWith("36.358");
        // Депо
        boolean checked_DEP_lat = latitudeGeo.startsWith("50.030") || latitudeGeo.startsWith("50.055");
        boolean checked_DEP_lon = longTitudeGeo.startsWith("36.36") || longTitudeGeo.startsWith("36.204");
        // Пицца.com
        boolean checked_COM_lat = latitudeGeo.startsWith("49.943") || latitudeGeo.startsWith("49.944");
        boolean checked_COM_lon = longTitudeGeo.startsWith("36.299") || longTitudeGeo.startsWith("36.300");
        // Океан
        boolean checked_OKN_lat = latitudeGeo.startsWith("50.027") || latitudeGeo.startsWith("50.028");
        boolean checked_OKN_lon = longTitudeGeo.startsWith("36.353");
        // Пицца 38
        boolean checked_X38_lat = latitudeGeo.startsWith("49.997") || latitudeGeo.startsWith("49.998");
        boolean checked_X38_lon = longTitudeGeo.startsWith("36.329");

        if (checked_OFFICE_lat && checked_OFFICE_lon && check_point_office > radius) {
            UIShowPoint = result_office;
        }else if (checked_XTZ_lat && checked_XTZ_lon && check_point_xtz > radius) {
            UIShowPoint = result_xtz;
        }else if (checked_SHOP_lat && checked_SHOP_lon && check_point_shop > radius) {
            UIShowPoint = result_shop;
        }else if (checked_MIA_lat && checked_MIA_lon && check_point_mia > radius) {
            UIShowPoint = result_mia;
        }else if (checked_SMZ_lat && checked_SMZ_lon && check_point_smz > radius) {
            UIShowPoint = result_smz;
        }else if (checked_FKH_lat && checked_FKH_lon && check_point_fkh > radius) {
            UIShowPoint = result_fkh;
        }else if (checked_VEN_lat && checked_VEN_lon && check_point_ven > radius) {
            UIShowPoint = result_ven;
        }else if (checked_HAW_lat && checked_HAW_lon && check_point_haw > radius) {
            UIShowPoint = result_haw;
        }else if (checked_IBZ_lat && checked_IBZ_lon && check_point_ibz > radius) {
            UIShowPoint = result_ibz;
        }else if (checked_IST_lat && checked_IST_lon && check_point_ist > radius) {
            UIShowPoint = result_No_Point;
        }else if (checked_LGR_lat && checked_LGR_lon && check_point_LGR > radius) {
            UIShowPoint = result_LGR;
        }else if (checked_LSV_lat && checked_LSV_lon && check_point_LSV > radius) {
            UIShowPoint = result_LSV;
        }else if (checked_MEL_lat && checked_MEL_lon && check_point_MEL > radius) {
            UIShowPoint = result_MEL;
        }else if (checked_NIC_lat && checked_NIC_lon && check_point_NIC > radius) {
            UIShowPoint = result_NIC;
        }else if (checked_PEK_lat && checked_PEK_lon && check_point_PEK > radius) {
            UIShowPoint = result_PEK;
        }else if (checked_PRA_lat && checked_PRA_lon && check_point_PRA > radius) {
            UIShowPoint = result_PRA;
        }else if (checked_RND_lat && checked_RND_lon && check_point_RND > radius) {
            UIShowPoint = result_RND;
        }else if (checked_STK_lat && checked_STK_lon && check_point_STK > radius) {
            UIShowPoint = result_STK;
        }else if (checked_SLD_lat && checked_SLD_lon && check_point_SLD > radius) {
            UIShowPoint = result_SLD;
        }else if (checked_STD_lat && checked_STD_lon && check_point_STD > radius) {
            UIShowPoint = result_STD;
        }else if (checked_TRG_lat && checked_TRG_lon && check_point_TRG > radius) {
            UIShowPoint = result_TRG;
        }else if (checked_FIO_lat && checked_FIO_lon && check_point_FIO > radius) {
            UIShowPoint = result_FIO;
        }else if (checked_JAM_lat && checked_JAM_lon && check_point_JAM > radius) {
            UIShowPoint = result_JAM;
        }else if (checked_XAI_lat && checked_XAI_lon && check_point_XAI > radius) {
            UIShowPoint = result_XAI;
        }else if (checked_ALK_lat && checked_ALK_lon && check_point_ALK > radius) {
            UIShowPoint = result_ALK;
        }else if (checked_SSH_lat && checked_SSH_lon && check_point_SSH > radius) {
            UIShowPoint = result_SSH;
        }else if (checked_GNM_lat && checked_GNM_lon && check_point_GNM > radius) {
            UIShowPoint = result_GNM;
        }else if (checked_BUG_lat && checked_BUG_lon && check_point_BUG > radius) {
            UIShowPoint = result_BUG;
        }else if (checked_LSG_lat && checked_LSG_lon && check_point_LSG > radius) {
            UIShowPoint = result_LSG;
        }else if (checked_MOL_lat && checked_MOL_lon && check_point_MOL > radius) {
            UIShowPoint = result_MOL;
        }else if (checked_OLM_lat && checked_OLM_lon && check_point_OLM > radius) {
            UIShowPoint = result_OLM;
        }else if (checked_PUS_lat && checked_PUS_lon && check_point_PUS > radius) {
            UIShowPoint = result_PUS;
        }else if (checked_SAL_lat && checked_SAL_lon && check_point_SAL > radius) {
            UIShowPoint = result_SAL;
        }else if (checked_TAN_lat && checked_TAN_lon && check_point_TAN > radius) {
            UIShowPoint = result_TAN;
        }else if (checked_TRA1_lat && checked_TRA1_lon && check_point_TRA1 > radius) {
            UIShowPoint = result_TRA1;
        }else if (checked_TRA2_lat && checked_TRA2_lon && check_point_TRA2 > radius) {
            UIShowPoint = result_TRA2;
        }else if (checked_TRA3_lat && checked_TRA3_lon && check_point_TRA3 > radius) {
            UIShowPoint = result_TRA3;
        }else if (checked_TRA5_lat && checked_TRA5_lon && check_point_TRA5 > radius) {
            UIShowPoint = result_TRA5;
        }else if (checked_FRZ_lat && checked_FRZ_lon && check_point_FRZ > radius) {
            UIShowPoint = result_FRZ;
        }else if (checked_GAR_lat && checked_GAR_lon && check_point_GAR > radius) {
            UIShowPoint = result_GAR;
        }else if (checked_DEP_lat && checked_DEP_lon && check_point_DEP > radius) {
            UIShowPoint = result_DEP;
        }else if (checked_COM_lat && checked_COM_lon && check_point_COM > radius) {
            UIShowPoint = result_COM;
        }else if (checked_OKN_lat && checked_OKN_lon && check_point_OKN > radius) {
            UIShowPoint = result_OKN;
        }else if (checked_X38_lat && checked_X38_lon && check_point_X38 > radius) {
            UIShowPoint = result_X38;
        }
        else UIShowPoint = result_No_Point;

        showMyLocation.setText(UIShowPoint);



    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    protected void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Location location = mLocationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        String latitudeGeo = mLatitudeTextView.getText().toString();
        String longTitudeGeo = mLongitudeTextView.getText().toString();

        Double geo_1 = Double.valueOf(latitudeGeo);
        Double geo_2 = Double.valueOf(longTitudeGeo);

        // [START_EXCLUDE silent]
        //
        //
        // [END_EXCLUDE]
        LatLng pro100_pos = new LatLng(geo_1, geo_2);
        googleMap.addMarker(new MarkerOptions()
                .position(pro100_pos)
                .title("Позиция"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pro100_pos, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        // [START_EXCLUDE silent]
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 5, null);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pro100_pos));
        // [END_EXCLUDE]

    }

    // Прослушиваем изменения
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = "Новое местоположение \n Долгота: " +
                    location.getLongitude() + "\n Широта: " + location.getLatitude();
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                    .show();
            showCurrentLocation();

        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(getActivity(), "Статус провайдера изменился",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(getActivity(),
                    "Провайдер заблокирован пользователем. GPS выключен",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(getActivity(),
                    "Провайдер включен пользователем. GPS включён",
                    Toast.LENGTH_LONG).show();
        }
    }


}
