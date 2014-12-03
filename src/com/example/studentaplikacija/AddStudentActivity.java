package com.example.studentaplikacija;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AddStudentActivity extends ActionBarActivity {

	public static final String Student_ID = "StudentId";
	public static final String StudentName = "StudentName";
	public static final String IndexNumber = "IndexNumber";
	public static final String City = "City";
	public static final String Address = "Address";
	public static final String JMBG = "Jmbg";
	public static final String Sex = "Sex";
	public static final String BirthDate = "BirthDate";
	static String ADD_STUDENTS ="addStudent";
	static String ADD_STUDENT_ACTION = "StudentService/AddStudent";
	static String TAG = "AddStudentActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_student);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_student, menu);
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



	public void addStudent (View view) {
		EditText txtName= (EditText) findViewById(R.id.txtImeStudenta);
		final String name = txtName.getText().toString();
		EditText txtJmbg = (EditText) findViewById(R.id.txtJMBG);
		final String jmbg = txtJmbg.getText().toString();
		EditText txtGrad = (EditText) findViewById(R.id.txtGrad);
		final String grad = txtGrad.getText().toString();
		EditText txtAddress = (EditText) findViewById(R.id.txtAdresa);
		final String adresa = txtAddress.getText().toString();
		EditText txtSex = (EditText) findViewById(R.id.txtSex);
		final String sex = txtSex.getText().toString();
		EditText txtDatum = (EditText) findViewById(R.id.txtDatum);
		final String datum = txtDatum.getText().toString();
		EditText txtBrIndexa = (EditText) findViewById(R.id.txtbrIndexa);
		final String brIndexa = txtBrIndexa.getText().toString();

		try {
			Thread t = new Thread(new  Runnable() {
				public void run(){
					Log.i(TAG, "Unesi studenta");
					SoapObject request = new SoapObject(MainActivity.NAMESPACE, ADD_STUDENTS);
					PropertyInfo propName = new PropertyInfo();
					propName.name= "studentName";
					propName.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propName, name);

					PropertyInfo propIndex = new PropertyInfo();
					propIndex.name = "IndexNumber";
					propIndex.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propIndex, brIndexa);

					PropertyInfo propCity = new PropertyInfo();
					propCity.name = "City";
					propCity.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propCity, grad);

					PropertyInfo propAdress = new PropertyInfo();
					propAdress.name = "address";
					propAdress.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propAdress, adresa);

					PropertyInfo propJMBG = new PropertyInfo();
					propJMBG.name = "jmbg";
					propJMBG.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propJMBG, jmbg);

					PropertyInfo propSex = new PropertyInfo();
					propSex.name = "sex";
					propSex.type = PropertyInfo.STRING_CLASS;
					request.addProperty(propSex, sex);

					PropertyInfo propDatum = new PropertyInfo();
					propDatum.name = "datumRodj";
					propDatum.type = PropertyInfo.STRING_CLASS;

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					try {
						date = formatter.parse(datum);
					} catch (ParseException e1) {
						// TODO Izbaci poruku o gresci!
						e1.printStackTrace();
					}
					request.addProperty(propDatum, formatter.format(date));

					System.out.println("request: " + request.toString());
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER10);
					envelope.implicitTypes = true;
					envelope.dotNet = true;
					envelope.encodingStyle = SoapSerializationEnvelope.XSD;
					envelope.setOutputSoapObject(request);
					HttpTransportSE transport = new HttpTransportSE(
							MainActivity.URL);

					Log.i(TAG, "Pozivam WCF.... ");

					try {
						Log.i(TAG, "before call");
						transport.debug = true;
						transport.call(ADD_STUDENT_ACTION, envelope);
						Log.i(TAG, "after call add");

					}  catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Intent intent = new Intent(AddStudentActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
			});
			t.start();
			try {
				t.join();

			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		txtName.setText("");
		txtAddress.setText("");
		txtBrIndexa.setText("");
		txtDatum.setText("");
		txtGrad.setText("");
		txtJmbg.setText("");
		txtSex.setText("");

	}

	protected void sleep(int i) {
		// TODO Auto-generated method stub
	}

	public void dugmeOK(View view){
		//TODO napraviti metodu za pokretanje dugmeta OK u activity_add_student.xml
	}
}
