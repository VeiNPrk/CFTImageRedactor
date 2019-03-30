package com.example.imageredactorcft;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.ResultViewHolder>{

    Context context;
    class ResultViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout rootView;
        ImageView imvResult;
        //TextView tvHistoryLang;

        ResultViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        void initViews(View itemView) {
            imvResult = itemView.findViewById(R.id.imv_rv_result);
            rootView=itemView.findViewById(R.id.root_view);
            //tvHistoryLang = (TextView)itemView.findViewById(R.id.tv_history_lang);
        }
    }

    List<PictureClass> data;

    public ResultRecyclerAdapter(Context _context, List<PictureClass> data) {
        this.data = data;
        context=_context;
    }

    public void setData(List<PictureClass> results){
        data=results;
        notifyDataSetChanged();
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_maket, parent, false);
        final ResultViewHolder viewHolder = new ResultViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int i) {
        Bitmap bitmap = PictureUtils.getScaledBitmap(data.get(i).getPath(),(Activity) context);
        if(i % 2 != 0)
        {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            holder.rootView.setBackgroundResource(R.color.colorBackItem);
        }
        holder.imvResult.setImageBitmap(bitmap);
        /*holder.tvHistoryLang.setText(data.get(i).getLang());*/
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
