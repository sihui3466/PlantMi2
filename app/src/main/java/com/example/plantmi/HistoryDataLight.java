package com.example.plantmi;

import java.util.ArrayList;

public class HistoryDataLight implements HistoryDataSource{
    private ArrayList<CardData> historyLightArrayList;

    HistoryDataLight(){
        historyLightArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyLightArrayList.add(c);
    }

    @Override
    public String getHistory(int i) {
        return historyLightArrayList.get(i).getData();
    }

    @Override
    public void removeHistory() {
        historyLightArrayList.remove(0);
    }

    @Override
    public int getSize() {
        return historyLightArrayList.size();
    }

    @Override
    public void clearHistory() {
        historyLightArrayList.clear();
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
