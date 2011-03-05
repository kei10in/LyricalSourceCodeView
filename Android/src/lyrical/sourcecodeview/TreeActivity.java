package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class TreeActivity extends ListActivity {
    private String[] mValues;
    private static final String BLOB_ALL_API = "http://github.com/api/v2/json/blob/all/";
    private static final String BLOB_SHOW_API = "http://github.com/api/v2/json/blob/show/";
    private static final String BRANCHE = "master";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String result = request(BLOB_ALL_API + "kei10in/LyricalSourceCodeView" + "/" + BRANCHE);

        ArrayList<String> keyList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();
        try {
            JSONObject json = new JSONObject(result);
            json = json.getJSONObject("blobs");
            Iterator<?> it = json.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                keyList.add(key);
                valueList.add(json.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, keyList.toArray(new String[0]));
        mValues = valueList.toArray(new String[0]);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = request(BLOB_SHOW_API + "kei10in/LyricalSourceCodeView/"
                        + mValues[position]);
                String html = lyrical.highlighter.Highlighter.buildHtml(result);
                Intent intent = new Intent(TreeActivity.this, ViewerActivity.class);
                intent.putExtra("html", html);
                startActivity(intent);
            }
        });
    }

    private String request(String api) {
        System.out.println(api);
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(api);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection()
                    .getInputStream()));
            String s;
            // 一行でJSONが返ってくるので冗長？
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, "GitHubに接続できませんでした", Toast.LENGTH_SHORT);
        }

        return sb.toString();
    }
}
