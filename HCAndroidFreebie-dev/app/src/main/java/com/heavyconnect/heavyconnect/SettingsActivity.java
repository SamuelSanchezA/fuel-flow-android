package com.heavyconnect.heavyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.heavyconnect.heavyconnect.dialog.AboutDialog;

/**
 * This class represents the settings screen.
 */
public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mList = (ListView) findViewById(R.id.settings_listview);
        mList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.settings_options)));
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                startActivity(new Intent(this, UserListActivity.class));
                break;
            case 1:
                new AboutDialog(this).show();
                break;
//            case 2:
//                AlertDialog.Builder removeAccountDialog = new AlertDialog.Builder(this);
//                removeAccountDialog.setTitle(R.string.remove_account_dialog_title);
//                removeAccountDialog
//                        .setMessage(R.string.remove_account_dialog_remove_manager_account_message)
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.remove_account_dialog_confirm_button, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                SettingsActivity.this.finish();
//                                // TODO: delete employee and everything related to him
//                            }
//                        })
//                        .setNegativeButton(R.string.remove_account_dialog_cancel_button, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = removeAccountDialog.create();
//                alertDialog.show();
//                break;
        }
    }
}
