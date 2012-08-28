package com.appndroid.crick20;

public class getDrawable {	
	
	public int getIcon(String teamName)
	{	
		
		if(teamName.toLowerCase().equals("afg")||teamName.toLowerCase().equals("afghanistan"))
			return  R.drawable.afghanistan ;
		else if(teamName.toLowerCase().equals("aus")||teamName.toLowerCase().equals("australia"))
			return R.drawable.australia ;
		else if(teamName.toLowerCase().equals("ban")||teamName.toLowerCase().equals("bangladesh"))
			return  R.drawable.bangladesh ;
		else if(teamName.toLowerCase().equals("eng")||teamName.toLowerCase().contains("england"))
			return R.drawable.england ;
		else if(teamName.toLowerCase().equals("ind")||teamName.toLowerCase().equals("india"))
			return R.drawable.india ;
		else if(teamName.toLowerCase().equals("ire")||teamName.toLowerCase().equals("ireland"))
			return  R.drawable.ireland ;
		else if(teamName.toLowerCase().equals("nz")||teamName.toLowerCase().equals("new zealand")||teamName.toLowerCase().equals("newzealand"))
			return R.drawable.newzealand ;
		else if(teamName.toLowerCase().equals("pak")||teamName.toLowerCase().equals("pakistan"))
			return  R.drawable.pakistan ;
		else if(teamName.toLowerCase().equals("sl")||teamName.toLowerCase().equals("sri lanka")||teamName.toLowerCase().equals("srilanka"))
			return R.drawable.srilanka ;
		else if(teamName.toLowerCase().equals("rsa")||teamName.toLowerCase().equals("south africa")||teamName.toLowerCase().equals("southafrica"))
			return R.drawable.southafrica ;
		else if(teamName.toLowerCase().equals("wi")||teamName.toLowerCase().equals("west indies")||teamName.toLowerCase().equals("westindies"))
			return R.drawable.westindies ;
		else if(teamName.toLowerCase().equals("zim")||teamName.toLowerCase().equals("zimbabwe"))
			return R.drawable.zimbabwe ;
		else if(teamName.toLowerCase().equals("appicon"))
			return R.drawable.icon ;
		else
			return R.drawable.icon;
	}
	
	public String getMonthName(String month)
    {
    	if(month.equals("01"))
    		return "January";

    	else if (month.equals("01"))
    		return "Jan";
    	else if (month.equals("02"))
    		return "Feb";
    	else if (month.equals("03"))
    		return "Mar";
    	else if (month.equals("04"))
    		return "Apr";
    	else if (month.equals("05"))
    		return "May";
    	else if (month.equals("06"))
    		return "Jun";
    	else if (month.equals("07"))
    		return "Jul";
    	else if (month.equals("08"))
    		return "Aug";
    	else if (month.equals("09"))
    		return "Sep";
    	else if (month.equals("10"))
    		return "Oct";
    	else if (month.equals("11"))
    		return "Nov";
    	else if (month.equals("12"))
    		return "Dec";
    	else 
    		return month;
    }
	
	public String getTeamShortCode(String teamName)
	{
		if(teamName.equalsIgnoreCase("Afghanistan"))
			return  "AFG";
		else if(teamName.equalsIgnoreCase("Australia"))
			return "AUS";
		else if(teamName.equalsIgnoreCase("Bangladesh"))
			return  "BAN" ;
		else if(teamName.equalsIgnoreCase("England"))
			return "ENG";
		else if(teamName.equalsIgnoreCase("India"))
			return "IND" ;
		else if(teamName.equalsIgnoreCase("Ireland"))
			return  "IRE" ;
		else if(teamName.equalsIgnoreCase("New zealand")||teamName.equalsIgnoreCase("Newzealand"))
			return "NZ" ;
		else if(teamName.equalsIgnoreCase("Pakistan"))
			return  "PAK" ;
		else if(teamName.equalsIgnoreCase("South africa")||teamName.equalsIgnoreCase("Southafrica"))
			return "RSA";
		else if(teamName.equalsIgnoreCase("Sri lanka")||teamName.equalsIgnoreCase("Srilanka"))
			return "SL";
		else if(teamName.equalsIgnoreCase("West indies")||teamName.equalsIgnoreCase("westindies"))
			return "WI";
		else if(teamName.equalsIgnoreCase("Zimbabwe"))
			return "ZIM";
		else
			return teamName ;

	}

}
