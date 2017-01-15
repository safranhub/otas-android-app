package si.um.ietk.safran_app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    private static final String URL =
            "https://feri.um.si/odeska/rss/";

    private XmlPullParserFactory xmlFactoryObject;

    private ArrayList<String> zunanjiMap = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Log.d("SplashActivity", "onCreate");
    }

    // ---------------- GetDataXML -------------------- //

    @Override
    public void onStart() {
        super.onStart();
        new DownloadXmlTask().execute(URL);
        Log.d("SplashActivity", "onStart");
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                downloadUrl(URL);
                Log.d("SplashActivity", "down url");
                return "a";
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.connection_error);
            } catch (IOException e ) {
                return getResources().getString(R.string.connection_error);
            }

        }
        @Override
        protected void onPostExecute(String result) {
            //setContentView(R.layout.activity_main);

            super.onPostExecute(result);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("outerMap", zunanjiMap);
            startActivity(intent);
            finish();

            Log.d("SplashActivity", "Podatki razclenjeni");

        }
    }

    private void downloadUrl(String urlString) throws IOException, XmlPullParserException {
            java.net.URL url = new URL(urlString);
            //Log.d("MAINactivity2", urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //String result = reader.readLine();
            //Log.d("MAINactivity2", result);

            // initiate parser
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(false);
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setInput(stream, null);

            parseXML(myparser); //parse xml content
            stream.close(); // close stream
            Log.d("SplashActivity", "downloadURL");
    }

    public void parseXML(XmlPullParser xpp) {
        try {
            boolean insideItem = false; // only parse elements inside item tag
            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) { // start tag is item
                        insideItem = true; //condition for parsing elements inside item
                    } else if (xpp.getName().equalsIgnoreCase("title")) { //start tag is title
                        if (insideItem) // and is inside item
                            //Log.i("....", xpp.nextText()); // extract the headline
                            zunanjiMap.add(xpp.nextText().toString());
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem)
                            //Log.i("....", xpp.nextText()); // extract the description
                            zunanjiMap.add(xpp.nextText().toString());
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItem)
                            //Log.i("....", xpp.nextText()); // extract the publication date
                            zunanjiMap.add(xpp.nextText().toString());
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            //Log.i("....", xpp.nextText());  // extract the link
                            zunanjiMap.add(xpp.nextText().toString());
                    }
                }
                else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) { //outside item element
                    insideItem = false; // set condition to false

                }
                eventType = xpp.next(); // move to next element

            }
            Log.d("SplashActivity", zunanjiMap.toString());
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("SplashActivity", "parseXML");
    }


   
    // ----------------- konec GetDataParseXML ------------------ //

}
