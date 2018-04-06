package com.binbin.date_demo.FragmentPackge;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.binbin.date_demo.AdapterPackage.DateInfoAdapter;
import com.binbin.date_demo.MainActivity;
import com.binbin.date_demo.Model.DateModel;
import com.binbin.date_demo.R;
import com.binbin.date_demo.Tools;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;

/**
 * Created by 彬彬 on 2018/3/28.
 */

public class MainFragment extends Fragment {

    private TextView textView;
    private ListView listView;
    private static boolean isHave;

    public static Fragment newInstance(ArrayList<DateModel> models, boolean have) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("models", Tools.ToJson(models));
        mainFragment.setArguments(bundle);
        isHave = have;
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view);
        Bundle bundle = getArguments();
        final ArrayList<DateModel> models = Tools.GetArrayDate(bundle.getString("models"));
        final DateInfoAdapter adapter = new DateInfoAdapter(LayoutInflater.from(getActivity()), models);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView idView = view.findViewById(R.id.id);

                new QMUIDialog.MessageDialogBuilder(getActivity()).setMessage("确认删除吗?").addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getResources().getString(R.string.share_name), Context.MODE_PRIVATE).edit();
                        editor.remove(idView.getText().toString());
                        editor.apply();
                        DateModel model = Tools.GetSame(models, idView.getText().toString());
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).UpdateInfo(idView.getText().toString(), isHave);
                        }
                        models.remove(model);
                        adapter.Update(models);
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            }
        });
    }
}
