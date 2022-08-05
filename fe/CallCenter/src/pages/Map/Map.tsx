import React, {useState,useEffect,useRef} from "react"
import MainLayout from "../../layouts/MainLayout";
import ReactMapGL, {
  Marker,
  useMap,
  NavigationControl,
  GeolocateControl,
  FullscreenControl,
  Popup,
  MapboxGeoJSONFeature,
  Layer,
  Source,
} from 'react-map-gl';
import 'mapbox-gl/dist/mapbox-gl.css'
import MapService from "src/service/Map/MapService";
import { coordinate } from "src/@types/map";
import {
  Editor,
  EditingMode,
  DrawLineStringMode,
  DrawPolygonMode
} from "react-map-gl-draw";
import { geoJSON } from 'leaflet';
import * as geojson from 'geojson';
import { DatePicker } from "antd";
import MessageService from "src/service/Message/MessageService";
const accessToken="pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";


const Modal=()=>{
  return (
    <div
      style={{ position: "absolute", top: 0, right: 0, maxWidth: "320px" }}
    >
      <select >
        <option value="">--Please choose a draw mode--</option>
        <option value="">--Please choose a draw mode--</option>
        <option value="">--Please choose a draw mode--</option>
      </select>

    </div>
  );
}




const Map = () => {
  const [latitude, setLatitude] = useState(10.76307106505523)
  const [longitude, setLongitude] = useState(106.68214425045026);
  const [zoom,setZoom]=useState(18);
  const [loadMap,setLoadMap]=useState(false);
  let coordinate1:coordinate={
    latitude:10.76307106505523,
    longitude:106.68214425045026
  };
  let coordinate2:coordinate={
    latitude:10.755820,
    longitude:106.691449
  }
  const [lineValue,setLineValue]=useState([] as coordinate)

  useEffect(()=>{
  const checkDistance= async ()=>{
    await MapService.getDistance(coordinate1 as coordinate,coordinate2 as coordinate, accessToken).then((res)=>{
      const distance=res.data.routes[0].distance/1000;
      setLineValue(res.data.routes[0].geometry.coordinates);
    })
  }
  checkDistance();
  },[]);

  useEffect(()=>{
    MessageService.openMessage({loading:'loading map', loaded:'loaded map success !'},loadMap)
  },[loadMap])



  const [viewState, setViewState] = useState({
    longitude: longitude,
    latitude: latitude,
    zoom: zoom,

  });


  const geoJson:GeoJSON.FeatureCollection<any>  = {
    type: 'FeatureCollection',
    features: [
      {
        type: 'Feature',
        geometry: {
          type: 'LineString',
          coordinates:lineValue
        },
        properties: {}
      }
    ]
  };

  const [showPopup, togglePopup] = React.useState(false);
  const mode = React.useState(new DrawPolygonMode());
  const mapRef = useRef(null);
  return <MainLayout>
    <ReactMapGL id="map"
      mapboxAccessToken={accessToken}
      mapStyle="mapbox://styles/mapbox/streets-v11"
      style={{
        width: "100%",
        height: "600px",
        borderRadius: "15px",
        border: "2px solid red",
      }}

      {...viewState}
      ref={mapRef}

      onClick={(evt)=>{

      }}
      onLoad={(map)=>{
        setLoadMap(true)
      }}


      onZoom={evt =>{
        setZoom(evt.viewState.zoom)
      }}

      onMove={evt=>{
        setLatitude(evt.viewState.latitude)
        setLongitude(evt.viewState.longitude);
        setZoom(evt.viewState.zoom)
        setViewState(evt.viewState)}}>
    <Source type='geojson' id='source-geojson'  data={geoJson} >
      <Layer
        id="lineLayer"
        type="line"
        source="route"
        layout={{
          "line-join": "round",
          "line-cap": "round"
        }}
        paint={{
          "line-color": "rgba(160,3,238,0.5)",
          "line-width": 5
        }}
      />
    </Source>
      {showPopup && (
        <Popup
          latitude={latitude}
          longitude={longitude}
          closeButton={true}
          closeOnClick={true}
          onClose={() => togglePopup(false)}
          anchor="top-right"
        >
          <div>Pop up marker</div>
        </Popup>
      )}


      <Marker
        latitude={coordinate1.latitude}
        longitude={coordinate1.longitude}>
        <img
          onClick={() => togglePopup(true)}
          style={{ height: 50, width: 50 }}
          src="https://xuonginthanhpho.com/wp-content/uploads/2020/03/map-marker-icon.png"
        />
      </Marker>

      <Marker
        latitude={coordinate2.latitude}
        longitude={coordinate2.longitude}>
        <img
          onClick={() => togglePopup(true)}
          style={{ height: 50, width: 50 }}
          src="https://xuonginthanhpho.com/wp-content/uploads/2020/03/map-marker-icon.png"
        />
      </Marker>
      <GeolocateControl position= 'top-left'/>
      <NavigationControl position= 'top-left'/>

      <Modal></Modal>

    </ReactMapGL>
</MainLayout>
}


export default Map