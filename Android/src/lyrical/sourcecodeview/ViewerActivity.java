package lyrical.sourcecodeview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        WebView webview = ((WebView) findViewById(R.id.webview));
        webview.loadDataWithBaseURL("about:blank", getIntent().getExtras().getString("html"),
                "text/html", "utf-8", null);
    }
}
