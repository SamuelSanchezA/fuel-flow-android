package com.heavyconnect.heavyconnect.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.widget.TextView;

import com.heavyconnect.heavyconnect.R;


/**
 * This class represents the about dialog.
 *
 * @author Felipe Porge Xavier - <a href="http://www.felipeporge.com" target="_blank">http://www.felipeporge.com</a>
 */
public class AboutDialog {

    private Dialog mDialog;

    /**
     * Constructor method.
     * @param context - The context.
     */
    public AboutDialog(final Context context){

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_about);

        TextView versionView = (TextView) mDialog.findViewById(R.id.about_version_view);
        versionView.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionView.setText(Html.fromHtml("<b>" + context.getString(R.string.app_name) + "</b><br/>" + context.getString(R.string.about_version) + " " +
                    pInfo.versionName + "<br/><br/><b><a href=\"" + context.getString(R.string.about_site)+ "\">" +
                    context.getString(R.string.about_site) + "</a></b>"));
        }catch(Exception e){}
    }

    /**
     * This method shows the about dialog.
     */
    public void show(){
        if(mDialog != null && !mDialog.isShowing()) {
            try {
                mDialog.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This method hides the about dialog.
     */
    public void dismiss(){
        if(mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
}
