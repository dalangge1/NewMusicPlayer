package com.example.newmusicplayer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmusicplayer.R;
import com.example.newmusicplayer.bean.AlbumNewestBean;
import com.example.newmusicplayer.bean.AlbumRecommendBean;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;

import java.util.ArrayList;

public class RecyclerViewAdapterAlbumNewestList extends RecyclerView.Adapter<RecyclerViewAdapterAlbumNewestList.ViewHolder> {

    ArrayList<AlbumNewestBean> musicAlbums;
    Context context;

    public RecyclerViewAdapterAlbumNewestList(ArrayList<AlbumNewestBean> musicAlbums, Context context){
        this.musicAlbums = musicAlbums;
        this.context = context;
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ImageLoader.with(context).load(musicAlbums.get(position).getPicUrl()).into(holder.albumImage);
        holder.albumName.setText(musicAlbums.get(position).getName());
        holder.albumArtist.setText(musicAlbums.get(position).getArtist().getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putLong("id",musicAlbums.get(position).getId());
//                NavController controller = Navigation.findNavController(v);
//                controller.navigate(R.id.action_fragmentMain_to_fragmentAlbumList, bundle);
//            }
//        });
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAlbumNewestList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_list_newest_item, parent, false);
        return new RecyclerViewAdapterAlbumNewestList.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return musicAlbums.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName, albumArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumImage);
            albumName = itemView.findViewById(R.id.albumName);
            albumArtist = itemView.findViewById(R.id.albumArtist);
        }
    }
}
