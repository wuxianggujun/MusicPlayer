package com.wuxianggujun.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;
import java.util.Timer;
import android.os.Binder;
import android.net.Uri;
import com.wuxianggujun.musicplayer.R;
import java.util.TimerTask;
import android.os.Message;
import com.wuxianggujun.musicplayer.activity.PlayMusicActivity;
import android.os.Bundle;
/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private Timer timer;
    public  MusicServiceControl mMusicServiceControl = new MusicServiceControl();
    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                    }
                });

        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    //onBind，返回IBinder对象，可以与Activity相交互。这是Bind Service的生命周期方法。
    @Override
    public IBinder onBind(Intent p1) {
        return mMusicServiceControl;
    }


    public class MusicServiceControl extends Binder {

        public MusicService getMusicService() {
            return MusicService.this;
        }

        public void addTimer() {
            if (timer == null) {
                timer = new Timer();
                TimerTask task = new TimerTask(){
                    @Override
                    public void run() {
                        //获取歌曲总时长
                        int duration = mediaPlayer.getDuration();
                        //获取当前播放进度条
                        int currentPos = mediaPlayer.getCurrentPosition();
                        Message message = PlayMusicActivity.handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentPosition", currentPos);
                        message.setData(bundle);
                        PlayMusicActivity.handler.sendMessage(message);
                    }
                };
                //开始计时任务后的5毫秒，后面500毫秒执行一次
                timer.schedule(task,5,500);
            }



        }


        public void play() {
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.asi105du);
            mediaPlayer.start();

        }

        public void pause() {
            mediaPlayer.pause();

        }


        public void resume() {
            mediaPlayer.start();
        }

        public void stop() {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        public void seekTo(int ms) {
            mediaPlayer.seekTo(ms);
        }

    }


}
