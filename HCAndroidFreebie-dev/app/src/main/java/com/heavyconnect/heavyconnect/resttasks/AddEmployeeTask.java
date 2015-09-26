package com.heavyconnect.heavyconnect.resttasks;

import android.os.AsyncTask;
import android.util.Log;

import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.AddEmployeeResult;
import com.heavyconnect.heavyconnect.rest.HttpRetrofitClient;
import com.heavyconnect.heavyconnect.rest.RegisterResult;
import com.heavyconnect.heavyconnect.utils.Constants;

/**
 * Created by Andre Menezes on 20/09/2015.
 */
public class AddEmployeeTask extends AsyncTask<Object, Void, AddEmployeeResult> {

    private TaskCallback callback;

    public AddEmployeeTask(TaskCallback userCallback) {
        this.callback = userCallback;
    }

    @Override
    protected AddEmployeeResult doInBackground(Object... params) {
        HttpRetrofitClient retrofitClient = new HttpRetrofitClient();

        if(!(params[0] instanceof String))
            return null;

        if(!(params[1] instanceof User))
            return null;

        User user = (User) params[1];

        try {
            return retrofitClient.client.createEmployee((String) params[0],
                    user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(AddEmployeeResult result) {
        if(result == null)
            callback.onTaskFailed(-1);
        else if(result.getStatus() != RegisterResult.OK)
            callback.onTaskFailed(result.getStatus());
        else
            callback.onTaskCompleted(result);
    }
}
