package lyrical.sourcecodeview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RepositorieAdapter extends ArrayAdapter<Repositorie> {
    private ArrayList<Repositorie> mItems;
    private LayoutInflater mInflater;

    public RepositorieAdapter(Context context, int textViewResourceId, ArrayList<Repositorie> items) {
        super(context, textViewResourceId, items);
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = mInflater.inflate(R.layout.repositorie_row, null);
        }
        Repositorie item = (Repositorie) mItems.get(position);
        if (null != item) {
            TextView name = (TextView) view.findViewById(R.id.nameTextView);
            name.setTypeface(Typeface.DEFAULT_BOLD);
            TextView description = (TextView) view.findViewById(R.id.descriptionTextView);
            if (null != name) {
                name.setText(item.getName());
            }
            if (null != description) {
                description.setText(item.getDescription());
            }
        }
        return view;
    }
}
