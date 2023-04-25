package com.example.plantmi;

import java.util.ArrayList;

public class HistoryDataSource {
    private ArrayList<CardData> historyDataArrayList;

    HistoryDataSource(){
        historyDataArrayList = new ArrayList<>();
    }

    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyDataArrayList.add(c);
    }

    public String getHistory(int i) {
        return historyDataArrayList.get(i).getData();
    }

    public void removeHistory() {
        historyDataArrayList.remove(0);
    }

    public int getSize() {
        return historyDataArrayList.size();
    }

    public void clearHistory() {
        historyDataArrayList.clear();
    }

    private static class CardData{
        private String data;

        private CardData(String data){
            this.data = data;
        }

        private String getData() {
            return data;
        }

    }
}
