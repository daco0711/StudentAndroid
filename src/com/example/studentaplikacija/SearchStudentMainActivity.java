package com.example.studentaplikacija;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchStudentMainActivity extends ActionBarActivity {
	
	private Integer studentId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_student_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_student_main, menu);
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

	public void searchStudent(View view) {
		EditText txtSearch = (EditText) findViewById(R.id.txtID);
		
		final String strID = txtSearch.getText().toString();
		if (strID.equals("")) {
			Toast.makeText(this, "Morate uneti ID!", Toast.LENGTH_LONG).show();
			return;
		} else {
			try {
				 studentId = Integer.parseInt(strID);
			} catch (NumberFormatException ex) {
				Toast.makeText(SearchStudentMainActivity.this,
						"ID mora biti broj!", Toast.LENGTH_LONG).show();
				return;
			}
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.i(MainActivity.TAG, "getStudentById(" + studentId + ")");
					SoapObject request = new SoapObject(MainActivity.NAMESPACE,
							MainActivity.GET_STUDENT_BY_ID);
					PropertyInfo prop = new PropertyInfo();
					prop.name = "studentID";
					prop.type = PropertyInfo.INTEGER_CLASS;
					request.addProperty(prop, studentId);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER10);
					envelope.implicitTypes = true;
					envelope.dotNet = true;
					envelope.encodingStyle = SoapSerializationEnvelope.XSD;
					envelope.setOutputSoapObject(request);
					HttpTransportSE transport = new HttpTransportSE(
							MainActivity.URL);

					HashMap<String, String> map;
					ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
					Log.i(MainActivity.TAG, "Pozivam WCF...");
					try {
						Log.i(MainActivity.TAG, "before call");
						transport.debug = true;
						transport.call(MainActivity.GET_STUDENT_BY_ID_ACTION,
								envelope);
						Log.i(MainActivity.TAG, "after call");
						SoapObject student = (SoapObject) envelope
								.getResponse();
						if (student != null || student.getPropertyCount() > 0) {
							
							MainActivity.fillHashMap(studentList, student);
						} else {
							Log.i(MainActivity.TAG, "Naslo kiturinu.");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent intent = new Intent(SearchStudentMainActivity.this,
							MainActivity.class);
					intent.putExtra(MainActivity.SEARCH_RESULT, studentList);
					startActivity(intent);
				}
			});
			t.start();
			try {
				t.join();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			txtSearch.setText("");
		}
	}
}
