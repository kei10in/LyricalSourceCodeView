package lyrical.sourcecodeview;

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
                    String result = HttpClient.request(BLOB_ALL_API + repo.getOwner() + "/"
                            + repo.getName() + "/" + BRANCHE, SearchActivity.this);
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
        String word = editText.getText().toString();
        if (0 == word.length()) {
            return;
        }
        String result = HttpClient.request(SEARCH_API + word, this);

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
}
