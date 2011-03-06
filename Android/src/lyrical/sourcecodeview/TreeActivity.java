package lyrical.sourcecodeview;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
        setTitle("/" + mPwd);
        mItemList = new ArrayList<Item>();
        ArrayList<String> added = new ArrayList<String>();

        for (int i = 0, range = mKeys.length; i < range; i++) {
            String str = mKeys[i];
            if (null == mPwd || 0 == mPwd.length()) {
            } else {
                if (!str.startsWith(mPwd)) {
                    continue;
                }
                str = str.replaceFirst(mPwd, "");
            }
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

        System.out.println("+" + mPwd.length());

        ItemAdapter adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, mItemList);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                Item item = (Item) listView.getItemAtPosition(position);
                final Intent intent = new Intent();
                ;
                if (null == item.getHash()) {
                    intent.setClass(TreeActivity.this, TreeActivity.class);
                    intent.putExtra("pwd", mPwd + item.getFileName() + "/");
                    intent.putExtra("name", mName);
                    intent.putExtra("owner", mOwner);
                    intent.putExtra("keys", mKeys);
                    intent.putExtra("values", mValues);
                    startActivity(intent);
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(TreeActivity.this);
                    progressDialog.setTitle("解析中");
                    progressDialog.setMessage("ちょっと待ってね！");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            String result = HttpClient.request(BLOB_SHOW_API + mOwner + "/" + mName
                                    + "/" + mValues[position], TreeActivity.this);
                            String html = lyrical.highlighter.Highlighter.buildHtml(result);
                            System.out.println(html);
                            return html;
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            progressDialog.dismiss();
                            intent.setClass(TreeActivity.this, ViewerActivity.class);
                            intent.putExtra("html", result);
                            startActivity(intent);
                        }
                    }.execute();
                }
            }
        });
    }

}
