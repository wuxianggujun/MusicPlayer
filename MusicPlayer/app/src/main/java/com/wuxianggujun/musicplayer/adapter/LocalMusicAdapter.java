package com.wuxianggujun.musicplayer.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.List;
import com.wuxianggujun.musicplayer.model.LocalMusicBean;
import android.view.LayoutInflater;
import com.wuxianggujun.musicplayer.R;
import android.widget.TextView;
/**
 * @作者: 无相孤君
 * @QQ: 3344207732
 * @描述:    
 */
public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {

    private Context context;
    private List<LocalMusicBean> mDatas;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

   
      
    public interface OnItemClickListener{
          void OnItemClick(View v,int position);
    }
    
    
    public LocalMusicAdapter(Context context, List<LocalMusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }
    
    
    @Override
    public LocalMusicAdapter.LocalMusicViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        return new LocalMusicViewHolder(view);
    }

    
    @Override
    public void onBindViewHolder(LocalMusicAdapter.LocalMusicViewHolder holder, final int position) {
     LocalMusicBean bean =  mDatas.get(position);
      holder.song.setText(bean.getSong());
      holder.singer.setText(bean.getSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(p1,position);
                    }
                    
                }
            });
            
            
    }

    
    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    
    
    
    
    
    
   class LocalMusicViewHolder extends RecyclerView.ViewHolder{
       TextView singer,song;
       public LocalMusicViewHolder(View itemView){
           super(itemView);
           song = itemView.findViewById(R.id.itemlocalmusicTextView1);
           singer = itemView.findViewById(R.id.item_local_musicTextView2);   
       }
       
       
       
   }
    
}
