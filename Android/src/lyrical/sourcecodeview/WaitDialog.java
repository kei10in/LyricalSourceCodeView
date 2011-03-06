package lyrical.sourcecodeview;

import android.app.ProgressDialog;
import android.content.Context;

public class WaitDialog extends ProgressDialog{
    public WaitDialog(Context context, String title, String message) {
        super(context);
        setTitle(title);
        setMessage(message);
        setProgressStyle(STYLE_SPINNER);
        setCancelable(false);
        show();
    }
}
