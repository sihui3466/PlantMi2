package com.example.plantmi;

import java.util.ArrayList;

public class HistoryDataTemp implements HistoryDataSource{
    private ArrayList<CardData> historyTempArrayList;

    HistoryDataTemp(){
        historyTempArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyTempArrayList.add(c);
    }

    @Override
    public String getHistory(int i) {
        return historyTempArrayList.get(i).getData();
    }

    @Override
    public void removeHistory() {
        historyTempArrayList.remove(0);
    }

    @Override
    public int getSize() {
        return historyTempArrayList.size();
    }

    @Override
    public void clearHistory() {
        historyTempArrayList.clear();
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

