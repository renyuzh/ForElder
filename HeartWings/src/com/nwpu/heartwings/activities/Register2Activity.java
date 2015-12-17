package com.nwpu.heartwings.activities;

import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.DialogUtil;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register2Activity extends ActionBarActivity {

	private EditText codeEditText;
	private Button submitBtn;
	
	public static final String INTENT_PHONE = "com.nwpu.heartwings.activities:2:phone";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
        getSupportActionBar().setHomeButtonEnabled(true);
        
        codeEditText = (EditText)findViewById(R.id.input_msg_code);
        submitBtn = (Button)findViewById(R.id.send_msg_code);
        
        final String phone = getIntent().getStringExtra(RegisterActivity.INTENT_MSG_PHONE);
        final String code = getIntent().getStringExtra(RegisterActivity.INTENT_MSG_CODE);
        
        submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				   if(TextUtils.isEmpty(codeEditText.getText()))
				   {
					   return;
				   }
				   if(!CheckNetWork.isNetAvailable(Register2Activity.this.getBaseContext())){
					   
					   Toast.makeText(Register2Activity.this, CONSTANTS.NETWORKERR, Toast.LENGTH_SHORT).show();
					   return;
				   }
				   
				   if(codeEditText.getText().toString().equals(code)){
					   
					    Intent intent = new Intent(Register2Activity.this,Register3Activity.class);
					   intent.putExtra(INTENT_PHONE, phone);
					   startActivity(intent);
					     
				   }
				   else {
					  
					   DialogUtil.showDialog(Register2Activity.this.getBaseContext(), "ÑéÖ¤Âë´íÎó");
					   
				}
			}
		});
		
	}


}
