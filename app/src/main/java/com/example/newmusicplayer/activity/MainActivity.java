package com.example.newmusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.newmusicplayer.MusicViewModel;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.animation.AnimationUtils;
import com.example.newmusicplayer.broadcast.MyBroadcastReceiver;
import com.example.newmusicplayer.databinding.ActivityMainBinding;
import com.example.newmusicplayer.service.MusicService;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tabLayout;
    private NotificationManager mManager;
    private NotificationCompat.Builder builder;
    private RemoteViews mRemoteViews;
    private ActivityMainBinding activityMainBinding;
    public MusicViewModel musicViewModel;
    private IntentFilter mIntentFilter;
    private MusicService service;
    private Boolean isBind = false;
    private MyBroadcastReceiver myBroadcastReceiver;
    private NavController controller;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.activity_main,null,false);
        setContentView(activityMainBinding.getRoot());
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);
        activityMainBinding.setViewModel(musicViewModel);
        activityMainBinding.setLifecycleOwner(this);//重要

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.control).setOnClickListener(this);


        controller = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);
        ImageView musicDetail = findViewById(R.id.musicDetail);
        musicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.fragmentMusicDetails);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        for(int i = 0; i < 3; i ++){
            tabLayout.getTabAt(i).setCustomView(makeTabView(i));
        }
        //Navigation.createNavigateOnClickListener(R.id.action_fragmentLogin_to_fragmentUserDetails)
        setUpTabViewOnClick(tabLayout);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.fragmentSearch);
            }
        });




        mManager = (NotificationManager) getBaseContext().getSystemService(getBaseContext().NOTIFICATION_SERVICE);

        createNotificationChannel();
        refreshNotification();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.example.action");
        mIntentFilter.addAction("com.example.condition");
        mIntentFilter.setPriority(100);
        myBroadcastReceiver = new MyBroadcastReceiver(MainActivity.this);
        registerReceiver(myBroadcastReceiver, mIntentFilter);

        //绑定服务
        Intent intent = new Intent(this, MusicService.class);

        bindService(intent, conn, BIND_AUTO_CREATE);


        musicViewModel.musicIdList.observe(this, new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                musicViewModel.startWithMusicIdList(integers);
            }
        });

        musicViewModel.currentPosition.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                musicViewModel.process.set((int)(((float)musicViewModel.currentPosition.getValue()/musicViewModel.duration.get())*1000));

            }
        });

        musicViewModel.isPlaying.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer.intValue() != 2){
                    musicViewModel.playOrPausePicRes.set(R.drawable.play);
                }else {
                    musicViewModel.playOrPausePicRes.set(R.drawable.pause);
                }
            }
        });

        musicViewModel.isRandom.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    musicViewModel.ifRandomPicRes.set(R.drawable.random);
                }else {
                    musicViewModel.ifRandomPicRes.set(R.drawable.repeat);
                }
            }
        });

        musicViewModel.picUrl.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ImageLoader.with(MainActivity.this).load(s).into(musicViewModel.picture);
            }
        });


        new Timer().schedule(timerTask, 1000,200);



    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            MusicService.MyBinder myBinder = (MusicService.MyBinder) binder;
            service = myBinder.getService();
            musicViewModel.musicService = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(MainActivity.this, R.id.fragment);
        return controller.navigateUp();
    }

    private void setUpTabViewOnClick(TabLayout tabLayout){
        tabLayout.getTabAt(2).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    controller.navigate(R.id.fragmentUserDetails);
                }catch (Exception ignored){

                }
            }
        });
        tabLayout.getTabAt(0).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    controller.navigate(R.id.fragmentMain);
                }catch (Exception ignored){

                }
            }
        });
        tabLayout.getTabAt(1).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    controller.navigate(R.id.fragmentLibrary);
                }catch (Exception ignored){

                }
            }
        });

    }

    private View makeTabView(int position){
        int[] pics = new int[3];
        pics[0] = R.drawable.flame;
        pics[1] = R.drawable.library;
        pics[2] = R.drawable.account;
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_text_icon,null);
        ImageView imageView = tabView.findViewById(R.id.imageView);
        imageView.setImageResource(pics[position]);

        return tabView;
    }

    @Override
    public void onClick(View v) {
        AnimationUtils.buttonClickAnimation(v);
    }


    public void refreshNotification(){
        showNotificationIcon(MainActivity.this);
    }

    public void showNotificationIcon(Context context) {
        //mManager.cancelAll();
        builder = new NotificationCompat.Builder(context,"0");
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        if (musicViewModel.isPlaying.getValue() == 2) {
            mRemoteViews.setImageViewResource(R.id.imageViewControl,R.drawable.pause);
        }else {
            mRemoteViews.setImageViewResource(R.id.imageViewControl,R.drawable.play);
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        builder.setAutoCancel(false);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("音乐控制");
        builder.setContentTitle("音乐控制");
        builder.setOngoing(true);

        Intent buttonIntent =new Intent("com.example.action");
        buttonIntent.putExtra("action", "control");
        buttonIntent.putExtra("type", "action");
        PendingIntent intent_btn=PendingIntent.getBroadcast(context,1,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.imageViewControl,intent_btn);

        Intent buttonIntent2 =new Intent("com.example.action");
        buttonIntent2.putExtra("action", "previous");
        buttonIntent2.putExtra("type", "action");
        PendingIntent intent_btn2=PendingIntent.getBroadcast(context,2,buttonIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.imageViewPrevious,intent_btn2);

        Intent buttonIntent3 =new Intent("com.example.action");
        buttonIntent3.putExtra("action", "next");
        buttonIntent3.putExtra("type", "action");
        PendingIntent intent_btn3=PendingIntent.getBroadcast(context,3,buttonIntent3,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.imageViewNext,intent_btn3);
        mRemoteViews.setProgressBar(R.id.progressBar, 1000, (int) (1000 * ((double) musicViewModel.currentPosition.getValue() / musicViewModel.duration.get())), false);
        mRemoteViews.setTextViewText(R.id.title,musicViewModel.name.get());
        mRemoteViews.setTextViewText(R.id.artist,musicViewModel.artistName.get());
        mRemoteViews.setImageViewBitmap(R.id.pic, musicViewModel.picture.get());


        builder.setContent(mRemoteViews);
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);

        mManager.notify(0, builder.build());
    }


    private void createNotificationChannel() {
        String channelName = "MyChannel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel("0", channelName, importance);
            mManager.createNotificationChannel(notificationChannel);
        }
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            refreshNotification();
        }
    };

}
