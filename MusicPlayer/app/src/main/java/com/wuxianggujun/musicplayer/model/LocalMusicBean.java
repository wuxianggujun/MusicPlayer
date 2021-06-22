package com.wuxianggujun.musicplayer.model;

/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class LocalMusicBean {
    
    private String id;//歌曲id
    private String song;//歌曲名称
    private String singer;//歌手名字
    private String album;//专辑名字
    private String duration;//持续时间
    private String path;//歌曲路径

    public LocalMusicBean(){
        
    }
    
    public LocalMusicBean(String id, String song, String singer, String album, String duration, String path) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }
    
    
    
    


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSong() {
        return song;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum() {
        return album;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    
    
    
    
    }
