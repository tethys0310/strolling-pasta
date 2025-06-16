package com.strollingpasta.bingo;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OverpassResponse {
    @SerializedName("elements")
    private List<OverpassElement> overpassElements;

    public List<OverpassElement> getElements() {
        return overpassElements;
    }

    public String printNames() { // 탐지에 걸린 것들 이름 출력
        StringBuilder names = new StringBuilder();

        for (OverpassElement element : overpassElements) {
            names.append(element.getTags().get("name")).append("\n");
        }

        return names.toString();
    }
}

