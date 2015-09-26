package com.heavyconnect.heavyconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heavyconnect.heavyconnect.entities.Employee;
import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.AddEmployeeResult;
import com.heavyconnect.heavyconnect.resttasks.AddEmployeeTask;
import com.heavyconnect.heavyconnect.resttasks.RemoveEmployeeTask;
import com.heavyconnect.heavyconnect.resttasks.SaveEmployeeTask;
import com.heavyconnect.heavyconnect.resttasks.TaskCallback;
import com.heavyconnect.heavyconnect.utils.StorageUtils;

import retrofit.client.Response;


/**
 * This class represents the Employee registration screen.
 */
public class UserRegistrationActivity extends AppCompatActivity implements View.OnClickListener, TaskCallback {

    public static final String EMPLOYEE_KEY = "employee";
    public static final int ADD_USER_REQUEST_CODE = 5322;

    private static final int MODE_REGISTRATION = 0;
    private static final int MODE_EDIT = 1;

    private Button mAdd;

    private ProgressDialog mProgress;

    private LinearLayout mEditLy;
    private Button mSave;
    private Button mRemove;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mUserName;
    private EditText mPassword;

    private TextView tPassword;

    private Employee mEmployee = new Employee();
    private User mEmployeeUser = new User();
    private User mManager;

    private int mMode = MODE_REGISTRATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle(null);
        mProgress.setMessage(getString(R.string.add_employee_sending));
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

        if(!StorageUtils.getIsLoggedIn(this) || (mManager = StorageUtils.getUserData(this)) == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.add_employee_isnt_logged_in), Toast.LENGTH_LONG).show();
            finish();
        }

        mAdd = (Button) findViewById(R.id.add_employee_add);
        mAdd.setOnClickListener(this);

        mEditLy = (LinearLayout) findViewById(R.id.add_employee_edit_ly);
        mSave = (Button) findViewById(R.id.add_employee_save);
        mSave.setOnClickListener(this);

        mRemove = (Button) findViewById(R.id.add_employee_remove);
        mRemove.setOnClickListener(this);

        mFirstName = (EditText) findViewById(R.id.add_employee_firstName);
        mLastName = (EditText) findViewById(R.id.add_employee_lastName);
        mEmail = (EditText) findViewById(R.id.add_employee_email);
        mUserName = (EditText) findViewById(R.id.add_employee_username);
        mPassword = (EditText) findViewById(R.id.add_employee_password);

        tPassword = (TextView) findViewById(R.id.add_employee_password_text);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mAdd.setVisibility(View.GONE);
            mPassword.setVisibility(View.GONE);
            tPassword.setVisibility(View.GONE);

            mEditLy.setVisibility(View.VISIBLE);

            loadEmployee((Employee) extras.get(EMPLOYEE_KEY));
            mMode = MODE_EDIT;
        }else{
            mAdd.setVisibility(View.VISIBLE);
            mEditLy.setVisibility(View.GONE);
            mMode = MODE_REGISTRATION;
        }
    }

    /**
     * Loads employee information.
     * @param employee - The employee.
     */
    private void loadEmployee(Employee employee){
        mEmployee = employee;
        mEmployeeUser = employee.getUser();
        mFirstName.setText(mEmployeeUser.getFirstName());
        mLastName.setText(mEmployeeUser.getLastName());
        mEmail.setText(mEmployeeUser.getEmail());
        mUserName.setText(mEmployeeUser.getUsername());
    }

    @Override
    public void onClick(View v) {
        if(v == mAdd){
            if(verifyEmployee())
                registerEmployee();
            return;
        }

        if(v == mSave){
            if(verifyEmployee())
                saveEmployee();
            return;
        }

        if(v == mRemove){
            removeEmployee();
            return;
        }
    }


    /**
     * Verifies if employee information is valid.
     * @return - True (if is valid) or False (otherwise).
     */
    private boolean verifyEmployee(){
        String first_name = mFirstName.getText().toString();
        String last_name = mLastName.getText().toString();
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        if(first_name.length() == 0)
            first_name = "";
        else if(first_name.length() < 3) {
            Toast.makeText(this, getString(R.string.add_employee_invalid_first_name), Toast.LENGTH_LONG).show();
            return false;
        }

        if(last_name.length() == 0)
            last_name = "";
        else if(last_name.length() < 3) {
            Toast.makeText(this, getString(R.string.add_employee_invalid_last_name), Toast.LENGTH_LONG).show();
            return false;
        }

        if(email.length() == 0)
            email = "";
        else if(email.length() < 3 && !email.contains("@") && !email.contains(".")) {
            Toast.makeText(this, getString(R.string.add_employee_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        }

        if(username.length() < 3) {
            Toast.makeText(this, getString(R.string.add_employee_invalid_username), Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.length() != 0 && password.length() < 4) {
            Toast.makeText(this, getString(R.string.add_employee_invalid_password), Toast.LENGTH_LONG).show();
            return false;
        }

        mEmployeeUser.setFirstName(first_name);
        mEmployeeUser.setLastName(last_name);
        mEmployeeUser.setUsername(username);
        mEmployeeUser.setPassword(password);
        mEmployeeUser.setEmail(email);

        return true;
    }

    /**
     * Removes employee.
     */
    private void removeEmployee(){
        final Dialog confirmationDialog = new Dialog(this);
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.dialog_ask);

        ((TextView) confirmationDialog.findViewById(R.id.dialog_ask_message)).setText(getString(R.string.add_employee_confirm_remove));

        Button ok = (Button) confirmationDialog.findViewById(R.id.dialog_ask_ok);
        ok.setText(getString(R.string.add_employee_confirm_yes));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
                if (mProgress != null && !mProgress.isShowing())
                    mProgress.show();

                new RemoveEmployeeTask(UserRegistrationActivity.this).execute(mManager.getToken(), mEmployee.getId());
            }
        });

        Button cancel = (Button) confirmationDialog.findViewById(R.id.dialog_ask_cancel);
        cancel.setText(getString(R.string.add_employee_confirm_no));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }

    /**
     * Creates a new employee.
     */
    private void registerEmployee(){
        if(mProgress != null &&  !mProgress.isShowing())
            mProgress.show();

        new AddEmployeeTask(this).execute(mManager.getToken(), mEmployeeUser);
    }

    /**
     * Saves new employee information.
     */
    private void saveEmployee(){
        if(mProgress != null &&  !mProgress.isShowing())
            mProgress.show();

        new SaveEmployeeTask(this).execute(mManager.getToken(), mEmployee.getId(), mEmployeeUser);
    }

    @Override
    public void onTaskFailed(int errorCode) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        Toast.makeText(this, getString(R.string.add_employee_registration_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskCompleted(Object result) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        if(result instanceof AddEmployeeResult) {
            if(mMode == MODE_REGISTRATION)
                Toast.makeText(this, getString(R.string.add_employee_registration_success), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, getString(R.string.add_employee_successfully_saved), Toast.LENGTH_LONG).show();

            setResult(Activity.RESULT_OK, getIntent());
            finish();
        }

        if(result instanceof Response) {
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        }
    }
}
