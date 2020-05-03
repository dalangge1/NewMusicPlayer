package com.example.newmusicplayer.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.animation.AnimationUtils;
import com.example.newmusicplayer.bean.User;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserDetails extends Fragment implements View.OnClickListener{

    private static final String USER_DETAILS_URL = "http://47.99.165.194/user/detail";

    private TextView city;
    private TextView nickname;
    private User user;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView posts;
    private TextView following;
    private TextView followers;
    private ImageView background;

    public FragmentUserDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nickname = getView().findViewById(R.id.nickname);
        city = getView().findViewById(R.id.city);
        followers = getView().findViewById(R.id.followers);
        following = getView().findViewById(R.id.following);
        posts = getView().findViewById(R.id.posts);
        background = getView().findViewById(R.id.background);
        getView().findViewById(R.id.imageButton1).setOnClickListener(this);
        getView().findViewById(R.id.imageButton2).setOnClickListener(this);
        getView().findViewById(R.id.imageButton3).setOnClickListener(this);

        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);

        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpUtil.getInstance().httpGet(USER_DETAILS_URL, new Callback() {

                    @Override
                    public void callback(String respond) {
                        try{
                            JSonObject jSonObject = new JSonObject(respond);
                            user = JSonEval.getInstance().fromJson(jSonObject.getString("profile"), User.class);

                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nickname.setText(user.getNickname());
                                    city.setText(user.getCity());
                                    posts.setText(String.valueOf(user.getEventCount()));
                                    followers.setText(String.valueOf(user.getFolloweds()));
                                    following.setText(String.valueOf(user.getFollows()));
                                    ImageLoader.with(getContext()).load(user.getBackgroundUrl()).into(background);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void callback() {

                    }
                },getContext(),"uid","318082831");
            }
        };

        swipeRefreshLayout.setOnRefreshListener(listener);

        swipeRefreshLayout.setRefreshing(true);
        listener.onRefresh();

    }

    @Override
    public void onClick(View v) {
        AnimationUtils.buttonClickAnimation(v);
    }
}
