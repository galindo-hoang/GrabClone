import axios from "axios"
import { coordinate } from "src/@types/map";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";

class MapService {
  /*convertAddressToCoordinate(address: String,accessToken:String){
      return axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${address}.json?access_token=${accessToken}`);
  }*/
  getDistanceCarMethod(address1:coordinate, address2:coordinate){
    console.log(`https://api.mapbox.com/directions/v5/mapbox/driving/${address1?.longitude},${address1?.latitude};${address2?.longitude},${address2?.latitude}.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=${accessToken}`);
    return axios.get(`https://api.mapbox.com/directions/v5/mapbox/driving/${address1?.longitude},${address1?.latitude};${address2?.longitude},${address2?.latitude}.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=${accessToken}`)
  }
  getDistanceMotoMethod(address1:coordinate, address2:coordinate){
    return axios.get(`https://api.mapbox.com/directions/v5/mapbox/cycling/${address1?.longitude},${address1?.latitude};${address2?.longitude},${address2?.latitude}.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=${accessToken}`)
  }

  testDistance(){
    return axios.get(`https://api.geoapify.com/v1/routing?waypoints=50.96209827745463%2C4.414458883409225%7C50.429137079078345%2C5.00088081232559&mode=drive&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }
  getDistanceMoto(address1:coordinate, address2:coordinate){
    return axios.get(`https://api.geoapify.com/v1/routing?waypoints=${address1.latitude}%2C${address1.longitude}%7C${address2.latitude}%2C${address2.longitude}&mode=drive&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }

}


export default new MapService()