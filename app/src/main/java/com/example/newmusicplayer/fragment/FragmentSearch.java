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
import android.widget.EditText;
import android.widget.TextView;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.adapter.RecyclerViewAdapterLibrary;
import com.example.newmusicplayer.adapter.RecyclerViewAdapterSearch;
import com.example.newmusicplayer.animation.AnimationUtils;
import com.example.newmusicplayer.bean.LibraryBean;
import com.example.newmusicplayer.utils.HttpUtils.HttpUtil;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonArray;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonEval;
import com.example.newmusicplayer.utils.JSonEvalUtils.JSonObject;
import com.example.newmusicplayer.utils.LogUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    private static final String LIBRARY_URL = "http://47.99.165.194/search";
    private TextView search;
    private EditText searchPlainText;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<LibraryBean> albums;
    private RecyclerViewAdapterSearch adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener listener;

    public FragmentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        search = getView().findViewById(R.id.search);
        searchPlainText = getView().findViewById(R.id.searchPlainText);
        recyclerView = getView().findViewById(R.id.recyclerView);
        refreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);

        adapter = new RecyclerViewAdapterSearch(new ArrayList<LibraryBean>(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.buttonClickAnimation(v);
                swipeRefreshLayout.setRefreshing(true);

                listener = new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        HttpUtil.getInstance().httpGet(LIBRARY_URL, new Callback() {
                            @Override
                            public void callback(String respond) {
                                try {

                                    JSonObject jSonObject = new JSonObject(new JSonObject(respond).getString("result"));
                                    //LogUtil.e("============"+new JSonObject(respond).getString("result"));
                                    JSonArray jSonArray = new JSonArray(jSonObject.getString("playlists"));

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
                        }, getContext(), "keywords", searchPlainText.getText().toString(), "type", "1000");
                    }
                };

                listener.onRefresh();



            }
        });



        refreshLayout.setOnRefreshListener(listener);



    }
}
