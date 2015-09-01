package com.example.avaron.test02;

import android.app.Service;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.os.Handler;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.Intent;
import android.os.IBinder;
import android.os.BatteryManager;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import java.io.IOException;
import java.util.GregorianCalendar;

public class Test03WallpaperService extends WallpaperService {

//    @Override
//    public Engine onCreateEngine() {
//        return new Test02WallpaperServiceEngine();
//    }

    public Engine onCreateEngine() {
        try {
            Movie movie = Movie.decodeStream(
                    getResources().getAssets().open("iwdrm-01.gif")
            );
            System.out.println("si he podio");
            return new Test03WallpaperServiceEngine(movie);
        } catch(IOException e) {
            System.out.println("no he podio");
            return null;
        }
    }

    private class Test03WallpaperServiceEngine extends Engine {
        private final int frameDuration = 20;
        private SurfaceHolder holder;
        private Movie movie;

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
        IntentFilter ifilter;
        Intent batteryStatus;
        GregorianCalendar calendar;
        int hour;

        public Test03WallpaperServiceEngine (Movie movie) {
            this.movie = movie;
            handler = new Handler();

//            paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(5);
//            bgColor = Color.parseColor("#C0C0C0");
//            picture = new Picture();

//            bgImage = new ImageView(getApplicationContext());

//            // ((BitmapDrawable)bgImage.getDrawable()).getBitmap().recycle();

//            bgImage.setImageResource(R.drawable.rose);

////            bgImage = getApplicationContext().getResources().getDrawable(R.drawable.van_gogh_sunflowers_1);
//            //System.out.println(bgImage.getIntrinsicHeight()+"/"+bgImage.getIntrinsicWidth()+"="+imgRatio+" o "+bgImage.getIntrinsicHeight()/bgImage.getIntrinsicWidth());
////            clock = new AnalogClock(getApplicationContext());
//            handler.post(drawRunner);
            orientation = getResources().getConfiguration().orientation;

            imgBounds = new Rect();
            System.out.println("parece que he podio");
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            try {
                super.onCreate(surfaceHolder);
                this.holder = surfaceHolder;
            } catch (Exception e) {
                System.out.println("no he podio generar surfaceholder");
            }
            System.out.println("si he podio generar surfacehodler");

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
            System.out.println("voy a calcular ratio");
            int width, height;
//            width = canvas.getClipBounds().width();
//            height = canvas.getClipBounds().height();
            imgRatio = (float)movie.height() / (float)movie.width();
            clipRatio = (float)canvas.getClipBounds().height() / (float)canvas.getClipBounds().width();
            System.out.println("img: "+imgRatio+", "+movie.height()+", "+movie.width());
            System.out.println("clip "+clipRatio+", "+canvas.getClipBounds().height()+", "+canvas.getClipBounds().width());
            System.out.println("antes "+canvas.getClipBounds().height()+" "+canvas.getClipBounds().width());
            if(imgRatio > clipRatio) {
                width = (int)(canvas.getClipBounds().height() / imgRatio);
                height = canvas.getClipBounds().height();
                scale = new Pair((float)width / (float)movie.width(), (float)height / (float)movie.height());
                System.out.println("imagen mas alta, "+scale.first+", "+scale.second);
                canvas.scale(scale.first, scale.second);
            } else {
                height = (int)(canvas.getClipBounds().width() * imgRatio);
                width = canvas.getClipBounds().width();
                scale = new Pair((float)width / (float)movie.width(), (float)height / (float)movie.height());
                System.out.println("imagen mas ancha, "+scale.first+", "+scale.second);
                canvas.scale(scale.first, scale.second);
                System.out.println("altura: " + canvas.getHeight() + ", anchura: " + canvas.getWidth());
            }
            System.out.println("despues "+canvas.getClipBounds().height()+" "+canvas.getClipBounds().width());
            System.out.println("asdasd "+movie.height()+", "+movie.width());
        }

//        private void setImageBounds (Rect canvasClipBounds) {
//            System.out.println("voy a calcular");
//            imgRatio = (float)bgImage.getDrawable().getIntrinsicHeight() / (float)bgImage.getDrawable().getIntrinsicWidth();
//            clipBounds = canvasClipBounds;
//            clipRatio = (float) clipBounds.height() / clipBounds.width();
//            int imgHeight, imgWidth;
//            if(imgRatio > clipRatio) {
//                imgBounds.set(0, clipBounds.top, 0, clipBounds.bottom);
//                imgWidth = (int)(imgBounds.height() / imgRatio);
//                imgBounds.left = (clipBounds.width() - imgWidth) / 2;
//                imgBounds.right =  -(imgBounds.left - clipBounds.width());
//                System.out.println(imgBounds.height()+"/"+imgBounds.width()+"="+imgBounds.height()/imgBounds.height());
//            }
//            else {
//                imgBounds.set(clipBounds.left, 0, clipBounds.right, 0);
//                imgHeight = (int)(imgBounds.width() * imgRatio);
//                //System.out.println(imgBounds.width()+"*"+imgRatio+"="+(int)imgBounds.width()*imgRatio);
//                //System.out.println(imgHeight);
//                imgBounds.top = (clipBounds.height() - imgHeight) / 2;
//                imgBounds.bottom = -(imgBounds.top - clipBounds.height());
//                System.out.println(imgBounds.height()+"/"+imgBounds.width()+"="+imgBounds.height()/imgBounds.height());
//            }
//            bgImage.getDrawable().setBounds(imgBounds);
//        }

//        private void updateCanvas(float batteryPct, int hour, Canvas canvas) {
//            PorterDuff.Mode mode;
//            mode = PorterDuff.Mode.SCREEN;
//            if(batteryPct > 0.2f) {
//                if(hour > 12) {
//                    bgImage.setImageResource(R.drawable.rose);
//                    setImageBounds(canvas.getClipBounds());
//                    bgImage.setColorFilter(Color.GRAY, mode);
//                }
//                else {
//                    bgImage.setColorFilter(Color.BLUE, mode);
//                }
//                //bgImage.setColorFilter(Color.GREEN, mode);
//            }
//            else {
//                bgImage.setColorFilter(Color.GRAY, mode);
//            }
//
//        }

