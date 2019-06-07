package ru.group12.tinytasks.util.database.objects;

public class Notification implements Comparable {

    private Long date;
    private String senderUID;
    private String senderName;
    private String task;

    public Notification(long date, String senderUID, String senderName, String task) {
        this.date = date;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.task = task;
    }

    public Long getDate() {
        return date;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTaskUUID() {
        return task;
    }

    @Override
    public int compareTo(Object o) {
        return date.compareTo(((Notification) o).getDate());
    }
}
