package com.appndroid.crick20.ui;

public interface ApplicationDefines {
	interface CommandType {
		public byte COMMAND_STANDINGS = 0;
		public byte COMMAND_SCHEDULE = 1;
		public byte COMMAND_ONGOING = 2;
		public byte COMMAND_LIVE_MATCH = 3;

	}

	interface ResponseType {
		public byte RESPONSE_NONE = 0;
		public byte RESPONSE_ERROR = 1;
	}

	interface HttpRequestType {
		public byte POST = 1;
		public byte GET = 2;
	}

	interface HandlerMessages {
		public byte START = 1;
		public byte STOP = 2;

	}

	interface Constants {
		// public String APP_ID = "215747395117981";
		// public String API_KEY = "4ad0515d56a3bc4b66f48db86705ea8f";
		// public String APP_SECRET = "0df0c25c64c74438487edfe50c1f9349";
		public String SCHEDULE_URL = "https://docs.google.com/spreadsheet/pub?key=0AquokcNauMVidEFvcGVyTDB1SEhlLWtnZDhJVnpFd3c&output=txt";
		// public String SCHEDULE_URL =
		// "https://docs.google.com/spreadsheet/pub?key=0AsDkbvxwVowedGVVdktDekdRdEN0ZFNjLWFBdjMtYWc&output=html";
		// public String STANDINGS_URL =
		// "https://spreadsheets.google.com/spreadsheet/ccc?hl=en&output=txt&key=t1xODsiDa8OEJUF2Vnj-ZVg&hl=en#gid=0";
		// public String SCHEDULE_URL_1 =
		// "http://spreadsheets.google.com/ccc?key=0AkxxVhB_SxGrdExVMEpacEloNnBVNnVRMUYtaTg3NUE&hl=en&output=txt&authkey=CPGxtroJ";
		// public String SCHEDULE_URL_2 =
		// "https://spreadsheets.google.com/ccc?key=0ArseD_L_glB-dHdLQVFKSHplY2hIUmFqbUdaT2FGa0E&hl=en&output=txt&#gid=0";
		// public String STANDINGS_URL_1 =
		// "http://spreadsheets.google.com/ccc?key=0AkxxVhB_SxGrdEFsMUZ2VUhBdHIxNVFlYUZfOEt2X1E&hl=en&output=txt&authkey=CPHWiqgM#gid=0";
		// public String STANDINGS_URL_2 =
		// "http://spreadsheets.google.com/spreadsheet/ccc?hl=en&hl=en&key=tHgcPPkk6a66QWhfj6yN1Yw&output=txt&authkey=CLu5xfwD#gid=0";
		// public String STANDINGS_URL_3 =
		// "https://spreadsheets.google.com/spreadsheet/ccc?hl=en&output=txt&key=t1xODsiDa8OEJUF2Vnj-ZVg&hl=en#gid=0";
		// public int MINIMIZE_APP = 0;
	}

}
