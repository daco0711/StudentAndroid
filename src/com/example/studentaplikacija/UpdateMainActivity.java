package com.example.studentaplikacija;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMainActivity extends ActionBarActivity {

	public static String UPDATE_STUDENTS = "updateStudent";
	public static String UPDATE_STUDENT_ACTION = "StudentService/updateStudent";

	private Integer studentID = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_main, menu);
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

	public void updateStudent(View view) {
		
		

		TextView txtID = (TextView) findViewById(R.id.txtIDupdate);
		final String iD = txtID.getText().toString();
		EditText txtName = (EditText) findViewById(R.id.txtImeStudentaupdate);
		final String studentName = txtName.getText().toString();
		EditText txtJMBG = (EditText) findViewById(R.id.txtJMBG);
		final String jmbg = txtJMBG.getText().toString();
		EditText txtBrIndexa = (EditText) findViewById(R.id.txtbrIndexa);
		final String brojIndexa = txtBrIndexa.getText().toString();
		EditText txtCity = (EditText) findViewById(R.id.txtGrad);
		final String city = txtCity.getText().toString();
		EditText txtAddress = (EditText) findViewById(R.id.txtAdresa);
		final String address = txtAddress.getText().toString();
		EditText txtSex = (EditText) findViewById(R.id.txtSex);
		final String sex = txtSex.getText().toString();
		EditText txtDatum = (EditText) findViewById(R.id.txtDatum);
		final String datum = txtDatum.getText().toString();

		if (iD.equals("")) {
			Toast.makeText(this, "Morate uneti ID!", Toast.LENGTH_LONG).show();
			return;
		} else {
			try {
				studentID = Integer.parseInt(iD);
			}

			catch (NumberFormatException ex) {
				Toast.makeText(UpdateMainActivity.this, "ID mora biti broj!",
						Toast.LENGTH_LONG).show();
				return;
			}
			
			
			
			try {

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						Log.i(MainActivity.TAG, "Update studenta");
						SoapObject request = new SoapObject(
								MainActivity.NAMESPACE, UPDATE_STUDENTS);
						PropertyInfo propID = new PropertyInfo();
						propID.name = "studentID";
						propID.type = PropertyInfo.INTEGER_CLASS;
						request.addProperty(propID, iD);
						
						PropertyInfo propName = new PropertyInfo();
						propName.name = "studentName";
						propName.type = PropertyInfo.STRING_CLASS;
						request.addProperty(propName, studentName);
						
						PropertyInfo propIndexNum = new PropertyInfo();
						propIndexNum.name = "IndexNumber";
						request.addProperty(propIndexNum, brojIndexa);
						
						PropertyInfo propCity = new PropertyInfo();
						propCity.name = "City";
						propCity.type = PropertyInfo.STRING_CLASS;
						request.addProperty(propCity, city);
						
						PropertyInfo propAddress = new PropertyInfo();
						propAddress.name = "address";
						propAddress.type = PropertyInfo.STRING_CLASS;
						request.addProperty(propAddress, address);
						
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
						
						
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date();
						try {
							date = format.parse(datum);
							
						}catch (Exception e) {
						e.printStackTrace();
						}
						request.addProperty(propDatum, format.format(date));
						
						System.out.println("request: " + request.toString());
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
						envelope.implicitTypes = true;
						envelope.dotNet = true;
						envelope.encodingStyle = SoapSerializationEnvelope.XSD;
						envelope.setOutputSoapObject(request);
						HttpTransportSE transport = new HttpTransportSE(MainActivity.URL);
						Log.i(MainActivity.TAG, "Pozivam WCF servis");
						
						try {
							Log.i(MainActivity.TAG, "before call");
							transport.debug = true;
							transport.call(UPDATE_STUDENT_ACTION, envelope);
							Log.i(MainActivity.TAG, "after call add");
							
						}  catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Intent intent = new Intent(UpdateMainActivity.this,
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

		}

	}
}
