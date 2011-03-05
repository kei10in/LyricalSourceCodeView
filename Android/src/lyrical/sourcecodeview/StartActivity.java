package lyrical.sourcecodeview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        IconCache iconCache = IconCache.getInstance();
        iconCache.setDir(getResources().getDrawable(R.drawable.dir));
        iconCache.setFile(getResources().getDrawable(R.drawable.file));
        startActivity(new Intent(this, SearchActivity.class));
        //startActivity(new Intent(this, TreeActivity.class));
    }
}
