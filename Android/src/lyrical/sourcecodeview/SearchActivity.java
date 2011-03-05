package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void onSearch(View view) {
        EditText editText = (EditText)findViewById(R.id.searchEditText);
        doSearchApi(editText.getText().toString());
    }

    public String doSearchApi(String word) {
        String urlString = "http://github.com/api/v2/json/repos/search/" + word;
        try {
            URL url = new URL(urlString);
            URLConnection uc = url.openConnection();
            InputStream is = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            StringBuffer sb = new StringBuffer();
            // 一行でJSONが返ってくるので冗長？
            while ((s = reader.readLine()) != null) {
                System.out.println(s);
            }
            reader.close();
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + urlString);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Can't connect to " + urlString);
            System.exit(0);
        }
        
        return null;
    }
}
