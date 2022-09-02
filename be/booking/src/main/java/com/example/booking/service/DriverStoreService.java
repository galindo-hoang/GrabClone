package com.example.booking.service;

import com.example.clients.feign.DriverLocation.DriverLocationDto;
import com.example.booking.model.entity.DriverRecord;
import com.example.booking.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DriverStoreService {
    @Autowired
    private DriverRepository driverRepository;

    public DriverRecord updateLocationService(DriverLocationDto driverRecord, String geohash) {
        DriverRecord driverRecordFindByUsername = driverRepository.findByDriverUsername(driverRecord.getUsername());
        if (driverRecordFindByUsername != null) {
            driverRecordFindByUsername.setLocation(driverRecord.getLocation());
            driverRecordFindByUsername.setGeoHash(geohash);
            return driverRepository.save(driverRecordFindByUsername);
        } else {
            return driverRepository.save(
                    DriverRecord.builder()
                            .driverUsername(driverRecord.getUsername())
                            .geoHash(geohash)
                            .location(driverRecord.getLocation())
                            .locationUpdateTime(new Date())
                            .build());
        }
    }

    public List<DriverRecord> findNearByGeohash(String geohash){
        return driverRepository.findByGeoHashStartsWith(geohash);
    }

    public List<DriverRecord> driverRecordList() {
        return driverRepository.findAll();
    }
}
