package com.strollingpasta.bingo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Bingo {

    private String id;
    private Date date;
    ArrayList<String> bingoList = new ArrayList<>();
    ArrayList<Boolean> bingoListDone = new ArrayList<>();

    Bingo() { }

    Bingo(Date date, ArrayList<String> bingoList, ArrayList<Boolean> bingoListDone) {
        this.date = date;
        this.bingoList = bingoList;
        this.bingoListDone = bingoListDone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBingoList(ArrayList<String> bingoList) {
        this.bingoList = bingoList;
    }

    public void setBingoListDone(ArrayList<Boolean> bingoListDone) {
        this.bingoListDone = bingoListDone;
    }

    public ArrayList<String> getBingoList() {
        return bingoList;
    }

    public ArrayList<Boolean> getBingoListDone() {
        return bingoListDone;
    }

    @NonNull
    @Override
    public String toString() {
        String string = "id: " + id + ",\n빙고 생성일: " + date + ",\n빙고 대상 목록: " + bingoList.toString() + ",\n빙고 대상 완료: " + bingoListDone.toString();
        return string;
    }

    //false 개수 반환
    public int countListDone() {
        int count = 0;

        for (Boolean b : bingoListDone) {
            if (!b)
                count++;
        }
        return count;
    }

}
