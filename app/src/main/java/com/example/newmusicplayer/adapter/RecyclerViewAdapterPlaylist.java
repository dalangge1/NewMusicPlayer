package com.example.newmusicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmusicplayer.MusicViewModel;
import com.example.newmusicplayer.R;
import com.example.newmusicplayer.bean.AlbumDetailBean;
import com.example.newmusicplayer.utils.MyImageUtils.ImageLoader;
import com.example.newmusicplayer.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerViewAdapterPlaylist extends RecyclerView.Adapter<RecyclerViewAdapterPlaylist.ViewHolder> {

    private static final int ALBUM_VIEW = 0;
    private static final int TITLE_VIEW = 1;
    private static final int NORMAL_VIEW = 2;



    AlbumDetailBean album;
    Context context;
    MusicViewModel musicViewModel;

    public void changeData(AlbumDetailBean album){
        this.album = album;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapterPlaylist(AlbumDetailBean album, final Context context, MusicViewModel musicViewModel){
        this.album = album;
        this.musicViewModel = musicViewModel;
        this.context = context;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,500,2000);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_library_item,parent,false);
        }else if (viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_title,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_item,parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return ALBUM_VIEW;
        }else if(position == 1){
            return TITLE_VIEW;
        }else return NORMAL_VIEW;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(position == 0){
            ImageLoader.with(context).load(album.getCoverImgUrl()).into(holder.albumImage);
            holder.name.setText(album.getName());
            holder.count.setText(album.getTracksList().length + " Tracks");
            holder.artist.setText(album.getCreator().getNickname());
        }else if (position == 1){

        }else {
            holder.name.setText(album.getTracksList()[position-2].getName());
            holder.duration.setText(TimeUtils.getGapTime(album.getTracksList()[position-2].getDt()));
            holder.num.setText(String.valueOf(position - 1));
            if(album.getTracksList()[position-2].getId() == musicViewModel.musicId.getValue()){
                holder.name.setTextColor(context.getResources().getColor(R.color.colorLightBlue));
            }else {
                holder.name.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for(int i = 0; i < album.getTracksList().length; i ++){
                        list.add(album.getTracksList()[i].getId());
                    }
                    musicViewModel.pos = position - 2;
                    musicViewModel.musicIdList.setValue(list);

                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_fragmentAlbumList_to_fragmentMusicDetails);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(album != null){
            return album.getTracksList().length + 2;

        }else return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView name, artist, count, num, duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumImage);
            name = itemView.findViewById(R.id.name);
            artist = itemView.findViewById(R.id.artist);
            count = itemView.findViewById(R.id.count);
            num = itemView.findViewById(R.id.num);
            duration = itemView.findViewById(R.id.duration);
        }
    }

}
