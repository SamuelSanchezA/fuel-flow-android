package com.heavyconnect.heavyconnect.resttasks;

import android.os.AsyncTask;
import android.util.Log;

import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.AddEmployeeResult;
import com.heavyconnect.heavyconnect.rest.HttpRetrofitClient;
import com.heavyconnect.heavyconnect.rest.RegisterResult;
import com.heavyconnect.heavyconnect.utils.Constants;

import retrofit.client.Response;

/**
 * Created by Andre Menezes on 20/09/2015.
 */
public class RemoveEmployeeTask extends AsyncTask<Object, Void, Response> {

    private TaskCallback callback;

    public RemoveEmployeeTask(TaskCallback userCallback) {
        this.callback = userCallback;
    }

    @Override
    protected Response doInBackground(Object... params) {
        HttpRetrofitClient retrofitClient = new HttpRetrofitClient();

        if(!(params[0] instanceof String))
            return null;

        if(!(params[1] instanceof Integer))
            return null;


        try {
            return retrofitClient.client.removeEmployee((String) params[0], (Integer) params[1]);
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
