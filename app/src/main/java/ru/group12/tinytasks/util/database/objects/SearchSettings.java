package ru.group12.tinytasks.util.database.objects;

import android.os.Parcel;
import android.os.Parcelable;

// Used to help decide which tasks to display after a search
public class SearchSettings implements Parcelable {

    private String priceFrom;
    private String priceTo;

    private String workFrom;
    private String workTo;

    private String distanceTo;

    private String category;

    private String sortBy;
    private String orderBy;

    public SearchSettings(String priceFrom, String priceTo, String workFrom, String workTo, String distanceTo, String category, String sortBy, String orderBy) {
        this.priceFrom = priceFrom.equals("") ? "0" : priceFrom;
        this.priceTo = priceTo.equals("") ? "0" : priceTo;
        this.workFrom = workFrom.equals("") ? "0" : workFrom;
        this.workTo = workTo.equals("") ? "0" : workTo;
        this.distanceTo = distanceTo.equals("") ? "0" : distanceTo;
        this.category = category;
        this.sortBy = sortBy;
        this.orderBy = orderBy;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public String getWorkFrom() {
        return workFrom;
    }

    public String getWorkTo() {
        return workTo;
    }

    public String getDistanceTo() {
        return distanceTo;
    }

    public String getCategory() {
        return category;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    protected SearchSettings(Parcel in) {
        priceFrom = in.readString();
        priceTo = in.readString();
        workFrom = in.readString();
        workTo = in.readString();
        distanceTo = in.readString();
        category = in.readString();
        sortBy = in.readString();
        orderBy = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(priceFrom);
        dest.writeString(priceTo);
        dest.writeString(workFrom);
        dest.writeString(workTo);
        dest.writeString(distanceTo);
        dest.writeString(category);
        dest.writeString(sortBy);
        dest.writeString(orderBy);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SearchSettings> CREATOR = new Parcelable.Creator<SearchSettings>() {
        @Override
        public SearchSettings createFromParcel(Parcel in) {
            return new SearchSettings(in);
        }

        @Override
        public SearchSettings[] newArray(int size) {
            return new SearchSettings[size];
        }
    };
}