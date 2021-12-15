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
import com.bumptech.glide.RequestBuilder;
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

        ViewHolder viewHolder = null;

        if (convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.listview,null);


            viewHolder.image= (ImageView)convertView.findViewById(R.id.image);
            viewHolder.usuario= (TextView)convertView.findViewById(R.id.usuario);
            viewHolder.trackAndAlbum = (TextView)convertView.findViewById(R.id.trackAndAlbum);
            viewHolder.context= (TextView)convertView.findViewById(R.id.context);


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
        viewHolder.trackAndAlbum.setText(beans.getTrack().getName()+" - "+beans.getTrack().getAlbum().getName());
        viewHolder.context.setText(beans.getTrack().getContext().getName());

        viewHolder.userUri = beans.getUser().getUri();
        viewHolder.albumUri = beans.getTrack().getAlbum().getUri();
        viewHolder.trackUri = beans.getTrack().getUri();
        viewHolder.contextUri = beans.getTrack().getContext().getUri();

        return convertView;
    }


    class ViewHolder{
        ImageView image;
        TextView usuario;
        TextView trackAndAlbum;
        TextView context;
        String userUri;
        String albumUri;
        String trackUri;
        String contextUri;
    }





}
