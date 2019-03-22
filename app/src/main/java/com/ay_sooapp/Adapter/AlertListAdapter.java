package com.ay_sooapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ay_sooapp.Model.AlertDetailsData;
import com.ay_sooapp.Model.WebSiteDetails;
import com.ay_sooapp.R;

import java.util.List;

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.AlertListViewHolder> {

    private final static String TAG = AlertListAdapter.class.getName();

    private Context context;
    private List<AlertDetailsData> alertDetailsDataList;
    private AlertListAdapterListener listener;
    private List<WebSiteDetails> webSiteDetailsList;

    public AlertListAdapter(Context context, List<AlertDetailsData> alertDetailsDataList, AlertListAdapterListener listener, List<WebSiteDetails> webSiteDetailsList) {

        this.context = context;
        this.alertDetailsDataList = alertDetailsDataList;
        this.listener = listener;
        this.webSiteDetailsList = webSiteDetailsList;
    }

    @NonNull
    @Override
    public AlertListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_alert_list, parent, false);
        return new AlertListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AlertListViewHolder holder, int position) {
        AlertDetailsData alertDetailsData = alertDetailsDataList.get(position);
        holder.url.setText(alertDetailsData.getUrl());
        holder.website.setText(getWebSite(alertDetailsData.getWebsiteID()));
        holder.alertID.setText(String.valueOf(alertDetailsData.getId()));
        if (alertDetailsData.getPrice() != null) {
            holder.currentPrice.setText(alertDetailsData.getPrice());
        } else {
            holder.currentPrice.setText("N/A");
        }

        Log.e(TAG, alertDetailsData.getUpdatedOn());
        String lastCheckedIn = alertDetailsData.getLastCheckedDate();
        if (!lastCheckedIn.contains("T")) {
            holder.lastCheckedIn.setText(lastCheckedIn);
        } else {
            holder.lastCheckedIn.setText(lastCheckedIn.replace("T", ", ").substring(0, lastCheckedIn.indexOf(".")));
        }
        holder.deleteIcon.setTag(alertDetailsData);
        applyClickEvents(holder, position);

    }

    private String getWebSite(Integer websiteID) {
        for (WebSiteDetails webSiteDetails : webSiteDetailsList) {
            if (webSiteDetails.getId() == websiteID) {
                return webSiteDetails.getName();
            }
        }
        return null;
    }


    private void applyClickEvents(AlertListViewHolder holder, final int position) {
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDetailsData alertDetailData = (AlertDetailsData) v.getTag();
                listener.onDeleteIconClick(alertDetailData);
            }
        });

        holder.checkNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheckKnowClicked(position);
            }
        });
    }

    /*private String getDate(String time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("etc/UTC");
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa", Locale.getDefault());
        Date currentTimeZone = new Date(((long) Long.parseLong(time)) * 1000);
        String date = sdf.format(currentTimeZone);
        Log.e(TAG,date  );
        return date;

    }*/


    @Override
    public int getItemCount() {
        return alertDetailsDataList.size();
    }

    public interface AlertListAdapterListener {
        void onDeleteIconClick(AlertDetailsData alertDetailData);

        void onItemClicked(int position);

        void onCheckKnowClicked(int position);
    }

    public class AlertListViewHolder extends RecyclerView.ViewHolder {
        TextView url;
        TextView website;
        TextView currentPrice;
        TextView lastCheckedIn;
        TextView checkNow;
        ImageView deleteIcon;
        TextView alertID;

        public AlertListViewHolder(View itemView) {
            super(itemView);
            url = (TextView) itemView.findViewById(R.id.tv_articleNo);
            website = (TextView) itemView.findViewById(R.id.tv_website);
            currentPrice = (TextView) itemView.findViewById(R.id.tv_pirce);
            lastCheckedIn = (TextView) itemView.findViewById(R.id.tv_checked_in);
            checkNow = (TextView) itemView.findViewById(R.id.tv_check_now);
            deleteIcon = (ImageView) itemView.findViewById(R.id.iv_delete);
            alertID = (TextView) itemView.findViewById(R.id.tv_alertID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });


        }


    }
}
