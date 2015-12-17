package com.nwpu.heartwings.share;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.heart.bean.CodeResult;
import com.heart.bean.HeartMsg;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.util.ChooseDialogUtil;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;;

public class ImageShowActivity extends ActionBarActivity {

	public static final int REQUEST_CODE = 1;
	public static final int HANLDER_WHAT = 0x101;
	
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	
	private HttpClient httpClient;
	
	private ProgressDialog mProgressDialog;
	
	private UploadHandler mHandler;
	
	private HandlerThread handlerThread;
	
	private boolean isChoose = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_image_show);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		getSupportActionBar().setHomeButtonEnabled(true);
		
		httpClient = new DefaultHttpClient();
		
		
		 mProgressDialog = new ProgressDialog(this);
		 mProgressDialog.setTitle(null);
		 mProgressDialog.setMessage("正在上传中...");
		 
		 
		 
		 handlerThread = new HandlerThread("upload");
		 handlerThread.start();
		 
		 mHandler = new UploadHandler(mProgressDialog, handlerThread.getLooper());
		 
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
		
		adapter = new ChildAdapter(this, list, mGridView);
		
		mGridView.setAdapter(adapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.child_checkbox);
				
				if(checkBox.isChecked())
				{
					checkBox.setChecked(false);
					
				}
				
				else {
					checkBox.setChecked(true);
					
				}
			}
			
		}); 
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.share_upload, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if(item.getItemId() == R.id.action_sendpic)
		{
			
           
			
			if(adapter.getSelectItems().size() == 0){
				DialogUtil.showDialog(this, "请至少选择一张图片");
				return true;
			}
			
			if(!isChoose){
				
				  String selectd = ChooseDialogUtil.showChoose(this);
			
				  System.out.println("selected .." + selectd);
				  if(selectd == null) 
				  {
					  
					  DialogUtil.showDialog(this, "请先前往设置页面绑定老人");
					  return true;
				  }
				  isChoose = true;
				return true;
			}
			
			
			mProgressDialog.show();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					  
					final HashSet<String> selectPath = adapter.getSelectedPath();
					
					HttpPost post = new HttpPost(HttpUtil.BASEURL + "uploadFile");
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					
					for(String s : selectPath)
					{
						File file = new File(s);
						ContentBody contentBody = new FileBody(file);
						builder.addPart("file"+System.currentTimeMillis(), contentBody);
					}
					
					HttpEntity httpEntity = builder.build();
					
					post.setHeader("enctype", "multipart/form-data");
					post.setEntity(httpEntity);
					
					try {
						HttpResponse httpResponse = httpClient.execute(post);
						HttpEntity responseEntity = httpResponse.getEntity();
					
						Message msg = new Message();
						msg.what = HANLDER_WHAT;
						
						msg.obj = EntityUtils.toString(responseEntity);
						
						mHandler.sendMessage(msg);
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
			}).start();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    

	static class UploadHandler extends Handler{
		       
		     private ProgressDialog progressDialog;
			public UploadHandler(ProgressDialog progressDialog,Looper looper) {
				super(looper);
				this.progressDialog = progressDialog;
			}
			@Override
			public void handleMessage(Message msg) {
				    if(msg.what == HANLDER_WHAT){
				    	   
				    	     progressDialog.dismiss();
				    	     
				    	     List<CodeResult> codeResults = JSONArray.parseArray(msg.obj.toString(), CodeResult.class);
				    	     
				    	     long date = System.currentTimeMillis();
				    	     
				    	     ArrayList<String> paths = new ArrayList<>();
				    	     for(CodeResult codeResult : codeResults)
				    	     {
				    	         if(codeResult.getValidValue() == 0)
					    	     {
					    	         paths.add(codeResult.getInfo());
					    	     }
				    	     }
				    	     
				    	 	 
			    	    	 String thisClient = LocalFileUtil.getThisClient(HeartApp.getInstance());
			    	    	 
			   
			    	    	 
			    	    	 HeartMsg heartMsg = new HeartMsg();
			    	    	 heartMsg.setCreateTime(date);
			    	    	 heartMsg.setFromUserName(thisClient);
			    	    	 System.out.println("this Client.... " + thisClient);
			    	    	 heartMsg.setToUserName(ChooseDialogUtil.selectdPhone);
			    	    	 System.out.println("selected phone " + ChooseDialogUtil.selectdPhone);
			    	    	 heartMsg.setMsgType(MSGUtil.TYPE_SHARE);
			    	    	 heartMsg.setMsgContent(JSONArray.toJSONString(paths));
			    	    	 
			    	    	 System.out.println("img path : " + JSONArray.toJSONString(paths));
			    	    	 
			     	    	 if(!MSGUtil.session.isConnected())
			    	    	 {
			    	    		 MSGUtil.session = MSGUtil.getIOSession(thisClient);
			    	    		 
			    	    	 }
			    	    	 MSGUtil.session.write(JSON.toJSONString(heartMsg));
			    	    	 
			    	    	  MSGUtil.session.getCloseFuture().awaitUninterruptibly();
			    	    	  
			    	    	 System.out.println("ok...");
				    	   
				    	
				    }
			}
		     
		    
	}
}
