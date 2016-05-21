package adry.graph.backup.sms;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Audrey on 14/05/2016.
 * Adapter for the sms list
 */
public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.ViewHolder>{

    private List<SMSData> mSmsList;
    private final SimpleDateFormat mDateFormatter;

    public SmsListAdapter() {
        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sms, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) v.getLayoutParams();
        lp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, parent.getContext().getResources().getDisplayMetrics());
        return new ViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SMSData data = mSmsList.get(position);
        String titleText = "<b>" + data.getNumber() + "</b>";
        if (data.getDateSent() != 0L) {
            titleText += " - " + mDateFormatter.format(data.getDateSent());
        }
        if (data.getDateReceived() != 0L) {
            titleText += " " + mDateFormatter.format(data.getDateReceived());
        }
        holder.mNumberTextView.setText(Html.fromHtml(titleText));

        holder.mMessageTextView.setText(data.getMessage());
    }

    @Override
    public int getItemCount() {
        return mSmsList == null ? 0 : mSmsList.size();
    }

    public void setSmsList(List<SMSData> smsList) {
        mSmsList = smsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNumberTextView;
        public TextView mMessageTextView;
        public ViewHolder(View v) {
            super(v);
            mNumberTextView = (TextView) v.findViewById(R.id.item_sms_number_tv);
            mMessageTextView = (TextView) v.findViewById(R.id.item_sms_message_tv);
        }
    }
}
