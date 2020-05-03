package com.example.newmusicplayer.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.activity.MainActivity;
import com.example.newmusicplayer.utils.LogUtil;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MainActivity activity;
    public MyBroadcastReceiver(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type=intent.getStringExtra("type");
        if(type.equals("condition")){
            activity.musicViewModel.isPlaying.setValue(intent.getIntExtra("isPlaying",0));
            activity.musicViewModel.isRandom.setValue(intent.getBooleanExtra("isRandom",false));
            activity.musicViewModel.musicId.setValue(intent.getIntExtra("musicId",0));
            activity.musicViewModel.duration.set(intent.getIntExtra("duration",0));
            activity.musicViewModel.currentPosition.setValue(intent.getIntExtra("currentPosition",0));

            activity.musicViewModel.name.set(intent.getStringExtra("name").equals("")?"享你所享":intent.getStringExtra("name"));
            activity.musicViewModel.artistName.set(intent.getStringExtra("artistName").equals("")?"适当休息一下哟":intent.getStringExtra("artistName"));
            if(!intent.getStringExtra("picUrl").equals(activity.musicViewModel.picUrl.getValue())){
                activity.musicViewModel.picUrl.setValue(intent.getStringExtra("picUrl"));
            }



        }
        if(type.equals("action")){
            if(intent.getStringExtra("action").equals("next")){
                activity.musicViewModel.next();
            }
            if(intent.getStringExtra("action").equals("previous")){
                activity.musicViewModel.previous();
            }
            if(intent.getStringExtra("action").equals("control")){
                activity.musicViewModel.control();
            }
        }
    }
}
