package org.michenux.drodrolib.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class MCXArrayAdapter<T> extends ArrayAdapter<T> {

    public MCXArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MCXArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MCXArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public MCXArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public MCXArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public MCXArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void setData(List<T> data) {
        clear();
        if (data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setDataApi11(data);
            } else {
                setDataLegacy(data);
            }
        }
    }

    private void setDataLegacy(List<T> data) {
        for (T item : data) {
            add(item);
        }
    }

    @TargetApi(11)
    private void setDataApi11(List<T> data) {
        addAll(data);
    }
}
