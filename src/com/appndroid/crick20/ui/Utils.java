package com.appndroid.crick20.ui;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class Utils {
	public static SQLiteDatabase db = null;
	public static Context currentContext = null;
	public static boolean isDataMatchURLparsed = false;

	public static void getDB(Context context) {
		db = context.openOrCreateDatabase("worldcupt20.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	public static void setContext(Context context) {
		currentContext = context;
	}

	public static void setIsPushStatusPostedOnServer(Context context,
			boolean bValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putBoolean("push_status_posted", bValue);
		editor.commit();
	}

	public static boolean getIsPushStatusPostedOnServer(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean("push_status_posted", false);
	}

	public static String getMd5Hash(String szSource) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bmessageDigest = md.digest(szSource.getBytes());
			// We convert the result into a big integer, so that we can convert
			// it into a Hexadecimal;
			BigInteger convertedToNumber = new BigInteger(1, bmessageDigest);
			String szmd5 = convertedToNumber.toString(16); // convert to hex
															// string

			// stuff with leading zeroes which are removed when we convert the
			// byte array to a bigInteger;
			if (szmd5.length() < 32) {
				while (szmd5.length() < 32)
					szmd5 = "0" + szmd5;
			}

			return szmd5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
