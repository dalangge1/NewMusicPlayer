package com.example.newmusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newmusicplayer.R;
import com.example.newmusicplayer.bean.AlbumNewestBean;
import com.example.newmusicplayer.bean.AlbumRecommendBean;

import java.util.ArrayList;

public class RecyclerViewAdapterAlbumMain extends RecyclerView.Adapter<RecyclerViewAdapterAlbumMain.ViewHolder> {
    private static final int NEWEST_VIEW = 0;
    private static final int TITLE_SHAPE_1 = 1;
    private static final int TITLE_SHAPE_2 = 2;
    private static final int NORMAL_VIEW = 3;

    ArrayList<AlbumRecommendBean> musicRecommendAlbums;
    ArrayList<AlbumNewestBean> musicNewestAlbums;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;

    public RecyclerViewAdapterAlbumMain(ArrayList<AlbumRecommendBean> musicRecommendAlbums, ArrayList<AlbumNewestBean> musicNewestAlbums, Context context, SwipeRefreshLayout swipeRefreshLayout){
        this.musicRecommendAlbums = musicRecommendAlbums;
        this.musicNewestAlbums = musicNewestAlbums;
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    //用于绑定viewModel
    public void changeRecommendData(ArrayList<AlbumRecommendBean> musicAlbums){
        this.musicRecommendAlbums = musicAlbums;
    }
    //用于绑定viewModel
    public void changeNewestData(ArrayList<AlbumNewestBean> musicAlbums){
        this.musicNewestAlbums = musicAlbums;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0){
            holder.title.setText("New Albums");
        }else if (position == 1){
            holder.recyclerViewNewest.setAdapter(new RecyclerViewAdapterAlbumNewestList(musicNewestAlbums,context));
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerViewNewest.setLayoutManager(layoutManager);
        }else if (position == 2){
            holder.title.setText("Recommendations");
        } else if (position == 3){
            holder.recyclerViewRecommend.setAdapter(new RecyclerViewAdapterAlbumRecommendList(musicRecommendAlbums,context));
            holder.recyclerViewRecommend.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TITLE_SHAPE_1;
        }else if (position == 1){
            return NEWEST_VIEW;
        }else if (position == 2){
            return TITLE_SHAPE_2;
        }else if (position == 3){
            return NORMAL_VIEW;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAlbumMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_recommend_item, parent, false);
        switch (viewType){
            case NORMAL_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_recommend_recycler, parent, false);
                break;
            case NEWEST_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_newest_recycler, parent, false);
                break;
            case TITLE_SHAPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_title_shape1, parent, false);
                break;
            case TITLE_SHAPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_title_shape2, parent, false);
                break;
        }
        return new RecyclerViewAdapterAlbumMain.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName, albumArtist, title;
        RecyclerView recyclerViewRecommend, recyclerViewNewest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumImage);
            albumName = itemView.findViewById(R.id.albumName);
            albumArtist = itemView.findViewById(R.id.albumArtist);
            recyclerViewRecommend = itemView.findViewById(R.id.recyclerViewRecommend);
            recyclerViewNewest = itemView.findViewById(R.id.recyclerViewNewest);
            title = itemView.findViewById(R.id.title);
        }
    }
}





