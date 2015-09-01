package com.example.avaron.test02;

/**
 * Created by Avaron on 05/06/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.avaron.test02.R;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
        }


        public void onWallpaperSettings(View view) {
            Intent wallpaperSettings = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(wallpaperSettings);
        }
}

