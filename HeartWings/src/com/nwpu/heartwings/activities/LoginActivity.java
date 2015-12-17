package com.nwpu.heartwings.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.heart.bean.OldmanBean;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import com.nwpu.heartwings.util.UpdateSharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends ActionBarActivity {

	private Button loginButton;

	private EditText userNameText;
	private EditText pwdText;

	private CheckBox remCheckBox;

	private TextView register;

	private ProgressDialog loginProgress;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == CONSTANTS.HTTPPOST) {

				if (msg.obj != null) {
					try {
						Thread.sleep(1250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					loginProgress.dismiss();

					
					if (msg.obj.toString().equals(CONSTANTS.LOGIN_ERR_TAG)) {

						pwdText.setText("");
						Toast.makeText(LoginActivity.this, CONSTANTS.LOGIN_ERR,
								Toast.LENGTH_SHORT).show();
					} else {

						Map<String, String> values = new HashMap<String, String>();

						values.put("login_phone", userNameText.getText()
								.toString());
						values.put("login_pwd", pwdText.getText().toString());

						UpdateSharedPreferences.updateMapString(
								CONSTANTS.PREFERENCE, values,
								LoginActivity.this);

						// 获取绑定信息

						List<OldmanBean> oldmanBeans = JSONArray.parseArray(
								msg.obj.toString(), OldmanBean.class);
						
						if (oldmanBeans.size() > 0) {
							Log.d("czp",
									"msg.obj of login init..."
											+ msg.obj.toString());
							LocalFileUtil.dropTable();
							LocalFileUtil.saveInitInDB(oldmanBeans);
						}

						else {
							System.out.println("return size is 0 ");
							HeartApp.getInstance().oldmanHashMap.clear();
						}
						
						if (remCheckBox.isChecked()) {

							UpdateSharedPreferences.updateBoolean(
									CONSTANTS.PREFERENCE, CONSTANTS.HASLOGIN,
									true, getBaseContext());
						}

						Intent intent = new Intent(LoginActivity.this,
								FuncionActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}
				}
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		loginProgress = new ProgressDialog(LoginActivity.this);
		loginProgress.setTitle(null);
		loginProgress.setMessage(CONSTANTS.LOGININFO);
		loginProgress.setCancelable(true);

		loginButton = (Button) findViewById(R.id.loginBtn);

		register = (TextView) findViewById(R.id.register_in_login);

		userNameText = (EditText) findViewById(R.id.user_name);
		pwdText = (EditText) findViewById(R.id.password);
		remCheckBox = (CheckBox) findViewById(R.id.remember_psd);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				final String name = userNameText.getText().toString();
				final String pwd = pwdText.getText().toString();

				if (!CheckNetWork.isNetAvailable(getApplicationContext())) {

					Toast.makeText(LoginActivity.this, CONSTANTS.NETWORKERR,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(userNameText.getText())
						|| TextUtils.isEmpty(pwdText.getText())) {
					return;
				} else {

					loginProgress.show();
					final Map<String, String> rawParams = new HashMap<String, String>();
					rawParams.put(CONSTANTS.LOGIN_PHONE, name);
					rawParams.put(CONSTANTS.LOGIN_PWD, pwd);

					new Thread() {
						@Override
						public void run() {

							String result = HttpUtil.postRequest(
									HttpUtil.BASEURL + CONSTANTS.LOGIN_SERVLET,
									rawParams);

							Message message = new Message();
							message.what = CONSTANTS.HTTPPOST;
							message.obj = result;
							handler.sendMessage(message);
						}
					}.start();
				}

			}
		});

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);

			}
		});
	}

}
