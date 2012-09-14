package com.appndroid.crick20.ui;

import java.util.List;
import java.util.Map;

import com.appndroid.crick20.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class overrideAdapter extends SimpleAdapter {

	String m_tabname;
	private int[] colors = new int[] { 0x30ffffff, 0x30ff2020  };

	//0x30ff2020 
	
	public overrideAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to, String tabname) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		m_tabname = tabname;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		if (m_tabname.equals("reocrdTab")) {

			TextView row1 = (TextView) view.findViewById(R.id.row_item1);
			TextView row2 = (TextView) view.findViewById(R.id.row_item2);
			TextView row3 = (TextView) view.findViewById(R.id.row_item3);
			TextView row4 = (TextView) view.findViewById(R.id.row_item4);
			TextView row5 = (TextView) view.findViewById(R.id.row_item5);
			TextView row6 = (TextView) view.findViewById(R.id.row_item6);

			if (position == 0) {
				row1.setTextColor(0xff69D2E7);
				row2.setTextColor(0xff69D2E7);
				row3.setTextColor(0xff69D2E7);
				row4.setTextColor(0xff69D2E7);
				row5.setTextColor(0xff69D2E7);
				row6.setTextColor(0xff69D2E7);

			}
		} else if (m_tabname.equals("currentStats")) {
			TextView row1 = (TextView) view.findViewById(R.id.stat_item1);
			TextView row2 = (TextView) view.findViewById(R.id.stat_item2);
			TextView row3 = (TextView) view.findViewById(R.id.stat_item3);
			TextView row4 = (TextView) view.findViewById(R.id.stat_item4);
			TextView row5 = (TextView) view.findViewById(R.id.stat_item5);
			TextView row6 = (TextView) view.findViewById(R.id.stat_item6);
			TextView row7 = (TextView) view.findViewById(R.id.stat_item7);

			if (position == 0) {
				row1.setTextColor(0xff69D2E7);
				row2.setTextColor(0xff69D2E7);
				row3.setTextColor(0xff69D2E7);
				row4.setTextColor(0xff69D2E7);
				row5.setTextColor(0xff69D2E7);
				row6.setTextColor(0xff69D2E7);
				row7.setTextColor(0xff69D2E7);

			}

			if (position != 0) {
				int colorPos = position % colors.length;
				view.setBackgroundColor(colors[colorPos]);
			}

		} else if (m_tabname.equals("captab")) {
			
			TextView row1 = (TextView) view.findViewById(R.id.stat_item1);
			TextView row2 = (TextView) view.findViewById(R.id.stat_item2);
			TextView row3 = (TextView) view.findViewById(R.id.stat_item3);
			TextView row4 = (TextView) view.findViewById(R.id.stat_item4);
			TextView row5 = (TextView) view.findViewById(R.id.stat_item5);
			TextView row6 = (TextView) view.findViewById(R.id.stat_item6);


			if (position == 0) {
				row1.setTextColor(0xff69D2E7);
				row2.setTextColor(0xff69D2E7);
				row3.setTextColor(0xff69D2E7);
				row4.setTextColor(0xff69D2E7);
				row5.setTextColor(0xff69D2E7);
				row6.setTextColor(0xff69D2E7);

			}
			
			if (position != 0) {
				int colorPos = position % colors.length;
				view.setBackgroundColor(colors[colorPos]);
			}
		
		}

		return view;
	}
}