package com.nwpu.heartwings.util;

import java.util.ArrayList;

import com.heart.bean.OldmanBean;
import com.nwpu.heartwings.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ChooseDialogUtil {

	
	public static String selectdPhone = "";
	
	public static String showChoose(Context context)
	{
		 
		
		ArrayList<OldmanBean> oldmanBeans =
				  LocalFileUtil.searchForAllBinded(LocalFileUtil.getThisClient(context));
		
		ArrayList<String> names = new ArrayList<>();
       
		final ArrayList<String> phones = new ArrayList<>();
		
		for(int i = 0; i < oldmanBeans.size(); i++)
		{
			names.add(oldmanBeans.get(i).getName());
			phones.add(oldmanBeans.get(i).getPhone());
		}
		
		final String[] items = names.toArray(new String[0]);
		
		if(items.length == 0)
		{	
			return null;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
		         .setTitle("选择一个老人").setIcon(R.drawable.ic_action_user)
		         .setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int item) {
		
						ChooseDialogUtil.selectdPhone = phones.get(item);
						
						dialog.cancel();
		
					}
				});
		
		builder.create().show();
		
		return "";
	}
}
