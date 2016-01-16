package com.brilliantbear.putdownthephone.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.brilliantbear.putdownthephone.R;
import com.brilliantbear.putdownthephone.adapter.LockFragmentAdapter;
import com.brilliantbear.putdownthephone.db.AppLockDB;
import com.brilliantbear.putdownthephone.domain.AppInfo;
import com.brilliantbear.putdownthephone.engine.AppInfoParser;

import java.util.ArrayList;
import java.util.List;


public class LockFragment extends Fragment {

    private RecyclerView recyclerView;
    private LockFragmentAdapter adapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setProgressBarVisiable(false);
            if (msg.what == 0) {
                recyclerView.setAdapter(adapter);
            } else if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
        }
    };
    private AddAppReceiver addAppReceiver;
    private List<AppInfo> appInfos;
    private ArrayList<AppInfo> lockAppInfos;
    private AppLockDB db;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lock_lauout, null);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void setProgressBarVisiable(boolean visiable){
        if(visiable){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lockAppInfos = new ArrayList<>();
        db = new AppLockDB(getActivity());
        setProgressBarVisiable(true);
        new Thread() {
            @Override
            public void run() {
                appInfos = AppInfoParser.getAppInfos(getActivity());
                lockAppInfos.clear();
                for (AppInfo appInfo : appInfos) {
                    String packageName = appInfo.getPackageName();
                    if (db.contain(packageName)) {
                        lockAppInfos.add(appInfo);
                    }
                }
                adapter = new LockFragmentAdapter(lockAppInfos, getActivity(), db);
                handler.sendEmptyMessage(0);
            }
        }.start();

        addAppReceiver = new AddAppReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.brilliantbear.putdownthephone.ADD_APP");
        getActivity().registerReceiver(addAppReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(addAppReceiver);
    }

    public void notifyDataSetChanged() {
        setProgressBarVisiable(true);
        new Thread() {
            @Override
            public void run() {
                lockAppInfos.clear();
                for (AppInfo appInfo : appInfos) {
                    String packageName = appInfo.getPackageName();
                    if (db.contain(packageName)) {
                        lockAppInfos.add(appInfo);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }.start();


    }

    class AddAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            notifyDataSetChanged();
        }
    }

}
