package com.example.plantmi;

import java.util.ArrayList;

public class HistoryDataHumid implements HistoryDataSource{
    private ArrayList<CardData> historyHumidArrayList;

    HistoryDataHumid(){
        historyHumidArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyHumidArrayList.add(c);
    }

    @Override
    public String getHistory(int i) {
        return historyHumidArrayList.get(i).getData();
    }

    @Override
    public void removeHistory() {
        historyHumidArrayList.remove(0);
    }

    @Override
    public int getSize() {
        return historyHumidArrayList.size();
    }

    @Override
    public void clearHistory() {
        historyHumidArrayList.clear();
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

