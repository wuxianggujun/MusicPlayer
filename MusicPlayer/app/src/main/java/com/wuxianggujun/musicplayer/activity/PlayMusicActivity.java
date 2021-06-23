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
/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class PlayMusicActivity extends AppCompatActivity {

    private ImageView iv_play,iv_last,iv_next;

    private MyItemOnclick itemOnclickListener;



    //绑定service活动
    private Intent mServiceIntent;
    private MusicService mMusicService; 
    private MusicServiceConnection mMusicServiceConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
        iv_play = findViewById(R.id.song_lrc_bottom_play);
        iv_play.setOnClickListener(itemOnclickListener);
    }

    private void init() {
        itemOnclickListener = new MyItemOnclick();
        //初始化绑定音乐服务
        mServiceIntent = new Intent(PlayMusicActivity.this, MusicService.class);
        mMusicServiceConnection = new MusicServiceConnection();

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
    }









    private class MyItemOnclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.song_lrc_bottom_play:
                    //播放音乐按钮
                    mMusicService.mMusicServiceControl.play();
                    Toast.makeText(getApplication(), "点击了播放", Toast.LENGTH_SHORT).show();
                    break;

            }

        }


    }




    private class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.d("tag", "onServiceConnected");          
            MusicService.MusicServiceControl  myBinder = (MusicService.MusicServiceControl) iBinder;
            mMusicService = myBinder.getMusicService();

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
