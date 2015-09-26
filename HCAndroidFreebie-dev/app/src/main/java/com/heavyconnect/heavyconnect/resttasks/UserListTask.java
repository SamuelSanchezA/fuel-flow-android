package com.heavyconnect.heavyconnect.resttasks;

import android.os.AsyncTask;

import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.UserListResult;
import com.heavyconnect.heavyconnect.rest.HttpRetrofitClient;

/**
 * This class represents the employee list task.
 */
public class UserListTask extends AsyncTask<User, Void, UserListResult> {

    private TaskCallback callback;

    public UserListTask(TaskCallback userCallback) {
        this.callback = userCallback;
    }

    @Override
    protected UserListResult doInBackground(User... params) {
        HttpRetrofitClient retrofitClient = new HttpRetrofitClient();
        User user = params[0];

        try {
            return retrofitClient.client.fetchUserEmployees(user.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(UserListResult result) {
        if(result == null)
            callback.onTaskFailed(-1);
        else if(result.getStatus() != UserListResult.OK)
           callback.onTaskFailed(result.getStatus());
        else
            callback.onTaskCompleted(result);
    }
}
