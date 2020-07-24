package com.denobili.app.helper_utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denobili.app.R;
import com.denobili.app.studentallEventsPage.StudentEventModel;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.CustomViewHolder> {

    private List<StudentEventModel> imageModelsList;
    private Context mContext;
    private Activity act;

    public UserListAdapter(Context context, ArrayList<StudentEventModel> imageList, Activity activity) {
        this.imageModelsList = imageList;
        this.mContext = context;
        this.act = activity;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.row_student_events, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final StudentEventModel get = imageModelsList.get(position);



    }


    @Override
    public int getItemCount() {
        return (null != imageModelsList ? imageModelsList.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public CustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.event_message);
        }
    }
}