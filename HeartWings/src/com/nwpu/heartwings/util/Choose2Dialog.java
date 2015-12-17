package com.nwpu.heartwings.util;

import java.util.ArrayList;

import com.heart.bean.OldmanBean;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.activities.SetEmergencyActivity;
import com.nwpu.heartwings.activities.ShowMapActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Choose2Dialog {

	public static void showDialog(final Context context, final int btnID){
		    
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
		
		if(items.length == 0){
			  
			DialogUtil.showDialog(context, "请先绑定老人");
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle("选择一个老人").setIcon(R.drawable.ic_action_user)
        .setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {

				ChooseDialogUtil.selectdPhone = phones.get(item);
				
				dialog.cancel();

				Intent intent = new Intent();
				switch (btnID) {
				case R.id.set_emergency_btn:
					 intent = new Intent(context, SetEmergencyActivity.class);
					 
					 intent.putExtra("thisOldman", phones.get(item));
					 
					 context.startActivity(intent);
					break;
 
				case 1:
					
					intent = new Intent(context,ShowMapActivity.class);
					intent.putExtra("oldmanphone", phones.get(item));
					context.startActivity(intent);
					break;
				default:
					break;
				}
			}
		});

builder.create().show();
	}
}
