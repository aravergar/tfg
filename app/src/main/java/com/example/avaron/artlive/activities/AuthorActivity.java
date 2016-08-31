package com.example.avaron.artlive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.avaron.artlive.R;
import com.example.avaron.artlive.db.Author;
import com.example.avaron.artlive.db.Style;
import com.example.avaron.artlive.db.WallpaperDAO;

/**
 * Created by Avaron on 10/07/2016.
 */
public class AuthorActivity extends AppCompatActivity {
    private long authorId;
    private Author author;
    private Style style;
    private TextView bioText;
    private TextView styleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.author_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowTitleEnabled(true);
//        actBar.setLogo(R.drawable.logo);
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);
//        actBar.setHomeAsUpIndicator(R.drawable.logo);
//        actBar.setTitle("Fondo Animado");
        Bundle b = getIntent().getExtras();
        authorId = b.getLong("_id");
        WallpaperDAO wallpaperDAO = new WallpaperDAO(this);
        wallpaperDAO.open();
        author = wallpaperDAO.getAuthorById(authorId);
        style = wallpaperDAO.getStyleById(author.getStyleId());

        actBar.setTitle(author.getName());
        bioText = (TextView) findViewById(R.id.text_author_bio);
        bioText.setText(author.getBiography());

        styleText = (TextView) findViewById(R.id.text_author_style);
        styleText.setText(style.getName());

        wallpaperDAO.close();
    }

    public void launchStyleActivity(View view) {
        Intent styleActivity = new Intent(this, StyleActivity.class);
        styleActivity.putExtra("_id",author.getStyleId());
        startActivity(styleActivity);
    }
}
