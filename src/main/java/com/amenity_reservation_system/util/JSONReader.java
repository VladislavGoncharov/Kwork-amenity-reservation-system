package com.amenity_reservation_system.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class JSONReader {

    public static String getTemperatureByJSON(String api) throws IOException {
        if (api == null) return null;

        URL url = new URL(api);
        // Из всего JSON беру только значение values
        String string = String.valueOf(new ObjectMapper().readTree(url).findValue("values"));
        // нахожу индекс запятой [[1657573520000,24.250999999999998]]
        int i = string.indexOf(',');

        // обрезаю 2 цифры после запятой и получаю температуру
        return string.substring(i + 1, i + 3);
    }
}
