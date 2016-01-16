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
public class LockFragmentAdapter extends RecyclerView.Adapter<LockFragmentAdapter.LockFragmentViewHolder> {


    private List<AppInfo> lockAppInfos;
    private Context context;
    private AppLockDB db;

    public LockFragmentAdapter(List<AppInfo> appInfos, Context context, AppLockDB db) {

        this.lockAppInfos = appInfos;
        this.context = context;
        this.db = db;


    }


    @Override
    public LockFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.app_lock_item, parent, false);
        LockFragmentViewHolder holder = new LockFragmentViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final LockFragmentViewHolder holder, int position) {
        holder.tvName.setText(lockAppInfos.get(position).getAppName());
        holder.ivIcon.setImageDrawable(lockAppInfos.get(position).getIcon());
        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(lockAppInfos.get(holder.getAdapterPosition()).getPackageName());
                lockAppInfos.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                Intent intent = new Intent("com.brilliantbear.putdownthephone.REMOVE_APP");
                context.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lockAppInfos.size();
    }

    class LockFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivIcon;
        ImageView ivAction;

        public LockFragmentViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            ivAction = (ImageView) itemView.findViewById(R.id.iv_action);
        }
    }
}
