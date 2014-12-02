package com.example.studentaplikacija;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.StudentListAdapter;

public class MainActivity extends ActionBarActivity {

	public static final String ip = "192.168.1.5";
	public static final int port = 80;

	public static final String Student_ID = "StudentId";
	public static final String StudentName = "StudentName";
	public static final String IndexNumber = "IndexNumber";
	public static final String City = "City";
	public static final String Address = "Address";
	public static final String JMBG = "Jmbg";
	public static final String Sex = "Sex";
	public static final String BirthDate = "BirthDate";
	public static String NAMESPACE = "WcfServiceStudent";
	public static String GET_STUDENTS = "getStudents";
	public static String GET_STUDENT_BY_ID = "getStudentById";
	public static String GET_STUDENTS_ACTION = "StudentService/GetStudents";
	public static String GET_STUDENT_BY_ID_ACTION = "StudentService/GetStudentById";
	public static String DELETE_STUDENTS = "deleteStudent";
	public static String DELETE_STUDENT_ACTION = "StudentService/deleteStudent";
	public static String URL = "http://" + ip + ":" + port
			+ "/WcfServiceStudent/Service1.svc";
	public static String TAG = "MainActivity";
	ListView listaStudenata;
	ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
	// private StudentListAdapter adapter;
	private HashMap<String, String> map;
	private StudentListAdapter adapter;
	public static String SEARCH_RESULT = "SEARCH_RESULT";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listaStudenata = (ListView) findViewById(R.id.listaStudenata);
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(SEARCH_RESULT)) {
			studentList = (ArrayList<HashMap<String, String>>) getIntent()
					.getExtras().get(SEARCH_RESULT);
		} else {
			listAllStudents();
		}
		adapter = new StudentListAdapter(this, studentList);
		listaStudenata.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void DugmeUpdate(View view){
		Intent intent = new Intent(this,UpdateMainActivity.class);
		startActivity(intent);
	}

	public void PokreniDugme(View view) {
		Log.i(TAG, "CLICK");
		Intent intent = new Intent(this, AddStudentActivity.class);
		startActivity(intent);
	}

	public void PokreniDugmeSearch(View view) {
		Log.i(TAG, "Click");
		Intent intent = new Intent(this, SearchStudentMainActivity.class);
		startActivity(intent);
	}

	public void Delete(View view) {
		// TODO napraviti metodu koja brise studenta iz baze
		RelativeLayout layout = (RelativeLayout) findViewById(R.layout.textfield);
		for (int i = 0; i < layout.getChildCount(); i++) {
	        View v = layout.getChildAt(i);
	        if (v instanceof CheckBox) {
	        	if(v.isSelected()){
	    			Log.i(TAG, "selected");
	    			}
	        }
	    }
	
	
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
		EditText txtId = (EditText) findViewById(R.id.idStudenta);
		Log.i(TAG, "pozvana delete student "+ txtId.getText().toString());
		if(cb.isSelected()){
			Log.i(TAG, "selected");
			//TextView txtId = (TextView) findViewById(R.id.idStudenta);
			String strID = txtId.getText().toString();
			final Integer id = Integer.parseInt(strID);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.i(TAG, "delete student");
					SoapObject request = new SoapObject(NAMESPACE, DELETE_STUDENTS);
					PropertyInfo property = new PropertyInfo();
					property.name="studentID";
					property.type=PropertyInfo.INTEGER_CLASS;
					request.addProperty(property, id);
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER10);
					envelope.implicitTypes = true;
					envelope.dotNet = true;
					envelope.encodingStyle = SoapSerializationEnvelope.XSD;
					envelope.setOutputSoapObject(request);
					HttpTransportSE transport = new HttpTransportSE(
							MainActivity.URL);
					
					Log.i(MainActivity.TAG, "before call");
					transport.debug = true;
					try {
						transport.call(DELETE_STUDENTS,envelope);
						Log.i(MainActivity.TAG, "after call");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void listAllStudents() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "RUN");
				SoapObject request = new SoapObject(NAMESPACE, GET_STUDENTS);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER10);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE transport = new HttpTransportSE(URL);
				Log.i(TAG, "Pozivam WCF... [" + URL + "]");
				try {
					Log.i(TAG, "before call");
					transport.debug = true;
					transport.call(GET_STUDENTS_ACTION, envelope);
					Log.i(TAG, "after call");
					SoapObject response = (SoapObject) envelope.getResponse();
					if (response != null && response.getPropertyCount() > 0) {
						Log.i(TAG,
								"broj studenata u bazi:"
										+ response.getPropertyCount());

						for (int i = 0; i < response.getPropertyCount(); i++) {
							Log.i(TAG, "napuni listu");
							Log.i(TAG, "property"
									+ response.getProperty(i).toString());
							SoapObject returned = ((SoapObject) response
									.getProperty(i));
							fillHashMap(studentList, returned);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();

				} catch (XmlPullParserException e) {
					Log.i(TAG, "XML pull");
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void fillHashMap(
			ArrayList<HashMap<String, String>> studentList, SoapObject returned) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Student_ID,
				returned.getProperty(Student_ID).toString().split(";")[0]);
		map.put(StudentName, returned.getProperty(StudentName).toString()
				.split(";")[0]);
		map.put(IndexNumber, returned.getProperty(IndexNumber).toString()
				.split(";")[0]);
		map.put(City, returned.getProperty(City).toString().split(";")[0]);
		map.put(Address, returned.getProperty(Address).toString().split(";")[0]);
		map.put(JMBG, returned.getProperty(JMBG).toString().split(";")[0]);
		map.put(Sex, returned.getProperty(Sex).toString().split(";")[0]);
		map.put(BirthDate, returned.getProperty(BirthDate).toString()
				.split(";")[0]);
		studentList.add(map);
	}
}