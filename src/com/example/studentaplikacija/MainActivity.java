package com.example.studentaplikacija;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapters.StudentListAdapter;

public class MainActivity extends ActionBarActivity {
	
	public static final String ip = "192.168.1.4";
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
	
	private Integer studentId = null;
	
	HashMap<String, String> kliknutStudent;

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
		registerForContextMenu(listaStudenata);
	}


	// Popup meni
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(v.getId() == R.id.listaStudenata) {
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			Object item = listaStudenata.getItemAtPosition(acmi.position);
			if(item instanceof HashMap) {
				HashMap<String, String> itemMap = (HashMap<String, String>) item;
				kliknutStudent = itemMap;
			}
		}
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.popup_menu, menu);

	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.popup_menu_delete_item) {
			if(kliknutStudent != null) {
				delete();
			}
		}else if(item.getItemId() == R.id.popup_menu_update_item) {
			if(kliknutStudent != null) {
				update();
			}
		}
		
		return super.onContextItemSelected(item);
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
	
	public void delete(View view){
		
		Intent intent = new Intent(this,DeleteActivity.class);
		startActivity(intent);
	}

	public void update(){
		String id = kliknutStudent.get(Student_ID);
		String name = kliknutStudent.get(StudentName);
		String index = kliknutStudent.get(IndexNumber);
		String city = kliknutStudent.get(City);
		String address = kliknutStudent.get(Address);
		String jmbg = kliknutStudent.get(JMBG);
		String sex = kliknutStudent.get(Sex);
		String dateStr = kliknutStudent.get(BirthDate);
		
		Intent intent = new Intent(this,UpdateMainActivity.class);
		
		intent.putExtra("UPDATE_ID", id);
		intent.putExtra("UPDATE_NAME", name);
		intent.putExtra("UPDATE_INDEX", index);
		intent.putExtra("UPDATE_CITY", city);
		intent.putExtra("UPDATE_ADDRESS", address);
		intent.putExtra("UPDATE_JMBG", jmbg);
		intent.putExtra("UPDATE_SEX", sex);
		intent.putExtra("UPDATE_DATUM", dateStr);
		
		//Log.i(TAG, "SALJEM: " + id + ", " + name + ", " + index + ", " + city + ", " + address + ", " + jmbg + ", " + sex + ", " + dateStr);
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

	public void delete() {
		studentId = Integer.parseInt(kliknutStudent.get(Student_ID));
		Log.i(TAG, "pozvana delete student "+ studentId);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "delete student");
				SoapObject request = new SoapObject(NAMESPACE, DELETE_STUDENTS);
				PropertyInfo property = new PropertyInfo();
				property.name="studentID";
				property.type=PropertyInfo.INTEGER_CLASS;
				request.addProperty(property, studentId);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
				envelope.implicitTypes = true;
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);
				HttpTransportSE transport = new HttpTransportSE(URL);

				Log.i(MainActivity.TAG, "before call");
				transport.debug = true;
				try {
					transport.call(DELETE_STUDENTS,envelope);
					Log.i(MainActivity.TAG, "after call");
					//listAllStudents();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
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
							//Log.i(TAG, "property" + response.getProperty(i).toString());
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