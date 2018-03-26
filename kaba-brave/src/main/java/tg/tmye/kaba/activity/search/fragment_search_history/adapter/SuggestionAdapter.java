package tg.tmye.kaba.activity.search.fragment_search_history.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.List;

/**
 * Created by abiguime on 2016/12/5.
 */

public class SuggestionAdapter<T> extends ArrayAdapter<T> {

    private List<T> items;
    private List<T> filteredItems;
    private ArrayFilter mFilter;

    public SuggestionAdapter(Context context, @LayoutRes int resource, @NonNull List<T> objects) {
//        super(context, resource, Lists.<T>newArrayList());
        super(context, resource, objects);
        this.items = objects;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    public int getCount() {
        //todo: change to pattern-size
        return items.size();
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            //custom-filtering of results
            results.values = items;
            results.count = items.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}