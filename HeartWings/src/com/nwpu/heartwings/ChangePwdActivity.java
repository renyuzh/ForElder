package com.nwpu.heartwings;


import java.util.HashMap;

import com.nwpu.heartwings.activities.LoginActivity;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.tencent.tencentmap.mapsdk.a.f;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;


@SuppressLint("HandlerLeak")
public class ChangePwdActivity extends ActionBarActivity  implements OnClickListener{

	int currentPage = 0;
	
	public static final String FRAGMENT_PAGE = "fragment_page";
	
	private ProgressDialog mpDialog;
	
	private String receive_code;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 0x88){
				
				mpDialog.dismiss();
				System.out.println(msg.obj.toString() + " return code");
				 if(!msg.obj.toString().equals("error"))
				 {
				
					 receive_code = msg.obj.toString();
					 
					 ChangePwdFragment fragment =
							 new ChangePwdFragment();
					 currentPage = 1;
					 
					 Bundle arguments = new Bundle();
						arguments.putInt(FRAGMENT_PAGE, currentPage);
						
						fragment.setArguments(arguments);
					 
						getSupportFragmentManager().beginTransaction()
						     .replace(R.id.container, fragment).commit();
						
				 }
				 else {
					
					 DialogUtil.showDialog(ChangePwdActivity.this, "密码不正确，请重新输入");
				}
			}
			
			
			if(msg.what == 0x89){
				
				if(!msg.obj.toString().equals("0"))
				{
					DialogUtil.showDialog(ChangePwdActivity.this, "密码修改完成");
					
					startActivity(new Intent(ChangePwdActivity.this,LoginActivity.class));
					
				}else {
					DialogUtil.showDialog(ChangePwdActivity.this, "系统出错，请稍后再试");
				}
			}
		}
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		mpDialog = new ProgressDialog(this);
		mpDialog.setTitle(null);
		
		if (savedInstanceState == null) {
			
			ChangePwdFragment fragment = new ChangePwdFragment();
			
			Bundle arguments = new Bundle();
			arguments.putInt(FRAGMENT_PAGE, currentPage);
			fragment.setArguments(arguments);
			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		
		
	}


	public static class ChangePwdFragment extends Fragment{

		int page = 0;
		
		public ChangePwdFragment() {
			
		}

		@Override
		public void onCreate(Bundle savedInstanceState){
			   
			super.onCreate(savedInstanceState);
			

		    if(getArguments().containsKey(FRAGMENT_PAGE))
		    {
		    	page = getArguments().getInt(FRAGMENT_PAGE);
		    	
		    	System.out.println("page ... " + page);
		    	
		    }
		}
       
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			View rootView = null;

		    switch (page) {
			case 0:
				rootView = inflater.inflate(R.layout.fragment_change_pwd,
						container, false);
				
				rootView.findViewById(R.id.changePwd_get_code).setOnClickListener((OnClickListener) getActivity());
		
            break;
               
			case 1:
			    
				rootView = inflater.inflate(R.layout.change_2pwd, container,false);
				
				rootView.findViewById(R.id.submit_change_code).setOnClickListener((OnClickListener)getActivity());
				
			break;
			 
			  
			   
			case 2:
				rootView = inflater.inflate(R.layout.change_3pwd, container,false);
				
				rootView.findViewById(R.id.confrimSubmitBtn).setOnClickListener((OnClickListener)getActivity());
				
				
			break;
			
			default:
				break;
			}
			 
			return rootView;
		}


	}


	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.changePwd_get_code:
			  
			EditText oldpwdEditText =
			     (EditText)findViewById(R.id.oldpwd);
			
			if(TextUtils.isEmpty(oldpwdEditText.getText()))
			{
				DialogUtil.showDialog(this, "请输入旧密码");
				return;
			}
			if(!CheckNetWork.isNetAvailable(this))
			{
				DialogUtil.showDialog(this,CONSTANTS.NETWORKERR);
				return;
			}
			

			final String oldPwd = oldpwdEditText.getText().toString();
			
			mpDialog.setMessage("正在核实旧密码...");
			mpDialog.show();
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					HashMap<String, String> rawParams = new HashMap<>();
					rawParams.put("phone", LocalFileUtil.getThisClient(ChangePwdActivity.this));
					rawParams.put("pwd", oldPwd);
					
					String result = HttpUtil.postRequest(HttpUtil.BASEURL + "checkpwd", rawParams);
					
					Message msg = new Message();
					msg.what = 0x88;
					msg.obj = result;
					
					handler.sendMessage(msg);
					
				}
			}).start();
			break;

		case R.id.submit_change_code:
			
			EditText inputCodeEditText = (EditText)findViewById(R.id.input_change_code);
			if(TextUtils.isEmpty(inputCodeEditText.getText()))
			{
				DialogUtil.showDialog(this, "验证码不能为空");
			    return;
			}
			if(!CheckNetWork.isNetAvailable(this))
			{
				DialogUtil.showDialog(this, CONSTANTS.NETWORKERR);
				return;
			}
		
			if(receive_code.equals(inputCodeEditText.getText().toString()))
			{
				 ChangePwdFragment fragment =
						 new ChangePwdFragment();
				 currentPage = 2;
				 
				 Bundle arguments = new Bundle();
					arguments.putInt(FRAGMENT_PAGE, currentPage);
					
					fragment.setArguments(arguments);
				 
					getSupportFragmentManager().beginTransaction()
					     .replace(R.id.container, fragment).commit();
			}else {
				
				DialogUtil.showDialog(this, "验证码不正确");
			    return;
			}
			break;
		case R.id.confrimSubmitBtn:
			
			EditText changeNewPwdEditText = (EditText)findViewById(R.id.change_new_pwd);
			EditText changeNewPwdAaginText = (EditText)findViewById(R.id.change_new_pwd_again);
			
			if(TextUtils.isEmpty(changeNewPwdEditText.getText()) 
					|| TextUtils.isEmpty(changeNewPwdAaginText.getText()))
			{
				DialogUtil.showDialog(this, "请正确完成输入");
				
				return;
			}
			
			final String pwd = changeNewPwdEditText.getText().toString();
			final String pwdAgain = changeNewPwdAaginText.getText().toString();
			
			if(pwd.length() < 6 || pwdAgain.length() < 6)
			{
				DialogUtil.showDialog(this, "密码需6位字符以上");
				return;
			}
			
			if(!pwd.equals(pwdAgain)){
				
				DialogUtil.showDialog(this,"两次密码不一致");
				return;
			}
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					HashMap<String, String> rawParams = new HashMap<>();
					rawParams.put("phone", LocalFileUtil.getThisClient(ChangePwdActivity.this));
					rawParams.put("newPwd", pwd);
					
					String result = HttpUtil.postRequest(HttpUtil.BASEURL + "changePwd", rawParams);
					
					Message msg = new Message();
					msg.what = 0x89;
					msg.obj = result;
					
					handler.sendMessage(msg);
					
				}
			}).start();
			
			break;
		default:
			break;
		}
		
	}
	
	

}
