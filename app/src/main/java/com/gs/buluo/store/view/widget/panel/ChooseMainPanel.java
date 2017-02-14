package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.MainStandardAdapter;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by hjn on 2017/2/13.
 */
public class ChooseMainPanel extends Dialog{
    private final OnChooseFinished onChooseFinished;
    @Bind(R.id.standard_list)
    ListView listView;
    private String result;

    public ChooseMainPanel(Context context, ArrayList<GoodsPriceAndRepertory> standardList,OnChooseFinished onChooseFinished) {
        super(context, R.style.my_dialog);
        this.onChooseFinished =onChooseFinished;
        initView();
        initData(standardList);
    }

    private void initData(final ArrayList<GoodsPriceAndRepertory> standardList) {
        GoodsPriceAndRepertory gpa = standardList.get(0);
        result =  gpa.firstName + (gpa.secondName==null? "" : "^"+gpa.secondName);
        final MainStandardAdapter adapter = new MainStandardAdapter(getContext(), standardList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<standardList.size();i++){
                    GoodsPriceAndRepertory repertory = standardList.get(i);
                    repertory.checked = (i==position);
                    if (i==position){
                        result = repertory.firstName + (repertory.secondName==null? "" : "^"+repertory.secondName);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.main_standard_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFinished.onFinished(result);
                dismiss();
            }
        });
    }

    public interface OnChooseFinished{
        void onFinished(String result);
    }
}
