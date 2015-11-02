package com.cyt.ieasy.setting;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import com.cyt.ieasy.mobilelingliao.R;
import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView;

/**
 * 以弹出框的样式显示更新升级日志
 * Created by jin on 2015.11.02.
 */
public class ChangeLogDialog extends DialogFragment {

    public ChangeLogDialog(){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        ChangeLogRecyclerView chgList= (ChangeLogRecyclerView) layoutInflater.inflate(R.layout.layout_changelog, null);
        return new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.update_title)
                .setView(chgList)
                .setPositiveButton(R.string.update_btnclose,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
