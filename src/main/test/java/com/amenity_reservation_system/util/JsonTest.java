package com.amenity_reservation_system.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class JsonTest {

    public String allResult;

    @Test
    public void testJSON() throws IOException {
        URL url = new URL("http://amenity.hopto.org:8086/query?db=telegraf&epoch=ms&q=SELECT+value/1000-30+FROM+cpu_temperature+limit+1");

        String strings = String.valueOf(new ObjectMapper().readTree(url).findValue("values"));

        int i = strings.indexOf(',');
        strings = strings.substring(i+1,i+3);

        System.out.println(strings);


//        System.out.println(json.at("\"values\""));
//        while (json.elements().hasNext()){

//
//        System.out.println(json.fieldNames().next() + "  1");
//        System.out.println(json.fields().next() + "  2");
//        System.out.println(json.findParent("values") + "  3");
//        System.out.println(json.findValue("values") + "  4");
//        System.out.println(json.get("values") + "  5");
//        System.out.println(json.get(0) + "  6");
//        System.out.println(json.getNodeType() + "  7");
////        System.out.println(json.required("values"));
//        System.out.println(json.path("values") + "  8");

    }

}
