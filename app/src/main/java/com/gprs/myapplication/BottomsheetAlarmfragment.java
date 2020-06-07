package com.gprs.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomsheetAlarmfragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.alarmitem, container, false);

        TextView name1 = view.findViewById(R.id.name);
        TextView desc1 = view.findViewById(R.id.desc);
        TextView time1 = view.findViewById(R.id.time);
        TextView status = view.findViewById(R.id.status);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        name1.setText(this.getArguments().getString("name"));
        desc1.setText(this.getArguments().getString("desc"));
        time1.setText(this.getArguments().getString("time"));
        if(this.getArguments().getBoolean("onoff"))
            status.setText("ON");
        else
            status.setText("Off");

        return view;
    }

}