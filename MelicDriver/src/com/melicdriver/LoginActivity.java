package com.melicdriver;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	EditText editLoginName;
	EditText editLoginPlate;
	Button identifiedBtn;
	String cabName;
	String cabPlate;
	String serverURL = "http://taxiya.aws.af.cm/cabbie/drivers";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		editLoginName = (EditText)findViewById(R.id.editLoginName);
		editLoginPlate = (EditText)findViewById(R.id.editLogin_idPlate);
		identifiedBtn = (Button)findViewById(R.id.btnRequestCab);
		
		identifiedBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				cabName = editLoginName.getText().toString();
				cabPlate = editLoginPlate.getText().toString();
				
				LongOperation longO = new LongOperation();
				longO.setContextForIt(LoginActivity.this);
				longO.setData(cabName, cabPlate);
				longO.execute(serverURL);
				
			}
		});		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

class LongOperation extends AsyncTask<String, Void, Void>{
	
	private final HttpClient Client = new DefaultHttpClient();
	private String Content;
	private String Error;
	private Context internalCtx;
	ProgressDialog pDialog;
	
	String Name;
	String Plate;
	
	public void setContextForIt(Context ctx){
		internalCtx = ctx;
	}
	
	public void setData(String d1, String d2){
		Name = d1;
		Plate = d2;
	}
	
	protected void onPreExecute(){
		
		pDialog = new ProgressDialog(internalCtx);
		pDialog.setMessage("Identificando...");
		pDialog.show();
	}

	@Override
	protected Void doInBackground(String... urls) {
		
		try{
			HttpGet httpGet = new HttpGet(urls[0]);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			Content = Client.execute(httpGet, responseHandler);
		
		}catch(ClientProtocolException e){
			Error = e.getMessage().toString();
			cancel(true);
		}catch(IOException e){
			Error = e.getMessage().toString();
			cancel(true);
		}
		
		return null;
	}
	
	protected void onPostExecute(Void unused){
		
		pDialog.dismiss();
		if(Error != null){
			Toast.makeText(internalCtx, "Hubo un error en la validacion", Toast.LENGTH_LONG).show();
		}else{
			
			if(Plate.equals("") || !Content.contains(Plate)){
				Toast.makeText(internalCtx, "No Existes en la Base de Datos", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(internalCtx, "Identificado", Toast.LENGTH_SHORT).show();
				Intent theIntent = new Intent(internalCtx, ThreadActivity.class);
				internalCtx.startActivity(theIntent);
				
			
			}
			
			//Toast.makeText(internalCtx, Content, Toast.LENGTH_LONG).show();
			
			/*try{
				
				JSONArray jsonArray = new JSONArray(Content);
				
				int n = jsonArray.length();
				
				for(int i = 0; i< n; i++){
					
					JSONObject jo = jsonArray.getJSONObject(i);
					
					JSONArray fields = jo.getJSONArray("fields");
					
					for(int j = 0; j < fields.length(); j++){
						
						JSONObject joInside = fields.getJSONObject(j);
						
						String nid = joInside.getString("nid");
						if(nid.equals(Plate)){
							Toast.makeText(internalCtx, "Indentificado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(internalCtx, "Fallo", Toast.LENGTH_SHORT).show();
						}
						
						
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}*/
			
			
		}
	}
	
}
