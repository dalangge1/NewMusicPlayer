package com.example.newmusicplayer.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {
    public static void buttonClickAnimation(View v){
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0.8f);
            animator.setDuration(150);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(1);
            animator.setRepeatMode(ValueAnimator.REVERSE);

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(v, "scaleY", 1f, 0.8f);
            animator2.setDuration(150);
            animator2.setInterpolator(new LinearInterpolator());
            animator2.setRepeatCount(1);
            animator2.setRepeatMode(ValueAnimator.REVERSE);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animator).with(animator2);
            animatorSet.start();
    }
}
