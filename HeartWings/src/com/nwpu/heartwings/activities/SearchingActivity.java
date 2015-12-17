package com.nwpu.heartwings.activities;

import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.OldmanBean;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.TextUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

@SuppressLint("HandlerLeak")
public class SearchingActivity extends ActionBarActivity {

	public static final int HANDLER_WHAT = 0x21;
	
	private EditText searchingText;
	private Button searchingBtn;
	
	private ProgressDialog progressDialog;
	
	
	private static OldmanBean mOldManBean = null;
	


	
	
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searching);

		layout = (LinearLayout)findViewById(R.id.showSearchingResult);
		
		
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		searchingText = (EditText)findViewById(R.id.binding_searchtext_search);
		
		searchingBtn = (Button)findViewById(R.id.searching_bind);
		
		
		progressDialog = new ProgressDialog(this);
		
		progressDialog.setTitle(null);
		progressDialog.setMessage("正在搜索中...");
		
		
		final Handler myHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				   if(msg.what == HANDLER_WHAT){
					     
					      try {
							
					    	  Thread.sleep(1250);
						} catch (InterruptedException e) {
							  e.printStackTrace();
						}
					      
					      progressDialog.dismiss();
					     if(!msg.obj.toString().equals(CONSTANTS.LOGIN_ERR_TAG))
					     {
					    	  OldmanBean oldmanBean = JSON.parseObject(msg.obj.toString(), OldmanBean.class);
					    	  
					    	  if(!oldmanBean.getPhone().equals(""))
					    	  {
					    		  mOldManBean = oldmanBean;
					    		  
					    		  View view =  LayoutInflater.from(SearchingActivity.this).inflate(R.layout.h_searching_results, null);
					    		  
					    		  TextView textView = (TextView)view.findViewById(R.id.display_result);
					    		  
					    		  
					    		  textView.setText(mOldManBean.getName() + ":" + mOldManBean.getPhone());
				
					    		  layout.addView(view);
					    		  
					    		  searchingBtn.setText("绑定");
					    		 
					    		  
					    	  }
					    	  else {
								
					    		  DialogUtil.showDialog(SearchingActivity.this, "该用户不存在或无法绑定");
					    		
							}
					    	  
					     }
					     else {
							 
					    	     DialogUtil.showDialog(SearchingActivity.this, "网络故障，请稍后重试");
						}
					     
				   }
			}
			
			
		};

        
		searchingBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				  
				if(searchingBtn.getText().equals("绑定"))
				{
					
					  System.out.println(MSGUtil.session.isConnected() + " is session connected...");
					  
					   new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							String thisClient = LocalFileUtil.getThisClient(SearchingActivity.this);
							
							  if(!MSGUtil.session.isConnected())
							  {
								  MSGUtil.session = MSGUtil.getIOSession(thisClient);
							  }
							
							 HeartMsg heartMsg = new HeartMsg();
							 heartMsg.setCreateTime(System.currentTimeMillis());
							 heartMsg.setFromUserName(thisClient);
							 heartMsg.setMsgType(MSGUtil.TYPE_BIND);
							 heartMsg.setMsgContent("");
							 heartMsg.setToUserName(mOldManBean.getPhone());
							
							 MSGUtil.session.write(JSON.toJSONString(heartMsg));
							 
							 MSGUtil.session.getCloseFuture().awaitUninterruptibly();
						}
					}).start();
				}
				
				else {
					
					  if(TextUtils.isEmpty(searchingText.getText()))
					  {
						  DialogUtil.showDialog(SearchingActivity.this, "输入不能为空");
						  return;
					  }
					
					  if( searchingText.getText().toString().length() < 11)
					  {
						  DialogUtil.showDialog(SearchingActivity.this, "请输入11位电话号码");
						  return;
					  }
					  final String searchPhone = searchingText.getText().toString();
					  
					  progressDialog.show();
					  new Thread(new Runnable() {
						
						@Override
						public void run() {
							 
							  Map<String, String> rawParams = new HashMap<>();
							  rawParams.put("searchingPhone", searchPhone);
							  
							  String result = HttpUtil.postRequest(HttpUtil.BASEURL + "searchingUnBind", rawParams);
							  
							  Message message = new Message();
							  message.what = HANDLER_WHAT;
							  message.obj = result;
							  myHandler.sendMessage(message);
							  
							  System.out.println(result + "  return result");
						}
					}).start();
				}
			
			}
		});

		
	}
	

}
