package mustafaozhan.github.com.simpleweather.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mustafa Ozhan on 9/30/17 at 1:43 PM on Arch Linux.
 */

public class Helper {

    private static String stream = null;

    public Helper() {
    }

    public String getHTTPData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = r.readLine()) != null)
                    sb.append(line);

                stream = sb.toString();
                urlConnection.disconnect();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