        private void draw() {
            if(visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.save();

                if (getResources().getConfiguration().orientation != orientation) {
                    setCanvasSize(canvas);
                    orientation = getResources().getConfiguration().orientation;
                }

                if(!scaled) {
                    setCanvasSize(canvas);
                    scaled = true;
                } else {
                    canvas.scale(scale.first, scale.second);
                }
//                if(imgBounds.isEmpty())
//                    setCanvasSize(canvas);
//                    setImageBounds(canvas.getClipBounds());
                movie.draw(canvas,0,0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawRunner);
                handler.postDelayed(drawRunner, frameDuration);
            }

//            SurfaceHolder holder = getSurfaceHolder();
//            Canvas canvas = null;
//            try {
//                canvas = holder.lockCanvas();
//                if (canvas != null) {
//                    if(imgBounds.isEmpty())
//                        setImageBounds(canvas.getClipBounds());
//                    draw(canvas);
//                }
//            } finally {
//                if (canvas != null)
//                    holder.unlockCanvasAndPost(canvas);
//            }
//
//            handler.removeCallbacks(drawRunner);
//
//            if (visible) {
//                handler.postDelayed(drawRunner, 200);
//            }
        }

//        private void draw(Canvas canvas) {
//            //canvas.drawColor(bgColor);
//            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//            batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
//            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//            if (getResources().getConfiguration().orientation != orientation) {
//                setImageBounds(canvas.getClipBounds());
//                orientation = getResources().getConfiguration().orientation;
//            }
//
//
//            float batteryPct = level / (float) scale;
//            calendar = new GregorianCalendar();
//            hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
//            updateCanvas(batteryPct, hour, canvas);
//
//            //canvas.drawColor(bgColor);
//            bgImage.draw(canvas);
//
//            //canvas.drawPicture(picture);
//        }

        /**
         *         public CustomView(Context context, AttributeSet attrs) {
         super(context, attrs);
         mCustomImage = context.getResources().getDrawable(R.drawable.my_image);
         }

         * protected void onDraw(Canvas canvas) {
            Rect imageBounds = canvas.getClipBounds();  // Adjust this for where you want it

            mCustomImage.setBounds(canvas.getClipBounds());
            mCustomImage.draw(canvas);
        }*/
    }
}
