import React, {useEffect, useRef, useState} from "react"
import MainLayout from "../../layouts/MainLayout";
import ReactMapGL,{Marker} from 'react-map-gl';
const Map = () => {
  const [latitude,setLatitude]= useState( 10.76307106505523)
  const [longitude,setLongitude]= useState( 106.68214425045026)
  return <MainLayout>
    <ReactMapGL
     /* onViewPortChange={viewport=>setViewPort(viewport)}*/
      mapboxAccessToken="pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ"
      mapStyle="mapbox://styles/mapbox/streets-v11"
      style={{
        width:"100%",
        height:"600px",
      }}
      initialViewState={{
        longitude:longitude,
        latitude:latitude,
        zoom:16,
      }}
    />
  </MainLayout>
}


export default Map