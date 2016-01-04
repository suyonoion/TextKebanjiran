package com.suyonoion.textkebanjiran;

/**
 * Created by Suyono on 1/4/2016.
 * Copyright (c) 2016 by Suyono (ion).
 * All rights reserved.
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.text.TextUtils;

@SuppressWarnings("ALL")
public class GantiTextPref extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{

    public int tujuanFolderRes(String NamaFile, String NamaFolder)
    {
        return getBaseContext().getResources().getIdentifier(NamaFile, NamaFolder, getBaseContext().getPackageName());
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        addPreferencesFromResource(tujuanFolderRes("pref_ganti_text", "xml"));
        EditTextPreference edit_pref = (EditTextPreference)findPreference("gantiTextOmbak");
        String string_txt = Settings.System.getString(getContentResolver(), "ganti_text_ombak");
        if(!TextUtils.isEmpty(string_txt)){
            edit_pref.setSummary(string_txt);
        }
        EditTextPreference edit_pref1 = (EditTextPreference)findPreference("gantiUkuranOmbak");
        String string_txt1 = Settings.System.getString(getContentResolver(), "ukuran_ombak");
        if(!TextUtils.isEmpty(string_txt1)){
            edit_pref1.setSummary(string_txt1);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences options, String key) {
        if (key.equals("gantiTextOmbak")){
            Preference pref_text= findPreference(key);
            String string_ombak = "";
            if(pref_text instanceof EditTextPreference){
                string_ombak = ((EditTextPreference)pref_text).getText();
            }
            pref_text.setSummary(string_ombak);
            Settings.System.putString(getContentResolver(), "ganti_text_ombak", string_ombak);
        }
        if (key.equals("gantiUkuranOmbak")){
            Preference pref_ukuran= findPreference(key);
            String string_ombak1 = "";
            if(pref_ukuran instanceof EditTextPreference){
                string_ombak1 = ((EditTextPreference)pref_ukuran).getText();
            }
            pref_ukuran.setSummary(string_ombak1);
            Settings.System.putString(getContentResolver(), "ukuran_ombak", string_ombak1);
        }
    }
}
