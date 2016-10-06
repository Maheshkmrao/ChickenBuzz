package com.android.chickenbuzz.componenets;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.chickenbuzz.R;


/**
 * 
 * @author 703145805
 * Used to display Custom Spinner as defined by custom layout. 
 */
public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

	private final Context activity;
	private String[] asr;

	public CustomSpinnerAdapter(Context context, String[] asr) {
		this.asr=asr;
		activity = context;
	}

	public int getCount()
	{
		return asr.length;
	}

	public Object getItem(int i)
	{
		return asr[i];
	}

	public long getItemId(int i)
	{
		return (long)i;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView txt = new TextView(activity);
		txt.setPadding(16, 16, 16, 16);
		txt.setTextSize(18);
		txt.setGravity(Gravity.LEFT);
		txt.setText(asr[position]);
		txt.setTextColor(activity.getResources().getColor(R.color.user_name_text_color));
		return  txt;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		TextView txt = new TextView(activity);
		txt.setGravity(Gravity.LEFT);
		txt.setPadding(16, 16, 16, 16);
		txt.setTextSize(16);
		txt.setBackgroundResource(R.drawable.search_edittext_shape);
		txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		txt.setText(asr[i]);
		txt.setTextColor(activity.getResources().getColor(R.color.user_name_text_color));
		return  txt;
	}
	
}
