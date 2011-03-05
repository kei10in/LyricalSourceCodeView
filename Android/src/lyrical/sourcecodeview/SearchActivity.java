package lyrical.sourcecodeview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity {
    private static final String SEARCH_API = "http://github.com/api/v2/json/repos/search/";
    private ArrayList<Repositorie> mRepositorieList;
    private RepositorieAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRepositorieList = new ArrayList<Repositorie>();
        adapter = new RepositorieAdapter(this, R.layout.repositorie_row, mRepositorieList);
        ((ListView)findViewById(R.id.repositorieListView)).setAdapter(adapter);
    }

    public void onSearch(View view) {
        EditText editText = (EditText) findViewById(R.id.searchEditText);
        String result = doSearchApi(editText.getText().toString());

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
        adapter.notifyDataSetChanged();
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
                sb.append(s);
            }
            reader.close();
        } catch (IOException e) {
            Toast.makeText(this, "GitHubに接続できませんでした", Toast.LENGTH_SHORT);
        }
        return sb.toString();
    }
}
