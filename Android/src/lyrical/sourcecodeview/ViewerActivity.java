package lyrical.sourcecodeview;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class ViewerActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
      Spanned marked_up = Html.fromHtml(getIntent().getExtras().getString("html"));
      ((TextView) findViewById(R.id.textView)).setText(marked_up.toString());
    }
}
