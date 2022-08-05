import React, {useEffect, useRef, useState} from "react"
import MainLayout from "../../layouts/MainLayout";
import {Title} from "./Map.styles"
import ReactMapGL, {GeolocateControl, Layer, Marker, NavigationControl, Popup, Source,} from 'react-map-gl';
import 'mapbox-gl/dist/mapbox-gl.css'
import MapService from "src/service/Map/MapService";
import {coordinate} from "src/@types/map";
import 'antd/dist/antd.css';
import MessageService from "src/service/Message/MessageService";
import {CarOutlined, EnvironmentOutlined} from '@ant-design/icons'
import {COLOR} from "src/constants/styles";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";


const Menu = () => {
  const [toggle, setToggle] = useState(false)
  return (
    <div
      style={{position: "absolute",left:'5px',top:'5px',maxWidth: "320px"}}
    >
      <button className="btn  btn-info btn-sm" style={{position: "relative",}} onClick={() => setToggle(!toggle)}>{toggle?'Hiện thông tin':'Ẩn thông tin'}</button>
      <form className="p-5 rounded-sm shadow text-center info-background" hidden={toggle}>
        <Title>Thông tin đặt xe</Title>
        <label className="float-left mb-1">Địa điểm đón <CarOutlined/> </label>
        <input
          type="text"
          placeholder="Điền số điện thoại"
          className="form-control form-control-lg mb-3"
          readOnly={true}
        />
        <label className="float-left mb-1">Địa chỉ đến <EnvironmentOutlined/></label>
        <input
          type="text"
          placeholder="Điền địa chỉ đi"
          className="form-control form-control-lg mb-3"
          readOnly={true}
        />
        <button type="submit" className="btn btn-block btn-info btn-lg">
          Tìm tài xế
        </button>
      </form>
    </div>
  );
}


const Map = () => {
  const [latitude, setLatitude] = useState(10.76307106505523)
  const [longitude, setLongitude] = useState(106.68214425045026)
  const [zoom, setZoom] = useState(18)
  const [loadMap, setLoadMap] = useState(false)
  const [lineValue, setLineValue] = useState([] as coordinate)
  const [showPopup, togglePopup] = useState(false);

  let coordinate1: coordinate = {
    latitude: 10.76307106505523,
    longitude: 106.68214425045026
  };
  let coordinate2: coordinate = {
    latitude: 10.755820,
    longitude: 106.691449
  }
  useEffect(() => {
    const checkDistance = async () => {
      await MapService.getDistance(coordinate1 as coordinate, coordinate2 as coordinate, accessToken).then((res) => {
        const distance = res.data.routes[0].distance / 1000;
        setLineValue(res.data.routes[0].geometry.coordinates);
      })
    }
    checkDistance();
  }, []);
  useEffect(() => {
    MessageService.openMessage({loading: 'đang tải map', loaded: 'Tải map thành công!'}, loadMap)
  }, [loadMap])
  const [viewState, setViewState] = useState({
    longitude: longitude,
    latitude: latitude,
    zoom: zoom,
  });
  const geoJson: GeoJSON.FeatureCollection<any> = {
    type: 'FeatureCollection',
    features: [
      {
        type: 'Feature',
        geometry: {
          type: 'LineString',
          coordinates: lineValue
        },
        properties: {}
      }
    ]
  };
  const mapRef = useRef(null);
  return <MainLayout>
    <ReactMapGL id="map"
                mapboxAccessToken={accessToken}
                mapStyle="mapbox://styles/mapbox/streets-v11"
                style={{
                  width: "80%",
                  height: "600px",
                  borderRadius: "15px",
                  border: `2px solid ${COLOR.MAIN}`,
                  position:'absolute'
                }}

                {...viewState}
                ref={mapRef}

                onClick={(evt) => {

                }}
                onLoad={(map) => {
                  setLoadMap(true)
                }}


                onZoom={evt => {
                }}
                onMove={evt => {
                  setViewState(evt.viewState)
                }}>
      <Source type='geojson' id='source-geojson' data={geoJson}>
        <Layer
          id="lineLayer"
          type="line"
          source="route"
          layout={{
            "line-join": "round",
            "line-cap": "round"
          }}
          paint={{
            "line-color": "red",
            "line-width": 3
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
          style={{height: 50, width: 50}}
          src="https://library.kissclipart.com/20180925/rpe/kissclipart-map-car-icon-clipart-car-google-maps-navigation-c81a6a2d0ecb7a15.png"
        />
      </Marker>

      <Marker
        latitude={coordinate2.latitude}
        longitude={coordinate2.longitude}>
        <img
          onClick={() => togglePopup(true)}
          style={{height: 50, width: 50}}
          src="https://xuonginthanhpho.com/wp-content/uploads/2020/03/map-marker-icon.png"
        />
      </Marker>
      <GeolocateControl position='top-right'/>
      <NavigationControl position='top-right'/>
      <Menu/>
    </ReactMapGL>
  </MainLayout>
}


export default Map