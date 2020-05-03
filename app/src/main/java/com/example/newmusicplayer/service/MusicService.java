package com.example.newmusicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.bean.Album;
import com.example.newmusicplayer.bean.Artist;
import com.example.newmusicplayer.bean.SongDetail;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonArray;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;
import com.example.newmusicplayer.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private static final String SONG_URL = "http://47.99.165.194/song/url";
    private static final String SONG_DETAIL_URL = "http://47.99.165.194/song/detail";

    private MediaPlayer mediaPlayer;
    private MyBinder myBinder = new MyBinder();
    private ArrayList<Integer> musicIdList = new ArrayList<>();
    private boolean isRandom = false;
    private int position = 0;
    private int isPlaying = 0;
    private SongDetail songDetail = new SongDetail();

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void sendBroadcastCondition(){
        Intent intent = new Intent("com.example.condition");
        intent.putExtra("type","condition");


        intent.putExtra("isPlaying",isPlaying);
        intent.putExtra("isRandom",isRandom);
        intent.putExtra("musicId",musicIdList.size() == 0 ? 0 : musicIdList.get(position));
        intent.putExtra("duration",mediaPlayer.getDuration());
        intent.putExtra("currentPosition",mediaPlayer.getCurrentPosition());
        intent.putExtra("name",songDetail.getName());
        intent.putExtra("artistName",songDetail.getAr().getName());
        intent.putExtra("picUrl",songDetail.getAl().getPicUrl());
        sendBroadcast(intent);
    }



    public void control(){
        if(isPlaying == 1){
            mediaPlayer.start();
            isPlaying = 2;
        }else if(isPlaying == 2){
            mediaPlayer.pause();
            isPlaying = 1;
        }
    }

    public void modeRandom(boolean b){
        isRandom = b;
    }

    public void seekTo(int p){
        mediaPlayer.seekTo(p);
    }

    public void prepare(String uri){
        isPlaying = 0;
        try{
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepareAsync();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void prepare(int musicId){
        isPlaying = 0;
        HttpUtil.getInstance().httpGet(SONG_URL, new Callback() {
            @Override
            public void callback(String respond) {
                try{
                    JSonObject jSonObject = new JSonObject(new JSonArray(new JSonObject(respond).getString("data")).get(0));
                    if(jSonObject.getString("url") != null){
                        Log.d("获取到歌曲url：", jSonObject.getString("url"));
                        prepare(jSonObject.getString("url"));
                    }else {
                        Toast.makeText(getApplicationContext(),"获取到歌曲url为空",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"获取音乐链接失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void callback() {

            }
        },getApplicationContext(),"id",String.valueOf(musicId),"br","320000");
        HttpUtil.getInstance().httpGet(SONG_DETAIL_URL, new Callback() {
            @Override
            public void callback(String respond) {
                try{
                    JSonObject jSonObject = new JSonObject(new JSonArray(new JSonObject(respond).getString("songs")).get(0));
                    songDetail.setName(jSonObject.getString("name"));
                    songDetail.setAr(JSonEval.getInstance().fromJson(new JSonArray(jSonObject.getString("ar")).get(0), Artist.class));
                    songDetail.setAl(JSonEval.getInstance().fromJson(jSonObject.getString("al"), Album.class));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"获取音乐信息失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void callback() {

            }
        },getApplicationContext(),"ids",String.valueOf(musicId));
    }

    public void randomNext(){
        mediaPlayer.pause();
        isPlaying = 1;
        if (musicIdList.size() == 0){
            return;
        }
        Random random = new Random();
        prepare(musicIdList.get(position = random.nextInt(musicIdList.size())));
    }

    public void previous(){
        mediaPlayer.pause();
        isPlaying = 1;
        if (musicIdList.size() == 0){
            return;
        }
        if(!isRandom){
            position --;
            if(position == -1){
                position = musicIdList.size() - 1;
            }
            position %= musicIdList.size();
            prepare(musicIdList.get((position)));
        }else {
            randomNext();
        }
    }

    public void next(){
        mediaPlayer.pause();
        isPlaying = 1;
        if (musicIdList.size() == 0){
            return;
        }
        if(!isRandom){
            position ++;
            position %= musicIdList.size();
            prepare(musicIdList.get((position)));
        }else {
            randomNext();
        }
    }

    public void startWithMusicIdList(ArrayList<Integer> musicIdList ,int position){
        mediaPlayer.seekTo(0);
        this.musicIdList = musicIdList;
        this.position = position;
        prepare(musicIdList.get(position));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPlaying = 1;
                control();
            }
        });
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isPlaying == 2 && mediaPlayer.getDuration() != 0 && mediaPlayer.getDuration() <= mediaPlayer.getCurrentPosition()){
                    next();
                }
                sendBroadcastCondition();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,1000,200);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }




}
