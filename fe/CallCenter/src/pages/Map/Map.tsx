import React, {useState} from "react"
import MainLayout from "../../layouts/MainLayout";
import ReactMapGL, {Marker,NavigationControl,GeolocateControl,FullscreenControl,Popup} from 'react-map-gl';


const Map = () => {
  const [latitude, setLatitude] = useState(10.76307106505523)
  const [longitude, setLongitude] = useState(106.68214425045026);
  const [zoom,setZoom]=useState(18);
  let lat=10.76307106505523
  let long=106.68214425045026
  const [viewState, setViewState] = useState({
    longitude: longitude,
    latitude: latitude,
    zoom: zoom
  });
  const [showPopup, togglePopup] = React.useState(false);
  return <MainLayout>
    <ReactMapGL
      mapboxAccessToken="pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ"
      mapStyle="mapbox://styles/mapbox/streets-v11"
      style={{
        width: "100%",
        height: "600px",
        borderRadius: "15px",
        border: "2px solid red",
      }}
      {...viewState}
      onZoom={evt =>{
        setLatitude(evt.viewState.latitude)
        setLongitude(evt.viewState.longitude);
        setZoom(evt.viewState.zoom)
        setViewState(evt.viewState)
      }}
      onMove={evt=>{
        setLatitude(evt.viewState.latitude)
        setLongitude(evt.viewState.longitude);
        console.log(evt.viewState)
        setZoom(evt.viewState.zoom)
        setViewState(evt.viewState)}}>
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
        latitude={lat}
        longitude={long}
      >
        <img
          onClick={() => togglePopup(true)}
          style={{ height: 50, width: 50 }}
          src="https://xuonginthanhpho.com/wp-content/uploads/2020/03/map-marker-icon.png"
        />
      </Marker>
      <NavigationControl/>

    </ReactMapGL>
</MainLayout>
}


export default Map