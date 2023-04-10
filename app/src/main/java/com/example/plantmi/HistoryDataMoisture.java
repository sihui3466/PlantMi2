package com.example.plantmi;

import java.util.ArrayList;

// class for operations of arraylist containing moisture data
public class HistoryDataMoisture implements HistoryDataSource{
    private ArrayList<CardData> historyMoistureArrayList;

    HistoryDataMoisture(){
        historyMoistureArrayList = new ArrayList<>();
    }

    @Override
    public void addHistory(String s) {
        CardData c = new CardData(s);
        historyMoistureArrayList.add(c);
    }

    @Override
    public String getHistory(int i) {
        return historyMoistureArrayList.get(i).getData();
    }

    @Override
    public void removeHistory() {
        historyMoistureArrayList.remove(0);
    }

    @Override
    public int getSize() {
        return historyMoistureArrayList.size();
    }

    @Override
    public void clearHistory() {
        historyMoistureArrayList.clear();
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
