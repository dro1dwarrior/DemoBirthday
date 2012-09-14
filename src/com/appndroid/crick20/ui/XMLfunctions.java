package com.appndroid.crick20.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.widget.Toast;

public class XMLfunctions {

	private static ArrayList<HashMap<String, String>> matchNodeList = new ArrayList<HashMap<String, String>>();
	public static Context mContext;

	public final static Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	public final static String getElementValue(Node elem) {
		Node kid;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (kid = elem.getFirstChild(); kid != null; kid = kid
						.getNextSibling()) {
					if (kid.getNodeType() == Node.TEXT_NODE) {
						return kid.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public static String getXML(String path) {
		String line = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(path);
			BufferedReader in = null;
			HttpResponse response = null;

			try {
				response = httpclient.execute(httpget);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpEntity entity = null;
			if (response != null)
				entity = response.getEntity();
			try {
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				// Log.d("cricket", sb.toString());

				return sb.toString();

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// DefaultHttpClient httpClient = new DefaultHttpClient();
			// HttpPost httpPost = new HttpPost(path);
			//
			// HttpResponse httpResponse = httpClient.execute(httpPost);
			// HttpEntity httpEntity = httpResponse.getEntity();
			// line = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(mContext,
					"Unable to fetch live score at this moment.",
					Toast.LENGTH_LONG).show();

		}

		return line;
	}

	public static ArrayList<HashMap<String, String>> getMatchNodelist(
			NodeList nodes) {
		for (int i = 0; i < nodes.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("seriesName",
					"Series Name : " + XMLfunctions.getValue(e, "seriesName"));
			map.put("team1", "Team 1 : " + XMLfunctions.getValue(e, "team2"));
			map.put("team2", "Team 2 : " + XMLfunctions.getValue(e, "team1"));
			map.put("startDate",
					"Start Date : " + XMLfunctions.getValue(e, "startdate"));
			map.put("endDate",
					"End Date : " + XMLfunctions.getValue(e, "enddate"));
			map.put("type", "Type : " + XMLfunctions.getValue(e, "type"));
			map.put("scoreUrl", XMLfunctions.getValue(e, "scores-url"));
			matchNodeList.add(map);
		}
		return matchNodeList;
	}

	public static String numResults(Document doc, String itemName) {
		Node results = doc.getDocumentElement();
		String res = "-1";

		try {
			res = results.getAttributes().getNamedItem("itemName")
					.getNodeValue();
		} catch (Exception e) {
			res = "-1";
		}

		return res;
	}

	// public static String getAttValue(N item, String str) {
	// NodeList n = item.getElementsByTagName(str);
	// return XMLfunctions.getElementValue(n.item(0));
	// }

	public static String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return XMLfunctions.getElementValue(n.item(0));
	}
}
