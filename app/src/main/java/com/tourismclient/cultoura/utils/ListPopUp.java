package com.tourismclient.cultoura.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tourismclient.cultoura.R;

import java.util.ArrayList;

public class ListPopUp {

    private ListPopUpListener listener;
    private ArrayAdapter<String> listAdapter;
    private AlertDialog.Builder listBuilder;
    private Dialog myDialog;

    public interface ListPopUpListener {
        public void onItemClicked(String selectedItem);
    }

    public ListPopUp() {
        this.listener = null;
    }

    public void setListPopUptListener(ListPopUpListener listener) {
        this.listener = listener;
    }

    public void showListPopup(Context context, String title , ArrayList<String> dataArray)
    {
        listBuilder = new AlertDialog.Builder(context);
        listBuilder.setTitle(title);

        listAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        for(int j = 0; j < dataArray.size(); j++)
        {
            listAdapter.add(dataArray.get(j));
        }

        listBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        listBuilder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = listAdapter.getItem(which);
                if(listener != null)
                    listener.onItemClicked(strName);
            }
        });
        listBuilder.show();
    }

    public void showPopup(String text, String action_text, Context context) {
        TextView info_text;
        Button action_button;
        if(myDialog == null) {
            myDialog = new Dialog(context);
        }
        myDialog.setContentView(R.layout.basic_popup);
        action_button =myDialog.findViewById(R.id.action_button);
        info_text =myDialog.findViewById(R.id.info_text);
        info_text.setText(text);
        action_button.setText(action_text);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if(listener != null)
                    listener.onItemClicked("");
            }
        });
        myDialog.show();
    }

}
