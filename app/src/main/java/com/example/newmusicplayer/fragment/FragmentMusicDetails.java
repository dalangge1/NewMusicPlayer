package com.example.newmusicplayer.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.example.newmusicplayer.MusicViewModel;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.custom.MyImageView;
import com.example.newmusicplayer.databinding.FragmentMusicDetailsBinding;
import com.example.newmusicplayer.utils.LogUtil;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMusicDetails extends Fragment {

    private MusicViewModel musicViewModel;
    private FragmentMusicDetailsBinding fragmentMusicDetailsBinding;
    private int process = 0;

    public FragmentMusicDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMusicDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_music_details,null,false);
        musicViewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        fragmentMusicDetailsBinding.setViewModel(musicViewModel);
        fragmentMusicDetailsBinding.setLifecycleOwner(this);
        return fragmentMusicDetailsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MyImageView picture = getView().findViewById(R.id.picture);

        new Thread() {
            @Override
            public void run() {
                super.run();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(200);
                            picture.setImageBitmap(musicViewModel.picture.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
        final ObjectAnimator animator = ObjectAnimator.ofFloat(picture, "rotation", 0f, 360.0f);
        animator.setDuration(20000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
        if(musicViewModel.isPlaying.getValue() != 2){
            animator.pause();
        }

        musicViewModel.isPlaying.observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer.intValue()==2){
                    animator.resume();
                }else {
                    animator.pause();
                }
            }
        });


        SeekBar seekBar = getView().findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                process = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicViewModel.seekTo((int) (((float)process/1000)*musicViewModel.duration.get()));

            }
        });
    }
}
