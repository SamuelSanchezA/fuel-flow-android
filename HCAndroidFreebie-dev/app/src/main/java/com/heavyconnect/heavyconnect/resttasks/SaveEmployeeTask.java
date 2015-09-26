package com.heavyconnect.heavyconnect.resttasks;

import android.os.AsyncTask;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.AddEmployeeResult;
import com.heavyconnect.heavyconnect.rest.HttpRetrofitClient;
import com.heavyconnect.heavyconnect.rest.RegisterResult;

import retrofit.client.Response;

/**
 * Created by Andre Menezes on 20/09/2015.
 */
public class SaveEmployeeTask extends AsyncTask<Object, Void, Response> {

    private TaskCallback callback;

    public SaveEmployeeTask(TaskCallback userCallback) {
        this.callback = userCallback;
    }

    @Override
    protected Response doInBackground(Object... params) {
        HttpRetrofitClient retrofitClient = new HttpRetrofitClient();

        if(!(params[0] instanceof String))
            return null;

        if(!(params[1] instanceof Integer))
            return null;

        if(!(params[2] instanceof User))
            return null;

        User user = (User) params[2];

        try {
            return retrofitClient.client.saveEmployeeChanges((String) params[0], (Integer) params[1],
                    user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response result) {
        if(result == null)
            callback.onTaskFailed(-1);
        else
            callback.onTaskCompleted(result);
    }
}
