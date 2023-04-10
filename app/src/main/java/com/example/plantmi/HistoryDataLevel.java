package com.example.plantmi;

import java.util.ArrayList;

// class for operations of arraylist containing water tank level data
public class HistoryDataLevel implements HistoryDataSource{
    private ArrayList<CardData> historyLevelArrayList;

    HistoryDataLevel(){
        historyLevelArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyLevelArrayList.add(c);
    }

    @Override
    public String getHistory(int i) {
        return historyLevelArrayList.get(i).getData();
    }

    @Override
    public void removeHistory() {
        historyLevelArrayList.remove(0);
    }

    @Override
    public int getSize() {
        return historyLevelArrayList.size();
    }

    @Override
    public void clearHistory() {
        historyLevelArrayList.clear();
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
