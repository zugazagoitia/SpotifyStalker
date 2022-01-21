package com.zugazagoitia.spotifystalker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zugazagoitia.spotifystalker.data.model.UserPlayingInfo;

import java.util.List;

/**
 * Created by Rp on 3/16/2016.
 */
public class ListbaseAdapter extends BaseAdapter {

    Context context;
    List<UserPlayingInfo> beans;


    public ListbaseAdapter(Context context, List<UserPlayingInfo> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size() ;
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.listview,null);


            viewHolder.image= convertView.findViewById(R.id.image);
            viewHolder.usuario= convertView.findViewById(R.id.usuario);
            viewHolder.trackAndAlbum = convertView.findViewById(R.id.trackAndAlbum);
            viewHolder.context= convertView.findViewById(R.id.context);
            viewHolder.status= convertView.findViewById(R.id.status);


            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder)convertView.getTag();

        }

        UserPlayingInfo beans = (UserPlayingInfo)getItem(position);

        Glide.with(convertView)
                .load(beans.getUser().getImageUrl())
                .placeholder(R.drawable.resource_default)
                .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                .into(viewHolder.image);
        viewHolder.usuario.setText(beans.getUser().getName());
        viewHolder.trackAndAlbum.setText(beans.getTrack().getName() +
                " - " +
                beans.getTrack().getAlbum().getName());
        viewHolder.context.setText(beans.getTrack().getContext() == null ? "" : beans.getTrack().getContext().getName());
        viewHolder.status.setText(timeStampToDisplayString(beans.getTimestamp()));

        viewHolder.userUri = beans.getUser().getUri();
        viewHolder.albumUri = beans.getTrack().getAlbum().getUri();
        viewHolder.trackUri = beans.getTrack().getUri();
        viewHolder.contextUri = beans.getTrack().getContext() == null ? "null" : beans.getTrack().getContext().getUri();

        //Enabling marquee text
        viewHolder.trackAndAlbum.setSelected(true);
        viewHolder.context.setSelected(true);


        return convertView;
    }

    public static String timeStampToDisplayString(long timestamp){
        final long ONE_SECOND = 1000L;
        final long ONE_MINUTE = 60000L;
        final long ONE_HOUR = 3600000L;
        final long ONE_DAY = 86400000L;
        final long ONE_MONTH = 2592000000L;
        final long ONE_YEAR = 31536000000L;
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - timestamp;
        if (difference < ONE_MINUTE) {
            return new String(Character.toChars(0x1F3B6));
        } else if (difference < ONE_HOUR) {
            long timeAgo = difference / ONE_MINUTE;
            int finalUnits = (int) timeAgo;
            return  finalUnits + " " + "min";
        } else if (difference < ONE_DAY) {
            long timeAgo = difference / ONE_HOUR;
            int finalUnits = (int) timeAgo;
            return finalUnits + " " + "hr";
        } else if (difference < ONE_MONTH) {
            long timeAgo = difference / ONE_DAY;
            int finalUnits = (int) timeAgo;
            return  finalUnits +" " + "d";
        } else if (difference < ONE_YEAR) {
            long timeAgo = difference / ONE_MONTH;
            int finalUnits = (int) timeAgo;
            return  finalUnits + " " + "M";
        } else {
            long timeAgo = difference / ONE_MONTH;
            int finalUnits = (int) timeAgo;
            return  finalUnits + "" + "M";

        }    }

    static class ViewHolder{
        ImageView image;
        TextView usuario;
        TextView trackAndAlbum;
        TextView context;
        TextView status;
        String userUri;
        String albumUri;
        String trackUri;
        String contextUri;
    }





}
