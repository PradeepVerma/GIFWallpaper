package com.vns.gifwallpaper;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;


import java.io.IOException;

/**
 * Created by AndroidPc_1 on 12/25/2015.
 */
public class GIFWallpaperService extends WallpaperService {

   // private final Handler mHandler = new Handler();
    @Override
    public WallpaperService.Engine onCreateEngine() {

        try {
            Movie movie = Movie.decodeStream(getResources().getAssets().open("girl.gif"));

            return new GIFWallpaperEngine(movie);
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

   private class GIFWallpaperEngine extends WallpaperService.Engine{

        private final int frameDuration=20;
        private SurfaceHolder holder;
        private Movie movie;
        private Handler handler;
        private boolean visible;

       public GIFWallpaperEngine(Movie movie){
            this.movie =movie;
           handler = new Handler();
        }
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }
       private Runnable drawGIF = new Runnable() {
           public void run() {
               draw();
           }
       };


       private void draw(){
           if(visible){
               Canvas canvas = holder.lockCanvas();
               canvas.save();
               // Adjust size and position so that
               // the image looks good on your screen
               canvas.scale(5f,5f);
               movie.draw(canvas,-100,0);
               canvas.restore();
               holder.unlockCanvasAndPost(canvas);
               movie.setTime((int)(System.currentTimeMillis() % movie.duration()));

               handler.removeCallbacks(drawGIF);
               handler.postDelayed(drawGIF,frameDuration);
           }

       }
       @Override
       public void onVisibilityChanged(boolean visible) {
           super.onVisibilityChanged(visible);
           this.visible=visible;
           if(visible){
               handler.post(drawGIF);
           }else{
               handler.removeCallbacks(drawGIF);
           }
       }
       @Override
       public void onDestroy() {
           super.onDestroy();
           handler.removeCallbacks(drawGIF);
       }

   }

}
