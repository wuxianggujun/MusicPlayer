package com.wuxianggujun.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import com.wuxianggujun.musicplayer.service.MusicService;
import android.util.Log;
import com.wuxianggujun.musicplayer.R;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class PlayMusicActivity extends AppCompatActivity {

    private ImageView iv_play,iv_last,iv_next;
    private static SeekBar sb_songProgress;//进度条
    private static TextView total_tv,current_tv;
    
    
    private MyItemOnclick itemOnclickListener;
    private SongProgressListener mSongProgressListener;
    

    //绑定service活动
    private Intent mServiceIntent;
    private MusicService mMusicService; 
    private MusicService.MusicServiceControl musicControl;
    private MusicServiceConnection mMusicServiceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
        iv_play = findViewById(R.id.song_lrc_bottom_play);
        current_tv = findViewById(R.id.song_lrc_time_start);
        total_tv = findViewById(R.id.song_lrc_time_end);
        iv_play.setOnClickListener(itemOnclickListener);
        sb_songProgress = findViewById(R.id.song_lrc_bar);
        sb_songProgress.setOnSeekBarChangeListener(mSongProgressListener);
        
    }

    private void init() {
        itemOnclickListener = new MyItemOnclick();
        //初始化绑定音乐服务
        mServiceIntent = new Intent(PlayMusicActivity.this, MusicService.class);
        mMusicServiceConnection = new MusicServiceConnection();
        mSongProgressListener  =new SongProgressListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.bindService(mServiceIntent, mMusicServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //接触绑定
        this.unbindService(mMusicServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMusicServiceConnection != null) mMusicServiceConnection = null;
        if (mMusicService != null) mMusicService = null;
        if (mServiceIntent != null) mServiceIntent = null;
        if (itemOnclickListener != null)itemOnclickListener = null;
        if(mSongProgressListener!=null)mSongProgressListener=null;
        
    }



    public static Handler handler = new Handler(Looper.myLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //获取从子线程发送过来的数据
            Bundle bundle = msg.getData();
            //歌曲总时长
            int duration = bundle.getInt("duration");
            //歌曲当前进度
            int currentPosition = bundle.getInt("currentPosition");
            //给进度条设置当前音乐进度  
            sb_songProgress.setMax(duration);
            sb_songProgress.setProgress(currentPosition);
            //setmax(duration);
            //setprogress(currentPosition);
            String totalTime = msToMinSec(duration);
            String currentTime = msToMinSec(currentPosition);
            total_tv.setText(totalTime);
            current_tv.setText(currentTime);

        }      
    };

    private static String msToMinSec(int ms) {
        int sec = ms / 1000;
        int min = sec / 60;
        sec -= min * 60;
        return String.format("%02d:%02d", min, sec);
    }






    private class MyItemOnclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.song_lrc_bottom_play:
                    if(!musicControl.isPlaying()){
                        //播放音乐按钮
                        musicControl.play();
                        iv_play.setImageResource(R.drawable.iv_pause);                       
                    }else if (musicControl.isPlaying()){
                        musicControl.pause();
                        iv_play.setImageResource(R.drawable.iv_play);
                        
                      }else{
                        musicControl.resume(); 
                        iv_play.setImageResource(R.drawable.iv_pause);
                    }
                    
                    Toast.makeText(getApplication(), "点击了播放", Toast.LENGTH_SHORT).show();
                    break;

            }

        }


    }

    private class SongProgressListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUse) {
            if(progress == seekbar.getMax()){
                //停止播放动画
            }
            if(fromUse){
                //判断是不是用户操纵
               musicControl.seekTo(progress);         
            }
            
                       
        }

        @Override
        public void onStartTrackingTouch(SeekBar p1) {
            musicControl.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar p1) {
            musicControl.resume();
        }
        
    };


    private class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.d("tag", "onServiceConnected");          
            musicControl = (MusicService.MusicServiceControl) iBinder;
            mMusicService = musicControl.getMusicService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("tag", "onServiceDisconnected");
            if (mMusicService != null) {
                mMusicService = null;
            }
        }
    }






}
