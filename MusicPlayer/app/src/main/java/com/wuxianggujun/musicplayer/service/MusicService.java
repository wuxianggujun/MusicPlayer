package com.wuxianggujun.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;

/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        if(mediaPlayer== null){
            mediaPlayer = new MediaPlayer();
            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        
                    }
                });
            
        }
        
        
    }
    
    
    
    //onBind，返回IBinder对象，可以与Activity相交互。这是Bind Service的生命周期方法。
    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }
    
    
    
    
}
