package com.example.imageredactorcft;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.List;

public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.ResultViewHolder>{

    Context context;
    private ResultPopMenuClickListener popMenuClickListener;

    class ResultViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rootView;
        ImageView imvResult;

        ResultViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        void initViews(View itemView) {
            imvResult = itemView.findViewById(R.id.imv_rv_result);
            rootView=itemView.findViewById(R.id.root_view);
        }
    }

    List<PictureClass> data;

    public ResultRecyclerAdapter(Context _context, ResultPopMenuClickListener _popMenuClickListener, List<PictureClass> data) {
        this.data = data;
        popMenuClickListener=_popMenuClickListener;
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
		view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int adapterPosition = viewHolder.getAdapterPosition();
				if (adapterPosition != RecyclerView.NO_POSITION) {

					final PictureClass picture = data.get(adapterPosition);
					createPopupMenu(view, picture);
				}
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int i) {

        Bitmap bitmap = PictureUtils.getScaledBitmap(data.get(i).getPath(),(Activity) context);
        int position = holder.getAdapterPosition();
        if(position % 2 != 0)
        {
            holder.rootView.setBackgroundResource(R.color.colorBackItem);
        }
        else
            holder.rootView.setBackgroundResource(R.color.colorBackItemWhite);

        holder.imvResult.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private void createPopupMenu(View v, final PictureClass _selectedPicture){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.popup_menu_rv);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.pop_menu_item_source:
                        popMenuClickListener.onSourceClick(_selectedPicture);
                        return true;
                    case R.id.pop_menu_item_delete:
                        popMenuClickListener.onDeleteClick(_selectedPicture);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    public interface ResultPopMenuClickListener {
        void onSourceClick(PictureClass picture);
        void onDeleteClick(PictureClass picture);
    }
}
