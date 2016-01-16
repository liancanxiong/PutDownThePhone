package com.brilliantbear.putdownthephone.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brilliantbear.putdownthephone.R;
import com.brilliantbear.putdownthephone.db.AppLockDB;
import com.brilliantbear.putdownthephone.domain.AppInfo;

import java.util.List;

/**
 * Created by Bear on 2016/1/13.
 */
public class UnlockFragmentAdapter extends RecyclerView.Adapter<UnlockFragmentAdapter.UnlockFragmentViewHolder> {


    private List<AppInfo> unlockAppInfos;
    private Context context;
    private AppLockDB db;

    public UnlockFragmentAdapter(List<AppInfo> appInfos, Context context, AppLockDB db) {
        this.unlockAppInfos = appInfos;
        this.context = context;
        this.db = db;

    }


    @Override
    public UnlockFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.app_unlock_item, parent, false);
        UnlockFragmentViewHolder holder = new UnlockFragmentViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final UnlockFragmentViewHolder holder, int position) {
        holder.tvName.setText(unlockAppInfos.get(position).getAppName());
        holder.ivIcon.setImageDrawable(unlockAppInfos.get(position).getIcon());
        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.add(unlockAppInfos.get(holder.getAdapterPosition()).getPackageName());
                unlockAppInfos.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                Intent intent = new Intent("com.brilliantbear.putdownthephone.ADD_APP");
                context.sendBroadcast(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return unlockAppInfos.size();
    }

    class UnlockFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivIcon;
        ImageView ivAction;

        public UnlockFragmentViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            ivAction = (ImageView) itemView.findViewById(R.id.iv_action);
        }


    }

}
