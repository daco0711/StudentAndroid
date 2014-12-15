package com.example.studentaplikacija;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteActivity extends ActionBarActivity {
	
	
	public Integer studentId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete, menu);
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
	public void deleteStudent(View view){
		EditText txtDelete = (EditText) (findViewById(R.id.idEditTextDelete));
		
		final String strID = txtDelete.getText().toString();
		if (strID.equals("")) {
			Toast.makeText(this, "Morate uneti ID!", Toast.LENGTH_LONG).show();
			return;
		} else {
			try {
				 studentId = Integer.parseInt(strID);
			} catch (NumberFormatException ex) {
				Toast.makeText(DeleteActivity.this,
						"ID mora biti broj!", Toast.LENGTH_LONG).show();
				return;
			}
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Log.i(MainActivity.TAG, "deleteStudent(" + studentId + ")");
					SoapObject request = new SoapObject(MainActivity.NAMESPACE,
							MainActivity.DELETE_STUDENTS);
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
						transport.call(MainActivity.DELETE_STUDENT_ACTION,
								envelope);
						Log.i(MainActivity.TAG, "after call");
						SoapObject student = (SoapObject) envelope
								.getResponse();
						if (student != null || student.getPropertyCount() > 0) {
							
							MainActivity.fillHashMap(studentList, student);
						} else {
							Log.i(MainActivity.TAG, "Naslo nista");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(DeleteActivity.this,
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
			txtDelete.setText("");
		}
	}
}
					
		
	

