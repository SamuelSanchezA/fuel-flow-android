package com.heavyconnect.heavyconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.heavyconnect.heavyconnect.adapters.EmployeeListAdapter;
import com.heavyconnect.heavyconnect.entities.Employee;
import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.UserListResult;
import com.heavyconnect.heavyconnect.resttasks.TaskCallback;
import com.heavyconnect.heavyconnect.resttasks.UserListTask;
import com.heavyconnect.heavyconnect.utils.StorageUtils;

import java.util.ArrayList;

/**
 * This class represents the user List screen.
 */
public class UserListActivity extends AppCompatActivity implements View.OnClickListener, TaskCallback{

    private ListView mListView;
    private EmployeeListAdapter mAdapter;
    private ArrayList<Employee> mUsers = new ArrayList<Employee>();

    private User mManager;
    private ProgressDialog mProgress;

    private Button mNewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        if(!StorageUtils.getIsLoggedIn(this) || (mManager = StorageUtils.getUserData(this)) == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.employee_list_need_to_login), Toast.LENGTH_LONG).show();
            finish();
        }

        mProgress = new ProgressDialog(this);
        mProgress.setTitle(null);
        mProgress.setMessage(getString(R.string.employee_list_loading));
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

        mNewUser = (Button) findViewById(R.id.employee_list_add);
        mNewUser.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.employee_list_view);
        refreshEmployeeList();
    }

    /**
     * This method loads the employee list.
     */
    private void refreshEmployeeList(){
        mUsers.clear();

        if(mProgress != null &&  !mProgress.isShowing())
            mProgress.show();

        new UserListTask(this).execute(mManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.employee_list_add:
                startActivityForResult(new Intent(this, UserRegistrationActivity.class),
                        UserRegistrationActivity.ADD_USER_REQUEST_CODE);
                break;
        }
    }


    @Override
    public void onTaskFailed(int code) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        Toast.makeText(this, getString(R.string.employee_list_load_failure), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onTaskCompleted(Object result) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        if(!(result instanceof UserListResult)) {
            onTaskFailed(100);
            return;
        }

        mUsers = ((UserListResult) result).getUsers();

        mAdapter = new EmployeeListAdapter(this, mUsers);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = mAdapter.get(position);
                Intent intent = new Intent(UserListActivity.this, UserRegistrationActivity.class);
                intent.putExtra(UserRegistrationActivity.EMPLOYEE_KEY, employee);
                startActivityForResult(intent, UserRegistrationActivity.ADD_USER_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
            refreshEmployeeList();
    }
}
