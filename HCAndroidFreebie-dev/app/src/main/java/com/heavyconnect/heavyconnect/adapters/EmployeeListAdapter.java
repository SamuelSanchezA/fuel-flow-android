package com.heavyconnect.heavyconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heavyconnect.heavyconnect.R;
import com.heavyconnect.heavyconnect.entities.Employee;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the employee list adapter.
 */
public class EmployeeListAdapter extends ArrayAdapter<Employee> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Employee> mEmployees = new ArrayList<Employee>();

    /**
     * Constructor method.
     * @param context - The context.
     * @param objects - List of employees.
     */
    public EmployeeListAdapter(Context context, List<Employee> objects) {
        super(context, R.layout.equip_list_item, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mEmployees = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.equip_list_item, null);

        Employee employee = mEmployees.get(position);

        TextView itemName = (TextView) convertView.findViewById(R.id.equip_list_item_tx);
        String name = employee.getUser().getFirstName();
        if(name == null || name.length() == 0)
            name = employee.getUser().getUsername();

        itemName.setText(name);

        ImageView itemImg = (ImageView) convertView.findViewById(R.id.equip_list_item_img);
        itemImg.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public int getCount(){
        return mEmployees == null? 0 : mEmployees.size();
    }

    @Override
    public void add(Employee employee){
        mEmployees.add(employee);
        notifyDataSetChanged();
    }

    /**
     * Returns current employee at a specific position.
     * @param position - Position.
     * @return - Employee.
     */
    public Employee get(int position){
        return mEmployees.get(position);
    }
}
