package ru.group12.tinytasks.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.util.database.objects.enums.Category;

public class CustomAdapter extends BaseAdapter {

    private Category[] names;
    private LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, Category[] names) {
        this.names = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int i) {
        return names[i].name();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView tvNames = view.findViewById(R.id.textView);
        tvNames.setText(names[i].getName());
        return view;
    }
}