package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.widget.Toast;

public class HttpClient {
    protected static String request(String api, Activity activity) {
        System.out.println(api);
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(api);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection()
                    .getInputStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s + "\n");
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(activity, "GitHubに接続できませんでした", Toast.LENGTH_SHORT);
        }

        return sb.toString();
    }
}
