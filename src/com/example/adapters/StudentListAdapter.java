package com.example.adapters;

import java.util.ArrayList;
import java.util.HashMap;


import com.example.studentaplikacija.MainActivity;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.studentaplikacija.R;

public class StudentListAdapter extends BaseAdapter {

	private MainActivity activity;
	private ArrayList list;

	public StudentListAdapter(MainActivity activity,ArrayList list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View studentView = convertView;
		if (convertView ==null)
			studentView = activity.getLayoutInflater().inflate(R.layout.textfield,null);

		HashMap<String, String> item =  (HashMap<String, String>) list.get(position);

		TextView txtID = (TextView)studentView.findViewById(R.id.idStudenta);
		txtID.setText(item.get(activity.Student_ID));

		TextView txtIme = (TextView)studentView.findViewById(R.id.ImeStudenta);
		txtIme.setText(item.get(activity.StudentName));

		TextView txtIndexNumber = (TextView)studentView.findViewById(R.id.IndexNumber);
		txtIndexNumber.setText(item.get(activity.IndexNumber));

		TextView txtGrad = (TextView)studentView.findViewById(R.id.Grad);
		txtGrad.setText(item.get(activity.City));

		TextView txtAdresa = (TextView)studentView.findViewById(R.id.Adresa);
		txtAdresa.setText(item.get(activity.Address));

		TextView txtJMBG = (TextView)studentView.findViewById(R.id.JMBG);
		txtJMBG.setText(item.get(activity.JMBG));

		TextView txtSex = (TextView)studentView.findViewById(R.id.Sex);
		txtSex.setText(item.get(activity.Sex));

		TextView txtDatumRodjenja = (TextView)studentView.findViewById(R.id.DatumRodjenja);
		txtDatumRodjenja.setText(item.get(activity.BirthDate));




		return studentView;
	}


}
