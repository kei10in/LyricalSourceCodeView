package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TreeActivity extends ListActivity {
    private String[] mValues;
    private String[] mKeys;

    private static final String BLOB_SHOW_API = "http://github.com/api/v2/json/blob/show/";
    private String mName;
    private String mOwner;
    private String mPwd;
    private ArrayList<Item> mItemList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        mName = extras.getString("name");
        mOwner = extras.getString("owner");
        mKeys = extras.getStringArray("keys");
        mValues = extras.getStringArray("values");
        mPwd = extras.getString("pwd");
        mItemList = new ArrayList<Item>();
        ArrayList<String> added = new ArrayList<String>();
        if (null == mPwd || 0 == mPwd.length()) {
            for (int i = 0, range = mKeys.length; i < range; i++) {
                String str = mKeys[i];
                int separatePoint = str.indexOf("/");
                String fileName;
                String hash = null;
                if (-1 == separatePoint) {
                    fileName = str;
                    hash = mValues[i];
                } else {
                    fileName = str.substring(0, separatePoint);
                }
                if (-1 == added.indexOf(fileName)) {
                    Item item = new Item();
                    item.setFileName(fileName);
                    item.setHash(hash);
                    mItemList.add(item);
                    added.add(fileName);
                }
            }
        } else {
            System.out.println("+" + mPwd.length());
        }

        ItemAdapter adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, mItemList);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Item item = (Item) listView.getItemAtPosition(position);
                Intent intent;
                if (null == item.getHash()) {
                    intent = new Intent(TreeActivity.this, TreeActivity.class);
                    intent.putExtra("pwd", mPwd + item.getFileName() + "/");
                    intent.putExtra("name", mName);
                    intent.putExtra("owner", mOwner);
                    intent.putExtra("keys", mKeys);
                    intent.putExtra("values", mValues);
                } else {
                    String result = request(BLOB_SHOW_API + mOwner + "/" + mName + "/"
                            + mValues[position]);
                    String html = lyrical.highlighter.Highlighter.buildHtml(result);
                    intent = new Intent(TreeActivity.this, ViewerActivity.class);
                    intent.putExtra("html", html);
                }
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
