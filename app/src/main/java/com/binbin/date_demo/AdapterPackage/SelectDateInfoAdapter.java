package com.binbin.date_demo.AdapterPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.binbin.date_demo.Model.DateModel;
import com.binbin.date_demo.R;

import java.util.ArrayList;

/**
 * Created by 彬彬 on 2018/4/5.
 */

public class SelectDateInfoAdapter extends BaseAdapter {
    private ArrayList<DateModel> models;
    private LayoutInflater inflater;

    public SelectDateInfoAdapter(LayoutInflater layoutInflater, ArrayList<DateModel> dateModels) {
        this.models = dateModels;
        this.inflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.select_date_info_list_item, null);
            viewHolder.Id = view.findViewById(R.id.id);
            viewHolder.Name = view.findViewById(R.id.name);
            viewHolder.Desc = view.findViewById(R.id.desc);
            viewHolder.Date = view.findViewById(R.id.date);
            viewHolder.HaveDay = view.findViewById(R.id.haveDay);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.Id.setText(models.get(i).Id);
        viewHolder.Name.setText(models.get(i).Name);
        viewHolder.Date.setText(models.get(i).Date);
        if (models.get(i).HaveDay.split(",")[0].equals("+")) {
            viewHolder.HaveDay.setText("还有" + models.get(i).HaveDay.split(",")[1] + "天");
        } else {
            viewHolder.HaveDay.setText("已过" + models.get(i).HaveDay.split(",")[1] + "天");
        }
        viewHolder.Desc.setText(models.get(i).Desc);
        return view;
    }

    class ViewHolder {
        public TextView Id;
        public TextView Name;
        public TextView Date;
        public TextView HaveDay;
        public TextView Desc;
    }

    public void Update(ArrayList<DateModel> dateModels) {
        models = dateModels;
        notifyDataSetChanged();
    }

}
