package adry.graph.backup.sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

/**
 * Created by Audrey on 22/05/2016.
 * ArrayAdapter containing export formats
 */
public class FormatArrayAdapter extends ArrayAdapter<ExportFormat> {

    public FormatArrayAdapter(Context context) {
        super(context, 0, ExportFormat.values());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView text = (CheckedTextView) convertView;

        if (text == null) {
            text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        text.setText(getItem(position).getLabel());

        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CheckedTextView text = (CheckedTextView) convertView;

        if (text == null) {
            text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        text.setText(getItem(position).getLabel());

        return text;
    }
}