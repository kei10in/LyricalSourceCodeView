package lyrical.sourcecodeview;

import android.graphics.drawable.Drawable;

public class IconCache {
    private static IconCache mInstance;
    private Drawable mFile;
    private Drawable mDir;

    public IconCache() {
    }

    public static synchronized IconCache getInstance() {
        if (null == mInstance) {
            mInstance = new IconCache();
        }
        return mInstance;
    }

    public Drawable getDir() {
        return mDir;
    }

    public Drawable getFile() {
        return mFile;
    }

    public void setDir(Drawable dir) {
        mDir = dir;
    }

    public void setFile(Drawable file) {
        mFile = file;
    }
}