package com.example.plantmi;

public interface HistoryDataSource {
    void addHistory(String s);
    String getHistory(int i);
    void removeHistory();
    int getSize();
    void clearHistory();
}
