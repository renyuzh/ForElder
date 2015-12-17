package org.heartwings.care;

import java.lang.ref.WeakReference;

import org.heartwings.care.falldetect.FallDetectService;
import org.heartwings.care.photoshare.PhotoSharingService;
import org.heartwings.care.util.UsernamePasswordPair;
import org.heartwings.network.NetworkChecker;
import org.heartwings.network.NetworkOperationHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static MainActivity mainActivity;
	private static Context applicationContext;
	private LoginResultHandler loginResultHandler;
	// private TencentLocation lastLocation = null;
	boolean initiated = false;
	boolean isLogin;
	boolean isRememberLogin = true;
	String username;
	String password;
	public SharedPreferences preferences;
	public ProgressDialog loginProgress;

	public SharedPreferences getPreferences() {
		return preferences;
	}

	/**
	 * 登出，删除所有用户信息，停止服务。
	 */
	void logout() {
		isLogin = false;
		isRememberLogin = false;
		username = null;
		password = null;
		Intent stopFallDetect = new Intent(getApplicationContext(),
				FallDetectService.class);
		stopService(stopFallDetect);
		Intent stopPositionTrack = new Intent(getApplicationContext(),
				PhotoSharingService.class);
		stopService(stopPositionTrack);
	}

	public void navigateTo(Class<? extends Fragment> fragmentClass)
			throws InstantiationException, IllegalAccessException {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragmentClass.newInstance()).commit();
	}

	public LoginResultHandler getLoginResultHandler() {
		return loginResultHandler;
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public static Context getTheApplicationContext() {
		return applicationContext;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public MainActivity() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			isLogin = false;
			username = null;
			password = null;
		}
		mainActivity = this;
		applicationContext = this.getApplicationContext();
		loginResultHandler = new LoginResultHandler(
				new WeakReference<MainActivity>(this));
		preferences = getSharedPreferences(
				Constants.NAME_SHARED_PREFERENCE_MAIN_ACTIVITY, MODE_PRIVATE);
	}

	@Override
	protected void onPause() {
		Log.v("Login", "Save the login info into preferences");
		Log.v("Login",
				String.valueOf(isRememberLogin) + " " + String.valueOf(isLogin));
		Editor editor = preferences.edit();
		editor.putBoolean("isRememberLogin", isRememberLogin);
		editor.putBoolean("isLogin", isLogin);
		if (isLogin) {
			editor.putString("username", username);
			editor.putString("password", password);
		}
		editor.commit();
		super.onPause();
	}

	@Override
	protected void onResume() {
		preferences.getBoolean("isRememberLogin", false);
		isLogin = preferences.getBoolean("isLogin", false);
		if (isLogin) {
			username = preferences.getString("username", "");
			password = preferences.getString("password", "");
		}
		Log.v("Login", "Load the login info from preferences");
		Log.v("Login",
				String.valueOf(isRememberLogin) + " " + String.valueOf(isLogin));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	/**
	 * Fragment for login
	 */
	public static class PlaceholderFragment extends Fragment {
		// Views
		private Button loginButton;
		private EditText usernameEditText;
		private EditText passwordEditText;

		private CheckBox remCheckBox;
		private TextView register;

		private NetworkOperationHelper networkOperationHelper = NetworkOperationHelper
				.getNetworkOperationHelper();

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			ActionBar actionBar = getActivity().getActionBar();
			actionBar.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.actionbar_bg));
			actionBar.setTitle("登录");

			return rootView;
		}

		@Override
		public void onResume() {
			if (MainActivity.getMainActivity().isRememberLogin
					&& MainActivity.getMainActivity().isLogin) {
				Log.v("Login", "User has logged in");
				if (((MainActivity) getActivity()).isLogin()) {
					usernameEditText.setText(((MainActivity) getActivity())
							.getUsername());
					passwordEditText.setText(((MainActivity) getActivity())
							.getPassword());
					try {
						MainActivity.getMainActivity().navigateTo(
								SettingFragment.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			super.onResume();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			((MainActivity) this.getActivity()).loginProgress = new ProgressDialog(
					getActivity());
			((MainActivity) this.getActivity()).loginProgress.setTitle(null);
			((MainActivity) this.getActivity()).loginProgress
					.setMessage("正在登陆中。。。");
			((MainActivity) this.getActivity()).loginProgress
					.setCancelable(true);

			loginButton = (Button) this.getActivity().findViewById(
					R.id.loginBtn);
			usernameEditText = (EditText) this.getActivity().findViewById(
					R.id.user_name);

			passwordEditText = (EditText) this.getActivity().findViewById(
					R.id.password);
			remCheckBox = (CheckBox) this.getActivity().findViewById(
					R.id.remember_psd);
			remCheckBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							MainActivity.getMainActivity().isRememberLogin = isChecked;
						}
					});
			remCheckBox.setChecked(true);
			MainActivity.getMainActivity().isRememberLogin = true;

			loginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String username = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					if (!NetworkChecker.isNetAvailable(PlaceholderFragment.this
							.getActivity().getApplicationContext())) {
						Log.e("Login", "Network Unavailable");
						Toast.makeText(PlaceholderFragment.this.getActivity(),
								"网络无法连接，请检查网络。", Toast.LENGTH_SHORT).show();
						return;
					}
					if (TextUtils.isEmpty(username)
							|| TextUtils.isEmpty(password)) {
						Log.e("Login", "Empty user/password");
						return;
					} else {
						((MainActivity) getActivity()).loginProgress.show();
						networkOperationHelper.login(username, password,
								((MainActivity) PlaceholderFragment.this
										.getActivity()).loginResultHandler);
					}
				}
			});

			register = (TextView) this.getActivity().findViewById(
					R.id.register_in_login);

			register.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("Register", "Navigate to register activity.");
					Intent intent = new Intent(getActivity(),
							RegisterActivity.class);
					startActivity(intent);
				}
			});

			if (((MainActivity) this.getActivity()).isLogin) {
				usernameEditText
						.setText(((MainActivity) this.getActivity()).username);
				passwordEditText
						.setText(((MainActivity) this.getActivity()).password);
			}

			// Start the service detecting fall
			// if (!((MainActivity) this.getActivity()).initiated) {
			// Intent startIntent = new Intent(this.getActivity(),
			// FallDetectService.class);
			// this.getActivity().startService(startIntent);
			// }

			((MainActivity) this.getActivity()).initiated = true;

			super.onActivityCreated(savedInstanceState);
		}
	}

	public static class SettingFragment extends Fragment {
		// Views
		Switch falldownDetectSwitch;
		Switch photoshareSwitch;
		Button logOutButton;

		public SettingFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_setting,
					container, false);

			ActionBar actionBar = getActivity().getActionBar();
			actionBar.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.actionbar_bg));
			actionBar.setTitle("设置");

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			falldownDetectSwitch = (Switch) this.getActivity().findViewById(
					R.id.fallDownServiceSwitch);
			photoshareSwitch = (Switch) this.getActivity().findViewById(
					R.id.photoshareServiceSwitch);
			logOutButton = (Button) this.getActivity().findViewById(
					R.id.logOutButton);
			falldownDetectSwitch
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								Intent intent = new Intent(getActivity(),
										FallDetectService.class);
								getActivity().startService(intent);
							} else {
								Intent intent = new Intent(getActivity(),
										FallDetectService.class);
								getActivity().stopService(intent);
							}
						}
					});
			photoshareSwitch
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								Intent intent = new Intent(getActivity(),
										PhotoSharingService.class);
								intent.putExtra("username", (MainActivity
										.getMainActivity().getUsername()));
								intent.putExtra("password", (MainActivity
										.getMainActivity().getPassword()));
								getActivity().startService(intent);
							} else {
								Intent intent = new Intent(getActivity(),
										PhotoSharingService.class);
								getActivity().stopService(intent);
							}
						}
					});
			logOutButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("Login", "Try to log out.");
					Editor editor = ((MainActivity) getActivity()).preferences
							.edit();
					editor.clear();
					editor.commit();
					MainActivity.getMainActivity().logout();
					try {
						((MainActivity) getActivity())
								.navigateTo(PlaceholderFragment.class);
						Log.i("Login", "Log out completed.");
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public void onResume() {
			falldownDetectSwitch.setChecked(FallDetectService.getStarted());
			photoshareSwitch.setChecked(PhotoSharingService
					.getPhotoSharingService() != null);
			super.onResume();
		}
	}

	// @Override
	// public void onLocationChanged(TencentLocation tencentLocation, int error,
	// String reason) {
	// if (tencentLocation == null) {
	// Log.e("Sensor", "Failed to retrieve current location.");
	// } else {
	// lastLocation = tencentLocation;
	// }
	// }
	//
	// @Override
	// public void onStatusUpdate(String name, int status, String desc) {
	// // TODO Auto-generated method stub
	// }

	/**
	 * @author Inno520 LoginResultHandler属于UI线程 用于接收登陆成功、失败的消息
	 */
	public static class LoginResultHandler extends Handler {
		WeakReference<MainActivity> mainActivity;

		public LoginResultHandler(WeakReference<MainActivity> mainActivity) {
			super();
			this.mainActivity = mainActivity;
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity.getMainActivity().loginProgress.dismiss();
			switch (msg.what) {
			case Constants.MESSAGE_LOGIN_SUCCESS:
				Log.i("Login", "Received the login success message.");

				if (!MainActivity.getMainActivity().isLogin) {
					MainActivity.getMainActivity().isLogin = true;
					UsernamePasswordPair npp = (UsernamePasswordPair) msg.obj;
					MainActivity.getMainActivity().username = npp.getUsername();
					MainActivity.getMainActivity().password = npp.getPassword();
					Log.v("Login", "Save the login info into preferences");
					Editor editor = mainActivity
							.get()
							.getApplicationContext()
							.getSharedPreferences(
									Constants.NAME_SHARED_PREFERENCE_MAIN,
									Context.MODE_PRIVATE).edit();
					editor.putString("username", npp.getUsername());
					editor.putString("password", npp.getPassword());
					editor.commit();
				}
				try {
					MainActivity.getMainActivity().navigateTo(
							SettingFragment.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case Constants.MESSAGE_LOGIN_FAILED_WRONG_PASSWORD:
				Log.i("Login", "Received the login failed message.");
				Toast.makeText(
						MainActivity.getMainActivity().getApplicationContext(),
						"密码错误", Toast.LENGTH_SHORT).show();
				break;
			case Constants.MESSAGE_LOGIN_FAILED_CONNECTION_ISSUE:
				Log.i("Login", "Received the login failed message.");
				Toast.makeText(
						MainActivity.getMainActivity().getApplicationContext(),
						"网络连接有问题", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	}
}
