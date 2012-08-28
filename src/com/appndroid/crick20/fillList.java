package com.appndroid.crick20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.database.Cursor;

public class fillList {

	HashMap<String, String> map = new HashMap<String, String>();
	List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();

	String[] columnNames;

	public fillList(String[] argColumnName) {
		// TODO Auto-generated constructor stub

		columnNames = argColumnName;

		for (int i = 0; i < argColumnName.length; i++) {
			map.put(argColumnName[i].toString(), argColumnName[i].toString());
		}

		mapList.add(map);

	}

	public void insertListData(String[] argListData) {
		map = new HashMap<String, String>();
		for (int i = 0; i < argListData.length; i++) {
			map.put(columnNames[i].toString(), argListData[i].toString());
		}

		mapList.add(map);
	}

	public List getFilledList() {
		return mapList;
	}

	public void fillRecordList(Cursor cur, fillList dataList, String tabname) {
		int counter = cur.getCount();

		cur.moveToFirst();
		while (cur.isAfterLast() == false) {

			int currentposotion = cur.getPosition();
			
			if (tabname.equals("reocrdTab")) {
				
				String Player = cur.getString(1).trim();
				String Match = cur.getString(2).trim();
				String Inns = cur.getString(3).trim();
				String Runs = cur.getString(4).trim();
				String HS = cur.getString(5).trim();
				String Team = cur.getString(6).trim();

				String[] rowData = new String[] { Player, Match, Inns, Runs,
						HS, Team };
				dataList.insertListData(rowData);
			}
			else
			{
				String Team = cur.getString(1).trim();
				String played = cur.getString(2).trim();
				String won = cur.getString(3).trim();
				String lost = cur.getString(4).trim();
				String NR = cur.getString(5).trim();
				String points = cur.getString(6).trim();
				String runrate = cur.getString(7).trim();

				String[] rowData = new String[] { Team, played, won, lost,
						NR, points, runrate };
				dataList.insertListData(rowData);
				
			}

			cur.moveToNext();
		}

		cur.close();
	}

}