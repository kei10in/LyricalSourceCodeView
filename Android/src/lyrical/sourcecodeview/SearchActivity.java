package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity {
    private static final String SEARCH_API = "http://github.com/api/v2/json/repos/search/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void onSearch(View view) {
        EditText editText = (EditText) findViewById(R.id.searchEditText);
        String result = doSearchApi(editText.getText().toString());
        
        try{
        JSONObject json = new JSONObject(result);
        JSONArray jsons = (JSONArray)json.get("repositories");
        for (int i = 0; i < jsons.length(); i++) {
            JSONObject jsonObj = jsons.getJSONObject(i);
            System.out.println(jsonObj.getString("name"));
            System.out.println(jsonObj.getString("url"));
            System.out.println(jsonObj.getString("language"));
            System.out.println(jsonObj.getString("description"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String doSearchApi(String word) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(SEARCH_API + word);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection()
                    .getInputStream()));
            String s;
            // 一行でJSONが返ってくるので冗長？
            while ((s = reader.readLine()) != null) {
//                System.out.println(s);
                sb.append(s);
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, "GitHubに接続できませんでした", Toast.LENGTH_SHORT);
        }

        return sb.toString();
    }
}
