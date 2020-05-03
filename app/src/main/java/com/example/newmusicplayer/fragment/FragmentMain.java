package com.example.newmusicplayer.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.MusicViewModel;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.adapter.RecyclerViewAdapterAlbumMain;
import com.example.newmusicplayer.bean.AlbumNewestBean;
import com.example.newmusicplayer.bean.AlbumRecommendBean;
import com.example.newmusicplayer.databinding.FragmentMainBinding;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonArray;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends Fragment {

    private static final String ALBUM_LIST_NEWEST_URL = "http://47.99.165.194/album/newest";
    private static final String ALBUM_LIST_RECOMMEND_URL = "http://47.99.165.194/top/playlist";

    private MusicViewModel musicViewModel;
    private FragmentMainBinding fragmentMainBinding;
    private int finishCount;
    private RecyclerViewAdapterAlbumMain adapter;
    private RecyclerView recyclerView;
    private ArrayList<AlbumNewestBean> albumNewestBeans;
    private ArrayList<AlbumRecommendBean> albumRecommendBeans;

    public FragmentMain() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMainBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_main,null,false);
        musicViewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        fragmentMainBinding.setViewModel(musicViewModel);
        fragmentMainBinding.setLifecycleOwner(this);
        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        recyclerView = getView().findViewById(R.id.recyclerView);

        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finishCount = 0;
                HttpUtil.getInstance().httpGet(ALBUM_LIST_NEWEST_URL, new Callback() {
                    @Override
                    public void callback(String respond) {
                        try{
                            JSonObject jSonObject = new JSonObject(respond);
                            JSonArray jSonArray = new JSonArray(jSonObject.getString("albums"));
                            albumNewestBeans = new ArrayList<>();
                            for (int i = 0; i < jSonArray.size(); i ++){
                                albumNewestBeans.add(JSonEval.getInstance().fromJson(jSonArray.get(i), AlbumNewestBean.class));
                            }


                        }catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        musicViewModel.albumNewestList.setValue(albumNewestBeans);
                                        finishCount ++;
                                        if (finishCount == 2){
                                            swipeRefreshLayout.setRefreshing(false);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void callback() { }
                },getContext());

                HttpUtil.getInstance().httpGet(ALBUM_LIST_RECOMMEND_URL, new Callback() {
                    @Override
                    public void callback(String respond) {
                        try{
//                            LogUtil.e(respond);
//                            Log.e("sandyzhang","size:"+respond.length());
                            JSonObject jSonObject = new JSonObject(respond);

                            JSonArray jSonArray = new JSonArray(jSonObject.getString("playlists"));

                            albumRecommendBeans = new ArrayList<>();
                            for (int i = 0; i < jSonArray.size(); i ++){
                                albumRecommendBeans.add(JSonEval.getInstance().fromJson(jSonArray.get(i), AlbumRecommendBean.class));
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        musicViewModel.albumRecommendList.setValue(albumRecommendBeans);
                                        finishCount ++;
                                        if (finishCount == 2){
                                            swipeRefreshLayout.setRefreshing(false);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void callback() { }
                },getContext(),"limit","40");
            }
        };

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setRefreshing(true);
        onRefreshListener.onRefresh();


        adapter = new RecyclerViewAdapterAlbumMain(new ArrayList<AlbumRecommendBean>(), new ArrayList<AlbumNewestBean>(), getContext(),swipeRefreshLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        musicViewModel.albumRecommendList.observe(getActivity(), new Observer<ArrayList<AlbumRecommendBean>>() {
            @Override
            public void onChanged(ArrayList<AlbumRecommendBean> albumRecommendBeans) {
                adapter.changeRecommendData(albumRecommendBeans);
            }
        });


        musicViewModel.albumNewestList.observe(getActivity(), new Observer<ArrayList<AlbumNewestBean>>() {
            @Override
            public void onChanged(ArrayList<AlbumNewestBean> albumNewestBeans) {
                adapter.changeNewestData(albumNewestBeans);
            }
        });





    }


}
