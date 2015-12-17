package com.nwpu.heartwings.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONArray;
import com.heart.bean.HeartMsg;
import com.heart.bean.HeartNews;
import com.heart.bean.HeartReturnBindMsg;
import com.heart.bean.OldmanBean;
import com.heart.bean.RegularPosition;
import com.heart.bean.RegularShare;
import com.nwpu.heartwings.app.HeartApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class LocalFileUtil {

	
	public static String getThisClient(Context context)
	{
		SharedPreferences preferences =context.getSharedPreferences(CONSTANTS.PREFERENCE, android.content.Context.MODE_PRIVATE);
		
		String name = preferences.getString("login_phone", "");
			
		return name;
	}
	
	public static void savaBindInDB(HeartReturnBindMsg msg){
		
		   BindInfoDBHelper dbHelper = new BindInfoDBHelper(HeartApp.getInstance(), "heartBind.db3", null, 1);
		   
		    String oldmanPhone = msg.getFromUserName();
		    String oldmanName = msg.getOldmanName();
		    
		    String thisClient = getThisClient(HeartApp.getInstance());
		    
		    insertBindData(dbHelper.getReadableDatabase(), oldmanPhone, oldmanName,thisClient);
		    
		    HeartApp.getInstance().oldmanHashMap.put(oldmanPhone, oldmanName);
		    
		    
		    dbHelper.close();
	}
	
	private static void insertBindData(SQLiteDatabase database, String phone, String name,String thisClient){
		   
		  database.execSQL("insert into binding values(null,?,?,?)",new String[]{phone,name,thisClient});
		  
	}
	
	public static ArrayList<OldmanBean> searchForAllBinded(String client){
		
		ArrayList<OldmanBean> oldmanBeans = new ArrayList<OldmanBean>();
		
		 BindInfoDBHelper dbHelper = new BindInfoDBHelper(HeartApp.getInstance(), "heartBind.db3", null, 1);
		
		 Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select  *  from binding where thisclient = ?", new String[]{client});
		 
		 while(cursor.moveToNext()){
			   
			 OldmanBean oldmanBean = new OldmanBean();
			 
			 oldmanBean.setPhone(cursor.getString(1));
			 oldmanBean.setName(cursor.getString(2));
			
			 
			 oldmanBeans.add(oldmanBean);
		 }
		 
		 cursor.close();
		 dbHelper.close();
		
		return oldmanBeans;
		
	}
	
	public static OldmanBean getOldmanByPhone(String phone){
		  
		 OldmanBean oldmanBean = new OldmanBean();
		 
		 BindInfoDBHelper dbHelper = new BindInfoDBHelper(HeartApp.getInstance(), "heartBind.db3", null, 1);
		 
		 Cursor cursor = dbHelper.getReadableDatabase().rawQuery
				 ("select * from binding where phone = ?", new String[]{phone});
		 
		 while(cursor.moveToNext()){
			    
			  oldmanBean.setPhone(phone);
			  oldmanBean.setName(cursor.getString(2));
		 }
		 
		 cursor.close();
		 dbHelper.close();
		 return oldmanBean;
	}
	
	public static void saveInitInDB(List<OldmanBean> oldmanBeans){
		
		System.out.println("run sava init in db");
		BindInfoDBHelper dbHelper = new BindInfoDBHelper(HeartApp.getInstance(), "heartBind.db3", null, 1);
		
		String thisClient = getThisClient(HeartApp.getInstance());
		
		HeartApp.getInstance().oldmanHashMap.clear();
		for(OldmanBean oldmanBean : oldmanBeans){
			 insertBindData(dbHelper.getWritableDatabase(), oldmanBean.getPhone(), oldmanBean.getName(),thisClient);

			 HeartApp.getInstance().oldmanHashMap.put(oldmanBean.getPhone(), oldmanBean.getName());
			 
			 
		}
		
		dbHelper.close();
	}
	
	public static void dropTable(){
		
		 BindInfoDBHelper dbHelper = new BindInfoDBHelper(HeartApp.getInstance(), "heartBind.db3", null, 1);
		 
		 dbHelper.getReadableDatabase().execSQL("delete from binding");
		 
		 dbHelper.close();
	}
	
	public static void SaveEmergencyInDB(String name,String phone,String oldmanPhone){
		   
		EmergencyDBHelper dbHelper = new EmergencyDBHelper(HeartApp.getInstance(), "heartEmergency.db3", null, 1);
		
		 dbHelper.getReadableDatabase().execSQL("insert into emergency values(null,?,?,?)",new String[]{phone,name,oldmanPhone});
		 
		 dbHelper.close();
	}
	
	public static Cursor fetchEmergencyCursor(String oldman){
		
		EmergencyDBHelper dbHelper = new EmergencyDBHelper(HeartApp.getInstance(), "heartEmergency.db3", null, 1);
		
		Cursor cursor = dbHelper.getReadableDatabase()
				 .rawQuery("select * from emergency where oldmanphone = ?", new String[]{oldman});
		
		//dbHelper.close();
		
		return cursor;
	}
	
	public static void removeEmergencyItem(int _id){
		
		EmergencyDBHelper dbHelper = new EmergencyDBHelper(HeartApp.getInstance(), "heartEmergency.db3", null, 1);
		
		dbHelper.getReadableDatabase().execSQL("delete from emergency where _id =" + _id);
		
		dbHelper.close();
	}
	
	/**
	 * 保存图片到SD的缓存,这个方法保存的图片没有设置有效期，
	 * 使用时，只保存了天气图标，即可长期存储的。
	 * @param bitmap
	 * @param url
	 */
	public static void saveImgToSD(Bitmap bitmap,String url){
		  if(bitmap == null){
			  Log.w("czp", "try to save null bitmap");
			  return;
		  }
		  
		  File cachdir = HeartApp.getInstance().getExternalCacheDir();
		  
		  System.out.println(urlToLocalPath(url) + " actural path is ");
		  File imgFile = new File(cachdir, urlToLocalPath(url));
		  
		 
		  try {
			OutputStream outputStream =
					   new FileOutputStream(imgFile);
			  
			  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			  
			  outputStream.flush();
			  outputStream.close();
			  Log.w("czp", "save ok");
		} catch (IOException e) {
		     Log.w("czp", "sava err");
			e.printStackTrace();
		}
		  
	}
	
	public static void savePositionCache(String data){
		 
		 File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File positionFile = new File(cachdir, "positions.dat");
		 
		
		 if(new File(cachdir, "position.dat").exists())
		 {
			 new File(cachdir, "position.dat").delete();
		 }
		  
		 try {
			 			 
			OutputStream outputStream =
					 new BufferedOutputStream(new FileOutputStream(positionFile));
			outputStream.write(data.getBytes());
			
			outputStream.flush();
			outputStream.close();
			
		} catch (FileNotFoundException e) {		
			 Log.w("czp", "sava err cache not found");
			e.printStackTrace();
		} catch (IOException e) {
			 Log.w("czp", "sava cache err");
			e.printStackTrace();
		}
		 		 
	}
	
	public static List<RegularPosition> getPositionCache(){
		
        File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File positionFile = new File(cachdir, "positions.dat");
		 
		 try {
			 
			 InputStream inputStream = new BufferedInputStream(new FileInputStream(positionFile));
			
			String data =  IOUtils.toString(inputStream);
			
			System.out.println("data from cache " + data);
			
			
			inputStream.close();
			
			return JSONArray.parseArray(data, RegularPosition.class);
			 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
		 return null;
	}
	public static String urlToLocalPath(String url){
		
		
		return url.replaceAll("[:%$=/]","_" ) ;
	}

	public static Bitmap getBitmapFromLocalPath(String url) throws FileNotFoundException{
		     
		 File cachdir = HeartApp.getInstance().getExternalCacheDir();
		  
		  File imgFile = new File(cachdir, url);
		  
		  return BitmapFactory.decodeStream(new FileInputStream(imgFile));
		  
	}
	
	public static boolean isFileExistInCache(String filename) throws FileNotFoundException, IOException{
		  
		  File cachdir = HeartApp.getInstance().getExternalCacheDir();
		  File file = new File(cachdir, filename);
		   return file.exists();
	}
	
	
	/**
	 * 保存一般的缓存文件,
	 * 分享 : share.dat
	 * 
	 * @param filename
	 * @param data
	 */
	public static void saveCommonCache(String filename,String data){
		
		 File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File cacheFile = new File(cachdir, filename);
		 
		 try {
 			 
				OutputStream outputStream =
						 new BufferedOutputStream(new FileOutputStream(cacheFile));
				outputStream.write(data.getBytes());	
				outputStream.flush();
				outputStream.close();
				
			} catch (FileNotFoundException e) {		
				 Log.w("czp", "sava err cache not fount in common save");
				e.printStackTrace();
			} catch (IOException e) {
				 Log.w("czp", "sava cache err in common save");
				e.printStackTrace();
			}
	}
	
	public static List<RegularShare> getCacheShare(){
		
		 File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File shareCache = new File(cachdir, "shares.dat");
		 
		 try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(shareCache));
				
				String data =  IOUtils.toString(inputStream);
				
				System.out.println("data from cache " + data);
						
				inputStream.close();
				
				return JSONArray.parseArray(data,RegularShare.class);
				
		} catch (IOException e) {
		     Log.d("czp", "get share cache err");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 这个缓存图片的方法是设置了有效期的。规定最大缓存15张图片
	 * 主要缓存分享的图片，保存在 \share\下面
	 * @param bitmap
	 * @param url 网络URL
	 */
	
	
	public static void saveImgWithExpire(Bitmap bitmap, String url){
		
		 File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File shareFolder = new File(cachdir.getAbsolutePath() + File.separator +
				  "share" + File.separator);
		 
		 if(!shareFolder.exists()){
			 shareFolder.mkdir();
		 }
		 
		 // 如果该目录下文件数大于15，则执行删除策略
         if(shareFolder.listFiles().length > 15)
         {
        	 // 先找出最近未使用的
        	 File[] files = shareFolder.listFiles();
        	 Arrays.sort(files, new Comparator<File>() {

				@Override
				public int compare(File file1, File file2) {
					
                    long deta = file1.lastModified() - file2.lastModified();
                    
					return deta < 0 ? -1 : 1;
				}
        		 
			});
        	    
        	 files[0].delete();
        	 files[1].delete();
        	 files[2].delete();
        	 
         }
         
		 File imgFile = new File(shareFolder, urlToLocalPath(url));
		 
		 try {
			OutputStream outputStream =
					   new FileOutputStream(imgFile);
			  
			  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			  
			  outputStream.flush();
			  outputStream.close();
			  Log.w("czp", "save ok");
			  
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
	
	/**
	 * 
	 * @param url 本地URL
	 * @return
	 */
	public static boolean isShareImgInCache(String url){
		  
        File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File shareFolder = new File(cachdir.getAbsolutePath() + File.separator +
				  "share" + File.separator);
		 
		 File shareImg = new File(shareFolder, url);
		 System.out.println(cachdir.getAbsolutePath() + "  " + shareImg.exists());
		 
		 return shareImg.exists();
		
	}
	
	/**
	 * 
	 * @param url 是本地URL
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getShareBitmapFromLocal(String url) throws FileNotFoundException{
		    
		File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File shareFolder = new File(cachdir.getAbsolutePath() + File.separator +
				  "share" + File.separator);
		 
		 File shareFile = new File(shareFolder,url);
		 
		 return BitmapFactory.decodeStream(new FileInputStream(shareFile));
		 
	}
	
	
	public static List<HeartMsg> getCacheAgenda(){
		    
		File cachdir = HeartApp.getInstance().getExternalCacheDir();
		 
		 File shareCache = new File(cachdir, "agenda.dat");
		 
		 try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(shareCache));
				
				String data =  IOUtils.toString(inputStream);
				
				System.out.println("data from cache " + data);
						
				inputStream.close();
				
				return JSONArray.parseArray(data,HeartMsg.class);
				
		} catch (IOException e) {
		     Log.d("czp", "get agenda cache err");
			e.printStackTrace();
		}
		 
		return null;
	}
	
	
	public static List<HeartNews> getCacheNews(){
		
		File cachdir = HeartApp.getInstance().getExternalCacheDir();
		
		File newsFile = new File(cachdir,"heartnews.dat");
		
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(newsFile));
			
			String data = IOUtils.toString(inputStream);
			
			inputStream.close();
			
			return JSONArray.parseArray(data, HeartNews.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
