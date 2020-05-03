package com.example.newmusicplayer;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newmusicplayer.R;
import com.example.newmusicplayer.bean.AlbumNewestBean;
import com.example.newmusicplayer.bean.AlbumRecommendBean;
import com.example.newmusicplayer.service.MusicService;
import com.example.newmusicplayer.utils.LogUtil;

import java.util.ArrayList;
import java.util.Observable;

public class MusicViewModel extends ViewModel {
    public Context context;

    public MutableLiveData<ArrayList<AlbumRecommendBean>> albumRecommendList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<AlbumNewestBean>> albumNewestList = new MutableLiveData<>();

    public MutableLiveData<Integer> isPlaying = new MutableLiveData<>();
    public ObservableField<Integer> duration = new ObservableField<>();
    public MutableLiveData<Boolean> isSeeking = new MutableLiveData<>();
    public MutableLiveData<Integer> musicId = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Integer>> musicIdList = new MutableLiveData<>();
    public MutableLiveData<Integer> currentPosition = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRandom = new MutableLiveData<>();
    public ObservableField<Integer> process = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> artistName = new ObservableField<>();
    public MutableLiveData<String> picUrl = new MutableLiveData<>();
    public ObservableField<Integer> playOrPausePicRes = new ObservableField<>();
    public ObservableField<Integer> ifRandomPicRes = new ObservableField<>();
    public ObservableField<Bitmap> picture = new ObservableField<>();

    public int pos = 0;
    public MusicViewModel(){
        albumRecommendList.setValue(new ArrayList<AlbumRecommendBean>());
        albumNewestList.setValue(new ArrayList<AlbumNewestBean>());
        musicIdList.setValue(new ArrayList<Integer>());
        isPlaying.setValue(0);
        currentPosition.setValue(0);
        duration.set(0);
        isRandom.setValue(false);
        isSeeking.setValue(false);
        musicId.setValue(0);
        name.set("享你所想");
        artistName.set("适当休息一下哟");
        picUrl.setValue("");
        playOrPausePicRes.set(R.drawable.pause);
        ifRandomPicRes.set(R.drawable.repeat);
        picture.set(null);
    }





    public MusicService musicService;


    public void next(){
        musicService.next();
    }

    public void previous(){
        musicService.previous();
    }

    public void control(){
        musicService.control();
    }

    public void seekTo(int p){
        musicService.seekTo(p);
    }

    public void modeRandom(){
        musicService.modeRandom(!isRandom.getValue());
    }

    public void startWithMusicIdList(ArrayList<Integer> musicIdList){
        if(musicService!=null){
            musicService.startWithMusicIdList(musicIdList, pos);
        }
    }



}
