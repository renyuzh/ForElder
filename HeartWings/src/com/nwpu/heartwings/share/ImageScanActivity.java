package com.nwpu.heartwings.share;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.nwpu.heartwings.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class ImageScanActivity extends ActionBarActivity {

	private GridView mGroupGridView;
	private ProgressDialog mProgressDialog;
	private List<ImageBean> list = new ArrayList<ImageBean>();
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();

	public static final int LOAD_OK = 0x100;
	private GroupAdapter adapter;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == LOAD_OK) {
				mProgressDialog.dismiss();
				list = subGroupOfImage(mGruopMap);
				adapter = new GroupAdapter(ImageScanActivity.this, list,
						mGroupGridView);
				mGroupGridView.setAdapter(adapter);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_image_scan);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		getSupportActionBar().setHomeButtonEnabled(true);
		
		mGroupGridView = (GridView) findViewById(R.id.main_grid);

		getImages();

		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				List<String> childList = mGruopMap.get(list.get(position)
						.getFolderName());

				Intent intent = new Intent(ImageScanActivity.this,
						ImageShowActivity.class);
				intent.putStringArrayListExtra("data",
						(ArrayList<String>) childList);

				startActivity(intent);

			}
		});

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver contentResolver = ImageScanActivity.this
						.getContentResolver();

				Cursor cursor = contentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				while (cursor.moveToNext()) {
					// 图片的路径
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					// 父文件目录名
					String parentPath = new File(path).getParentFile()
							.getName();

					// 没有该 父文件目录名 (即文件夹的名称)
					if (!mGruopMap.containsKey(parentPath)) {
						List<String> childList = new ArrayList<String>();
						childList.add(path);
						mGruopMap.put(parentPath, childList);
					} else {

						mGruopMap.get(parentPath).add(path);
					}

				}

				cursor.close();
				// 扫描完成就通知handler

				handler.sendEmptyMessage(LOAD_OK);
			}
		}).start();
	}

	private List<ImageBean> subGroupOfImage(
			HashMap<String, List<String>> mGruopMap) {

		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();

		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageBean imageBean = new ImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();

			imageBean.setFolderName(key);

			imageBean.setImageCounts(value.size());
			imageBean.setTopImagePath(value.get(0));

			list.add(imageBean);
		}
		return list;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.share_shoot, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_shoot:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 1);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println("run the onactivity result...");

		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {

				Toast.makeText(ImageScanActivity.this, "SD卡存储不可用",
						Toast.LENGTH_LONG).show();
				return;
			}

			
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.CHINA);
			String fileName = format.format(date) + ".jpg";

			File fileFolder = new File(
					Environment.getExternalStorageDirectory() + File.separator +
							"DCIM" + File.separator + "Heart" + File.separator);
			if (!fileFolder.exists()) {
				fileFolder.mkdir();
			}

			File pngFile = new File(fileFolder, fileName);

			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(pngFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						fileOutputStream);

				
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

		}
	}

}
