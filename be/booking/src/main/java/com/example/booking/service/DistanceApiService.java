package com.example.booking.service;

import com.example.booking.model.domain.Route;
import com.example.clients.feign.DriverLocation.MapCoordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class DistanceApiService {
    public Double getDistance(MapCoordinate origin, MapCoordinate destination) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();
            String api = String.format("https://api.mapbox.com/directions/v5/mapbox/driving/%s,%s;%s,%s.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ", origin.getLongitude(),
                    origin.getLatitude(), destination.getLongitude(), destination.getLatitude());
            String result = restTemplate.getForObject(api, String.class);
            Map<String, Object> map = mapper.readValue(result, Map.class);
            List<Object> distanceList = (List<Object>) map.get("routes");
            String str = mapper.writeValueAsString(distanceList.get(0));
            Route route = mapper.readValue(str, Route.class);
            return Double.valueOf(route.getDistance());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
