package com.example.newmusicplayer.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.MusicViewModel;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.adapter.RecyclerViewAdapterPlaylist;
import com.example.newmusicplayer.bean.AlbumDetailBean;
import com.example.newmusicplayer.bean.AlbumDetailBeanOri;
import com.example.newmusicplayer.bean.Tracks;
import com.example.newmusicplayer.databinding.FragmentAlbumListBinding;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonArray;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;
import com.example.newmusicplayer.utils.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlbumList extends Fragment {

    private static final String ALBUM_LIST_DETAIL_URL = "http://47.99.165.194/playlist/detail";

    private RecyclerViewAdapterPlaylist adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlbumDetailBeanOri albumOri;
    private AlbumDetailBean album = new AlbumDetailBean();
    private MusicViewModel musicViewModel;

    public FragmentAlbumList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentAlbumListBinding fragmentAlbumListBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_album_list,null,false);
        musicViewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        fragmentAlbumListBinding.setLifecycleOwner(this);
        return fragmentAlbumListBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapterPlaylist(null, getContext(), musicViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);

        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Long id = getArguments().getLong("id");
                if(id == 0){
                    Toast.makeText(getContext(),"无法获取歌单详细",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpUtil.getInstance().httpGet(ALBUM_LIST_DETAIL_URL, new Callback() {
                    @Override
                    public void callback(String respond) {
                        try {
                            JSonObject jSonObject = new JSonObject(respond);
                            albumOri = JSonEval.getInstance().fromJson(jSonObject.getString("playlist"), AlbumDetailBeanOri.class);
                            JSonArray jSonArray = new JSonArray(albumOri.getTracks());
                            Tracks[] tracksList = new Tracks[jSonArray.size()];
                            for (int i = 0; i < jSonArray.size(); i ++){
                                tracksList[i] = JSonEval.getInstance().fromJson(jSonArray.get(i) ,Tracks.class);
                            }
                            album.setCoverImgUrl(albumOri.getCoverImgUrl());
                            album.setCreator(albumOri.getCreator());
                            album.setName(albumOri.getName());
                            album.setTracksList(tracksList);

                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                    adapter.changeData(album);
                                }
                            });
                        }
                    }

                    @Override
                    public void callback() {

                    }
                }, getContext(), "id" ,String.valueOf(id));
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        swipeRefreshLayout.setRefreshing(true);
        listener.onRefresh();



    }
}
