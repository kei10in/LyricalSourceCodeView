package lyrical.sourcecodeview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> mItems;
    private LayoutInflater mInflater;

    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> items) {
        super(context, textViewResourceId, items);
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        Item item = (Item) mItems.get(position);
        if (null != item) {
            TextView name = (TextView) view.findViewById(android.R.id.text1);
            Drawable icon;
            if (null == item.getHash() || 0 == item.getHash().length()) {
                icon = IconCache.getInstance().getDir();
            } else {
                icon = IconCache.getInstance().getFile();
            }
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            name.setCompoundDrawables(icon, null, null, null);

            name.setTypeface(Typeface.DEFAULT_BOLD);
            name.setText(item.getFileName());
        }
        return view;
    }
}
