package com.example.newmusicplayer.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.adapter.RecyclerViewAdapterLibrary;
import com.example.newmusicplayer.bean.LibraryBean;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonArray;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLibrary extends Fragment {
    private static final String LIBRARY_URL = "http://47.99.165.194/user/playlist";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<LibraryBean> albums;
    private RecyclerViewAdapterLibrary adapter;

    public FragmentLibrary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);

        adapter = new RecyclerViewAdapterLibrary(new ArrayList<LibraryBean>(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpUtil.getInstance().httpGet(LIBRARY_URL, new Callback() {
                    @Override
                    public void callback(String respond) {
                        try {
                            JSonObject jSonObject = new JSonObject(respond);
                            JSonArray jSonArray = new JSonArray(jSonObject.getString("playlist"));

                            albums = new ArrayList<>();
                            for(int i = 0; i < jSonArray.size(); i ++){
                                albums.add(JSonEval.getInstance().fromJson(jSonArray.get(i), LibraryBean.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.changeData(albums);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void callback() {

                    }
                }, getContext(), "uid", "318082831");
            }
        };

        swipeRefreshLayout.setOnRefreshListener(listener);
        swipeRefreshLayout.setRefreshing(true);
        listener.onRefresh();

    }
}
