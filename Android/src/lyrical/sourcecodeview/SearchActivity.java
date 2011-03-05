package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity {
    private static final String BRANCHE = "master";
    private static final String BLOB_ALL_API = "http://github.com/api/v2/json/blob/all/";
    private static final String SEARCH_API = "http://github.com/api/v2/json/repos/search/";
    private ArrayList<Repositorie> mRepositorieList;
    private RepositorieAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRepositorieList = new ArrayList<Repositorie>();
        adapter = new RepositorieAdapter(this, R.layout.repositorie_row, mRepositorieList);
        ListView listView = (ListView) findViewById(R.id.repositorieListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
                progressDialog.setTitle("取得中");
                progressDialog.setMessage("ちょっと待ってね！");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                
                ListView listView = (ListView) parent;
                Repositorie repo = (Repositorie) listView.getItemAtPosition(position);
                ArrayList<String> keyList = new ArrayList<String>();
                ArrayList<String> valueList = new ArrayList<String>();
                try {
                    String result = request(BLOB_ALL_API + repo.getOwner() + "/" + repo.getName()
                            + "/" + BRANCHE);
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
                Intent intent = new Intent(SearchActivity.this, TreeActivity.class);
                intent.putExtra("name", repo.getName());
                intent.putExtra("owner", repo.getOwner());
                intent.putExtra("keys", keyList.toArray(new String[0]));
                intent.putExtra("values", valueList.toArray(new String[0]));
                intent.putExtra("pwd", "");
                startActivity(intent);
                progressDialog.dismiss();
            }
        });
    }

    public void onSearch(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("検索中");
        progressDialog.setMessage("ちょっと待ってね！");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                searchTask();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();

            }
        }.execute();

    }

    private void searchTask() {
        EditText editText = (EditText) findViewById(R.id.searchEditText);
        String result = request(SEARCH_API + editText.getText().toString());

        try {
            mRepositorieList.clear();
            JSONObject json = new JSONObject(result);
            JSONArray jsons = (JSONArray) json.get("repositories");
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject jsonObj = jsons.getJSONObject(i);
                Repositorie repo = new Repositorie();
                repo.setDescription(jsonObj.getString("description"));
                repo.setName(jsonObj.getString("name"));
                repo.setOwner(jsonObj.getString("owner"));
                mRepositorieList.add(repo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
