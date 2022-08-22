import axios from "axios"
import { coordinate } from "src/@types/map";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";

class MapService {
  /*convertAddressToCoordinate(address: String,accessToken:String){
      return axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${address}.json?access_token=${accessToken}`);
  }*/
  getDistanceAndRoute(address1:coordinate, address2:coordinate){
    return axios.get(`https://api.geoapify.com/v1/routing?waypoints=${address1.latitude}%2C${address1.longitude}%7C${address2.latitude}%2C${address2.longitude}&mode=drive&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }


}


export default new MapService()