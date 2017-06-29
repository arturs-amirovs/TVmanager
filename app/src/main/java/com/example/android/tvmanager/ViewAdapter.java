package com.example.android.tvmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arturs.amirovs on 28/06/2017.
 */

public class ViewAdapter extends BaseAdapter implements ListAdapter {
    Context listContext;
    private static LayoutInflater layoutInflater;
    private ArrayList<ShowDetailsClass> showsList;

    public ViewAdapter(Context listContext, ArrayList<ShowDetailsClass> showsList){
        this.showsList = showsList;
        this.listContext = listContext;
        layoutInflater = (LayoutInflater) listContext.getSystemService(listContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return showsList.size();
    }

    @Override
    public Object getItem(int position) {
        return showsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        private TextView nameList;
        private ImageView imageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View view = convertView;

        if(view == null){
            view = layoutInflater.inflate(R.layout.show_list_item, null, true);
            holder = new ViewHolder();
            holder.nameList = (TextView) view.findViewById(R.id.nameList);
            holder.imageList = (ImageView) view.findViewById(R.id.imageList);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameList.setText(showsList.get(position).getName());
        new ImageLoadTask(showsList.get(position).getImage(), holder.imageList).execute();
//        if(showsList.get(position).getOpenNow().equals("Open")) holder.textViewStatus.setTextColor(Color.GREEN);
//        else holder.textViewStatus.setTextColor(Color.RED);

        return view;
    }
}