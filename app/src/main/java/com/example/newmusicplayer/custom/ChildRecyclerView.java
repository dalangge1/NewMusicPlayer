package com.example.newmusicplayer.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * package:custom用于存自定义view
 * 用于解决滑动冲突（嵌套recyclerview时）
 */
public class ChildRecyclerView extends RecyclerView {
    private float startY;
    private boolean tmp = true;//是否拦截

    public ChildRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //swipeRefreshLayout和多个recyclerView嵌套，滑动冲突使得滑动失灵搞了好久，终于成功。

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //父层ViewGroup不要拦截点击事件
        //Log.e("sandyzhang", canScrollVertically(1) + "");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();//按下时保存起点，用于判断是上划还是下滑。
                tmp = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //对于上划和下滑有两种方案
                if(startY > ev.getY()+60){//数字是判断的灵敏度
                    //上划则判断最外层recyclerview是否能往下，如果能，则通知最外层recyclerview拦截事件，先让最外层往上直到不能滑为止。
                    Log.e("sandyzhang", "上划");
                    if(((ParentRecyclerView)getParent().getParent()).canScrollVertically(1)){
                        tmp = false;
                    }else {
                        tmp = true;
                    }
                }else if(startY < ev.getY()-60){
                    //下划则判断里层recyclerview是否能往上，如果不能再往上了，则才通知最外层recyclerview拦截事件，才让最外层往下滑直到不能滑为止。
                    Log.e("sandyzhang", "下划");
                    if(canScrollVertically(-1)){
                        tmp = true;
                    }else {
                        tmp = false;
                    }
                }
        }
        //Log.e("sandyzhang", "是否拦截："+tmp);
        getParent().getParent().requestDisallowInterceptTouchEvent(tmp);
        return super.dispatchTouchEvent(ev);
    }

    //获得RecyclerView滑动的位置 direction为正数为是否能下滑，否则为上划

    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }
}
