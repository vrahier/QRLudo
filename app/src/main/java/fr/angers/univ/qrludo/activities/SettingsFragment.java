package fr.angers.univ.qrludo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import fr.angers.univ.qrludo.R;
import fr.angers.univ.qrludo.utils.FileDowloader;

public class SettingsFragment extends PreferenceFragment {

    SharedPreferences settings;
    SharedPreferences.Editor edit;
    float speedSpeech;
    float mdt;
    float sr;
    boolean webOpeningViaBrowser;
    Locale m_ttslanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_headers);
        settings = this.getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        edit = settings.edit();


        speedSpeech = settings.getFloat("speechSpeed",MainActivity.SPEEDSPEECH_DEFAULT);
        mdt = settings.getFloat("MDTime",MainActivity.DEFAULT_MULTIPLE_DETECTION_TIME);
        sr = settings.getFloat("resetTime2",MainActivity.DEFAULT_CONTENT_RESET_TIME);
        webOpeningViaBrowser = settings.getBoolean("WebOpening", MainActivity.DEFAULT_WEB_OPENING_VIA_BROWSER);
        m_ttslanguage = new Locale(settings.getString("speechLanguage",MainActivity.LOCALE_DEFAULT.getLanguage()),settings.getString("speechCountry",MainActivity.LOCALE_DEFAULT.getCountry()));


//        Toast.makeText(getActivity(), "Vitesse de lecture : " + speedSpeech, Toast.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "MDTime : " + speedSpeech, Toast.LENGTH_LONG).show();
//        Toast.makeText(getActivity(), "Reset time " + speedSpeech, Toast.LENGTH_LONG).show();
        if (webOpeningViaBrowser)
            Toast.makeText(getActivity(), "Ouvertue fichier : NAVIGATEUR", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Ouvertue fichier : LECTURE VOCALE", Toast.LENGTH_SHORT).show();



//        android:key="pref_langue"
//        android:key="pref_vitesse_synthese"
//        android:key="pref_delai_detection"
//        android:key="pref_lecture_web"

        final Preference p_vitesse = findPreference("pref_vitesse_synthese");
        p_vitesse.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Vitesse synthèse vocale");

                final View sliderView = View.inflate(getActivity(), R.layout.slide_preference, null);
                builder.setView(sliderView);

                SeekBar slider = (SeekBar)sliderView.findViewById(R.id.slider);

                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekBar.setProgress(progress);
                        speedSpeech = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                builder.setPositiveButton("Confirmer",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyPreferences();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();


                return true;
            }
        });

        final CheckBoxPreference p_lectureWeb = (CheckBoxPreference) getPreferenceManager().findPreference("pref_lecture_web");
        if (webOpeningViaBrowser)
            p_lectureWeb.setChecked(true);
        else
            p_lectureWeb.setChecked(false);

        p_lectureWeb.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                webOpeningViaBrowser = !p_lectureWeb.isChecked();
                modifyPreferences();
                return true;
            }
        });

        //Suppression des fichiers
        Preference p_supprimer  = findPreference("pref_supprime");

        p_supprimer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Suppression");
                builder.setMessage("Les données téléchargées vont être supprimées");
                builder.setPositiveButton("Confirmer",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(FileDowloader.viderMemoire()) {
                                    Toast.makeText(getActivity(), "Les données ont bien été supprimées", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getActivity(), "Le dossier n'existe pas", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    private void modifyPreferences()
    {
//        Locale m_ttslanguage;
        edit.putFloat("speechSpeed", speedSpeech);
        edit.putFloat("resetTime2", sr);
        edit.putFloat("MDTime", mdt);
        edit.putBoolean("WebOpening", webOpeningViaBrowser);


        Toast.makeText(getActivity(), "Vitesse lecture = " + speedSpeech, Toast.LENGTH_SHORT).show();

//        edit.putString("speechCountry", countries.get(spin_language.getSelectedItemPosition()));
//        Log.d("Country", countries.get(spin_language.getSelectedItemPosition()));
//        edit.putString("speechLanguage", langs.get(spin_language.getSelectedItemPosition()));
//        Log.d("Language", langs.get(spin_language.getSelectedItemPosition()));



        edit.apply();
    }
}
