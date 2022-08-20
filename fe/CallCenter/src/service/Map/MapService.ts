import axios from "axios"
import { coordinate } from "src/@types/map";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";

class MapService {
  /*convertAddressToCoordinate(address: String,accessToken:String){
      return axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${address}.json?access_token=${accessToken}`);
  }*/
  getDistance(address1:coordinate,address2:coordinate){
    console.log(`https://api.mapbox.com/directions/v5/mapbox/driving/${address1?.longitude},${address1?.latitude};${address2?.longitude},${address2?.latitude}.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=${accessToken}`);
    return axios.get(`https://api.mapbox.com/directions/v5/mapbox/driving/${address1?.longitude},${address1?.latitude};${address2?.longitude},${address2?.latitude}.json?alternatives=true&geometries=geojson&language=en&overview=simplified&steps=true&access_token=${accessToken}`)
  }
}


export default new MapService()