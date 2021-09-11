package xyz.devdiscovery.odoocourier.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import xyz.devdiscovery.odoocourier.R;


public class MySettingsFragment extends Fragment {
    private MySettingsViewModel mySettingsViewModel;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_TOOL = "Tool";
    public static final String YOU_MAIL_CONST = "Ваш EMAIL адрес: ";


    Button save_settings;
    Button load_settings;
    Button removeSettings;
    TextView show_settings;
    TextView field_mail_sett;
    SharedPreferences mSettings;
    public static String CONST_EMAIL = "";


    public MySettingsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mySettingsViewModel = ViewModelProviders.of(this).get(MySettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        save_settings = (Button) root.findViewById(R.id.save_settings);
        // load_settings = (Button) root.findViewById(R.id.load_settings);
        show_settings = root.findViewById(R.id.show_settings);
        EditText enterPostBox = root.findViewById(R.id.enter_mailadress);
        field_mail_sett = root.findViewById(R.id.field_mail_settings);
        removeSettings = (Button) root.findViewById(R.id.delete_settings);

        // показываем пользователю его адрес с которым он работает
        if (mSettings.contains(APP_PREFERENCES_SETTINGS)) {
            // выводим данные в TextView
            show_settings.setText(YOU_MAIL_CONST + mSettings.getString(APP_PREFERENCES_SETTINGS,
                    ""));
        }
        if (show_settings!=null){
            CONST_EMAIL = show_settings.getText().toString();
        }



        save_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String strPostAdress = enterPostBox.getText().toString();

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_SETTINGS, strPostAdress);
                editor.apply();

                Toast.makeText(getActivity(), "Параметры сохранены",Toast.LENGTH_LONG)
                        .show();

            }
        });

        /* // Условия для скрытия элементов UI, если были сохранены данные
           if (CONST_EMAIL!=null){
            save_settings.setVisibility(View.GONE);
         } if (CONST_EMAIL!=null){
            enterPostBox.setVisibility(View.GONE);
         } if (CONST_EMAIL!=null){
            field_mail_sett.setVisibility(View.GONE);
        } else enterPostBox.setVisibility(View.VISIBLE);
               field_mail_sett.setVisibility(View.VISIBLE);
               save_settings.setVisibility(View.VISIBLE);
        */

        removeSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor editor = mSettings.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(getActivity(), "Параметры настроек удалены",Toast.LENGTH_LONG)
                        .show();

            }
        });

    //    load_settings.setOnClickListener(new View.OnClickListener() {
    //        public void onClick(View v) {

    //            if (mSettings.contains(APP_PREFERENCES_SETTINGS)) {
                    // выводим данные в TextView
    //                show_settings.setText(YOU_MAIL_CONST + mSettings.getString(APP_PREFERENCES_SETTINGS,
    //                        ""));
    //            }

    //        }
    //    });

        return root;
    }



}

