package yys.com.myopeneye.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import yys.com.myopeneye.R;
import yys.com.myopeneye.data.model.DailyEntity.IssueEntity.ItemListEntity;
import yys.com.myopeneye.view.activity.PlayActivity;

/**
 * Created by yangys on 2019/3/5.
 */

public class DailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<ItemListEntity> datas;
    private LayoutInflater inflater;

    private static final int BANNER = 1;
    private static final int VIDEO = 2;

    public DailyAdapter(Context context,List<ItemListEntity> datas){
        this.context = context;
        this.datas = datas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        View view;

        if(viewType == BANNER){
           view = inflater.inflate(R.layout.daily_item_banner,parent,false);
           holder = new BannerHolder(view);
        }else{
            view = inflater.inflate(R.layout.daily_item_video,parent,false);
            holder = new VideoHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof VideoHolder){
            VideoHolder vholder = (VideoHolder) holder;
            Glide.with(context)
                    .load(datas.get(position).getData().getCover().getFeed())
                    .into(vholder.content_imgview);

            vholder.title_textView.setText(datas.get(position).getData().getTitle());
            vholder.duraView.setText(getTimeString(datas.get(position).getData().getDuration()));
            vholder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayActivity.class);
                    Bundle bundle = new Bundle();
                    ItemListEntity itemListEntity = datas.get(position);
                    bundle.putLong("duration",itemListEntity.getData().getDuration());
                    bundle.putString("categary",itemListEntity.getData().getCategory());
                    bundle.putString("img",itemListEntity.getData().getCover().getFeed());
                    bundle.putString("url",itemListEntity.getData().getPlayUrl());
                    bundle.putString("title",itemListEntity.getData().getTitle());
                    bundle.putString("descript",itemListEntity.getData().getDescription());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }else if(holder instanceof BannerHolder){
            Glide.with(context)
                    .load(datas.get(position).getData().getImage())
                    .into(((BannerHolder) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(datas.get(position).getType().equals("video")){
            return VIDEO;
        }else{
            return BANNER;
        }
    }


    class BannerHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public BannerHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.banner_img_view);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder{

        ImageView content_imgview;
        TextView title_textView;
        TextView duraView;
        View view;

        @SuppressLint("WrongViewCast")
        public VideoHolder(View view){
            super(view);
            content_imgview = view.findViewById(R.id.content_img_view);
            title_textView = view.findViewById(R.id.title_text_view);
            duraView = view.findViewById(R.id.time_text_view);
            this.view = view;
        }

    }


    private String getTimeString(int duration){
        String durationstr = "";
        int seconds = duration % 60;
        int minutes = duration / 60;
        durationstr = (minutes>9? ""+minutes : "0"+minutes) +"'" + (seconds>9? ""+seconds : "0"+seconds +'"');
        return durationstr;
    };

}
