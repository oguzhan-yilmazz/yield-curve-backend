package com.project.yieldcurve;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class DataMapper {

  
     //JSONObject içeriğini BondInstrument listesi olarak döndürür.
  
     
    public List<BondInstrument> mapToBondInstrumentList(JSONObject jsonObject) {
        JSONArray itemsArray = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("items");
        List<BondInstrument> bondInstrumentList = new ArrayList<>();

        DateTimeFormatter dTF = 
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                                          .appendPattern("dd-MMM-yy")
                                          .toFormatter(Locale.ENGLISH);

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);

            LocalDate businessDate = LocalDate.parse(item.getString("business_date") , dTF);
            LocalDate startDate = LocalDate.parse(item.getString("start_date") , dTF);
            LocalDate endDate = LocalDate.parse(item.getString("end_date"), dTF);

            bondInstrumentList.add(new BondInstrument(
                businessDate, 
                item.getString("series_id"), 
                item.getDouble("saved_price"), 
                startDate, 
                endDate, 
                item.getDouble("couponrate")
            ));
        }

        return bondInstrumentList;
    }
}