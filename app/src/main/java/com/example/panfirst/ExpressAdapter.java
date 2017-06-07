package com.example.panfirst;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.panfirst.gson.ExpressContent;

import java.util.List;

/**
 * Created by asus-pc on 2017/6/7.
 */

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ViewHolder> {


    private  List<ExpressContent> mExpressContentList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tv1;
        TextView tv2;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tv1 = (TextView) itemView.findViewById(R.id.express_content);
            tv2 = (TextView) itemView.findViewById(R.id.express_date);
        }
    }

    public ExpressAdapter(List<ExpressContent> expressContentList){
        mExpressContentList = expressContentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.express_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpressAdapter.ViewHolder holder, int position) {

        ExpressContent expressContent = mExpressContentList.get(position);
        holder.tv1.setText(expressContent.getRemark());
        holder.tv2.setText(expressContent.getDatetime());
    }

    @Override
    public int getItemCount() {
        return mExpressContentList.size();
    }
}
