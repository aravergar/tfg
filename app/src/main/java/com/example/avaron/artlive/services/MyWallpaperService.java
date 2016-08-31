package com.example.avaron.artlive.services;

import android.content.IntentFilter;
import android.graphics.Movie;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.service.wallpaper.WallpaperService;
import android.os.Handler;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.example.avaron.artlive.activities.MainActivity;

import java.io.IOException;
import java.util.GregorianCalendar;

public class MyWallpaperService extends WallpaperService {

//    @Override
//    public Engine onCreateEngine() {
//        return new Test02WallpaperServiceEngine();
//    }

    protected String filenames;

    public Engine onCreateEngine() {
        filenames = MainActivity.filenames;
        String[] filenameslist = filenames.split("\\s+");
        try {
            Movie movie = Movie.decodeStream(
                    getResources().getAssets().open(filenameslist[0])
            );
            Test03WallpaperServiceEngine engine = new Test03WallpaperServiceEngine(movie);
            engine.setFilenameslist(filenameslist);
            return engine;
        } catch(IOException e) {
            return null;
        }
    }

    private class Test03WallpaperServiceEngine extends Engine {
        private final int frameDuration = 20;
        private SurfaceHolder holder;
        private Movie movie;
        private static final int WALLPAPER_DAY = 1;
        private static final int WALLPAPER_NIGHT = 2;
        private static final int WALLPAPER_BATTERY = 3;
        private int state = 0;

        private final Handler handler;
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        //private Drawable bgImage;
        private ImageView bgImage;
        private Paint paint;
        private int bgColor;
        private int orientation;
        private boolean visible = true;
        private boolean scaled = false;
        private Rect clipBounds;
        private Rect imgBounds;
        private Pair <Float, Float> scale;
        float imgRatio, clipRatio;
        private Picture picture;
        private String[] filenameslist;
        IntentFilter ifilter;
        Intent batteryStatus;
        GregorianCalendar calendar;
        int hour;

        public Test03WallpaperServiceEngine (Movie movie) {
            this.movie = movie;
            handler = new Handler();
            state = WALLPAPER_DAY;

            orientation = getResources().getConfiguration().orientation;

            imgBounds = new Rect();
        }

        public void setFilenameslist(String[] filenameslist) {
            this.filenameslist = filenameslist;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            try {
                super.onCreate(surfaceHolder);
                this.holder = surfaceHolder;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        private void setCanvasSize(Canvas canvas) {
            int width, height;
            imgRatio = (float)movie.height() / (float)movie.width();
            clipRatio = (float)canvas.getClipBounds().height() / (float)canvas.getClipBounds().width();
            if(imgRatio > clipRatio) {
                width = (int)(canvas.getClipBounds().height() / imgRatio);
                height = canvas.getClipBounds().height();
                scale = new Pair((float)width / (float)movie.width(), (float)height / (float)movie.height());

            } else {
                height = (int)(canvas.getClipBounds().width() * imgRatio);
                width = canvas.getClipBounds().width();
                scale = new Pair((float)width / (float)movie.width(), (float)height / (float)movie.height());
            }
            canvas.scale(scale.first, scale.second);
        }

        private void updateCanvas(float batteryPct, int hour, Canvas canvas) {
            if(batteryPct > 0.2f) {
                if(hour > 20 || hour < 8) {
                    if(state != WALLPAPER_NIGHT) {
                        try {
                            Movie movie = Movie.decodeStream(
                                    getResources().getAssets().open(filenameslist[1])
                            );
                            this.movie = movie;
                        } catch (IOException e) {
                            System.out.println(e.toString());
                        }
                        state = WALLPAPER_NIGHT;
                    }
                }
                else {
                    if(state != WALLPAPER_DAY) {
                        try {
                            Movie movie = Movie.decodeStream(
                                    getResources().getAssets().open(filenameslist[0])
                            );
                            this.movie = movie;
                        } catch (IOException e) {
                            System.out.println(e.toString());
                        }
                        state = WALLPAPER_DAY;
                    }
                }
            }
            else {
                if(state != WALLPAPER_BATTERY) {
                    try {
                        Movie movie = Movie.decodeStream(
                                getResources().getAssets().open(filenameslist[2])
                        );
                        this.movie = movie;
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                    state = WALLPAPER_BATTERY;
                }
            }

        }

        private void draw() {
            if(visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.save();

                ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int batScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level / (float) batScale;
                calendar = new GregorianCalendar();
                hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
                updateCanvas(batteryPct, hour, canvas);


                if(!scaled) {
                    setCanvasSize(canvas);
                    scaled = true;
                } else {
                    canvas.scale(scale.first, scale.second);
                }
                movie.draw(canvas,0,movie.height()/2);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                if(movie.duration() != 0)
                    movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
                else
                    movie.setTime(0);

                handler.removeCallbacks(drawRunner);
                handler.postDelayed(drawRunner, frameDuration);
            }
        }
    }
}
