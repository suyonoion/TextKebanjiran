package com.suyonoion.textkebanjiran;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tujuanFolderRes("activity_main","layout"));
    }
    public int tujuanFolderRes(String NamaFile, String NamaFolder)
    {
        return getBaseContext().getResources().getIdentifier(NamaFile, NamaFolder, getBaseContext().getPackageName());
    }
}
