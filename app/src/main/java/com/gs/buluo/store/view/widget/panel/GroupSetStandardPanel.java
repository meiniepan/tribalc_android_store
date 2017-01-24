package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/22.
 */
public class GroupSetStandardPanel extends Dialog{
    Context mCtx;
    EditText etPrice;
    EditText etReper;
    private  onSetFinishListener onSetFinishListener;
    public GroupSetStandardPanel(Context context,onSetFinishListener onSetFinishListener) {
        super(context, R.style.sheet_dialog);
        mCtx = context;
        this.onSetFinishListener = onSetFinishListener;
        intiView();
    }

    private void intiView() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        View rootView = View.inflate(mCtx,R.layout.standard_board,null);
        etPrice = (EditText) rootView.findViewById(R.id.standard_price);
        etReper = (EditText) rootView.findViewById(R.id.standard_repertory);

        rootView.findViewById(R.id.standard_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetFinishListener.onSetFinished(Float.parseFloat(etPrice.getText().toString().trim()),Integer.parseInt(etReper.getText().toString().trim()));
                dismiss();
            }
        });
        rootView.findViewById(R.id.standard_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(rootView);
    }

    public interface onSetFinishListener{
        void onSetFinished(float price,int repertory);
    }
}
