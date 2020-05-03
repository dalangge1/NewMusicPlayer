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
import com.example.newmusicplayer.bean.LibraryBean;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;

import java.util.ArrayList;

public class RecyclerViewAdapterLibrary extends RecyclerView.Adapter<RecyclerViewAdapterLibrary.ViewHolder> {


    ArrayList<LibraryBean> albums;
    Context context;

    public void changeData(ArrayList<LibraryBean> albums){
        this.albums = albums;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapterLibrary(ArrayList<LibraryBean> albums, Context context){
        this.albums = albums;
        this.context =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_library_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        ImageLoader.with(context).load(albums.get(position).getCoverImgUrl()).into(holder.albumImage);
        holder.name.setText(albums.get(position).getName());
        holder.artist.setText(albums.get(position).getCreator().getNickname());
        holder.count.setText(albums.get(position).getTrackCount() + " Tracks");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("id",albums.get(position).getId());
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_fragmentLibrary_to_fragmentAlbumList, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView name, artist, count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumImage);
            name = itemView.findViewById(R.id.name);
            artist = itemView.findViewById(R.id.artist);
            count = itemView.findViewById(R.id.count);
        }
    }

}
