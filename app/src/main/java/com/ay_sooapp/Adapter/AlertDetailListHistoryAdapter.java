package com.ay_sooapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ay_sooapp.Model.AlertDetailData;
import com.ay_sooapp.Model.AlertItemDetailHistory;
import com.ay_sooapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AlertDetailListHistoryAdapter extends RecyclerView.Adapter<AlertDetailListHistoryAdapter.AlertDetailHistoryViewHolder> {
    private static final String TAG = AlertDetailListHistoryAdapter.class.getName();
    private Context context;
    private List<AlertItemDetailHistory> alertItemDetailHistoryList;
    private List<AlertDetailData> alertDetailDataList;
    private String website;
    private int colorEvenRow;
    private int colorOddRow;


    public AlertDetailListHistoryAdapter(Context context, List<AlertDetailData> alertDetailDataList, String website) {
        this.context = context;
        this.alertDetailDataList = alertDetailDataList;
        this.website = website;
        colorEvenRow = context.getResources().getColor(R.color.colorRow);
        colorOddRow = context.getResources().getColor(R.color.colorWhite);

    }

    @NonNull
    @Override
    public AlertDetailHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_alert_list_details, parent, false);
        return new AlertDetailHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertDetailHistoryViewHolder holder, int position) {
        AlertDetailData alertDetailData = alertDetailDataList.get(position);
        holder.price.setText(alertDetailData.getPrice());
        holder.website.setText(website);
        String lastCheckedIn = alertDetailData.getCreatedOn();
        lastCheckedIn = lastCheckedIn.replace("T", ", ").substring(0, lastCheckedIn.indexOf(".") + 1);
        holder.checkedOn.setText(lastCheckedIn);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(colorEvenRow);

        } else {
            holder.itemView.setBackgroundColor(colorOddRow);
        }

        /*}
        else
        {
            ;
        }*/
    }


    private String getDate(String time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss  aaa", Locale.getDefault());
        Date currentTimeZone = new Date(((long) Long.parseLong(time)) * 1000);
        String date = sdf.format(currentTimeZone);
        Log.e(TAG, date);
        return date;

    }


    @Override
    public int getItemCount() {
        return alertDetailDataList.size();
    }

    public class AlertDetailHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView price;
        TextView website;
        TextView checkedOn;

        public AlertDetailHistoryViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            website = (TextView) itemView.findViewById(R.id.tv_website);
            checkedOn = (TextView) itemView.findViewById(R.id.tv_checked_in);
        }
    }
}
