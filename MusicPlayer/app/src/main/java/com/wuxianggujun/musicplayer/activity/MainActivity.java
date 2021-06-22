package com.wuxianggujun.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.wuxianggujun.musicplayer.R;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;
import com.wuxianggujun.musicplayer.model.LocalMusicBean;
import java.util.ArrayList;
import com.wuxianggujun.musicplayer.adapter.LocalMusicAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Toast;
import android.media.MediaPlayer;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView nextIv,playIv,lastIv;
    private TextView singerTv,songTv;
    private RecyclerView musicRv;
    private int currentPlayPosition = -1;
    //记录暂停时音乐进度条变量
    private int currentPausePositionInSong = 0;
    private MediaPlayer mediaPlayer;

    private List<LocalMusicBean> mDatas;//数据源
    private LocalMusicAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		initView();

        mediaPlayer = new MediaPlayer();

        mDatas = new ArrayList<LocalMusicBean>();
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);

        loadLocalMusicData();
        //设置RecyxlerView的点击事件
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener(){


                @Override
                public void OnItemClick(View v, int position) {
                    currentPlayPosition = position;
                    LocalMusicBean bean = mDatas.get(position);
                    playMusicInMusicBean(bean);

                    //Toast.makeText(getApplication(), "点击位置" + position, Toast.LENGTH_SHORT).show();
                }

            });

	}

    public void playMusicInMusicBean(LocalMusicBean musicBean) {

        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        stopMusic();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic() {

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {

                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }

            playIv.setImageResource(R.drawable.ic_play_circle);          
        }

    }

    private void pauseMusic() {
        //暂停音乐的函数
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.drawable.ic_play_circle);

        }


    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.drawable.ic_play_circle_outline);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }





    private void loadLocalMusicData() {
        ContentResolver resolver = getContentResolver();
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Cursor cursor = resolver.query(uri, new String[]{MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ARTIST}, null, null, null);
        Cursor cursor = resolver.query(uri, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return;
        }
        int id = 0;
        while (cursor.moveToNext()) {
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));  
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));        
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));  
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));  
            SimpleDateFormat sdf= new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path);
            mDatas.add(bean);
        }
        cursor.close();
        //提示adapter更新
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);        
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);     
        musicRv = findViewById(R.id.local_music_recyclerview);
        nextIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {            
            case R.id.local_music_bottom_iv_next:

                if (currentPlayPosition == mDatas.size() - 1) {
                    Toast.makeText(this, "已经是最后一首歌!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPlayPosition = currentPlayPosition + 1;
                LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                playMusicInMusicBean(nextBean);

                break;
            case R.id.local_music_bottom_iv_play:
                if (currentPlayPosition == -1) {
                    Toast.makeText(this, "请选择想要播放的音乐!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    playMusic();
                }

                break;                 
            case R.id.local_music_bottom_iv_last:
                if (currentPlayPosition == 0) {
                    Toast.makeText(this, "已经是第一首歌!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPlayPosition = currentPlayPosition - 1;
                LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                playMusicInMusicBean(lastBean);
                break;            
        }

    }





}
