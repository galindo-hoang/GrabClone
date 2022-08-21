import axios from "axios"
import {coordinate} from "../../@types/map";


const GET_TOP_5_LOCATION_RECENT=""
class BookingService {
  autoComplete(address: String) {
    return axios.get(`https://api.geoapify.com/v1/geocode/autocomplete?text=${address}&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }

  convertCoordinateToAddress(coordinate: coordinate) {
    return axios.get(`https://api.geoapify.com/v1/geocode/reverse?lat=${coordinate.latitude}&lon=${coordinate.longitude}&lang=fr&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }
  top5LocationRecent(){

  }
  calcPrice(distance: number, typeCar: string) {
    let price = 0;
    if (typeCar.toString().includes("MOTORCYCLE")) {
      if (distance <= 2) {
        return 12500;
      }
      else {
        price+=12500;
        let tempPrice=4300;
        price = price +(tempPrice*distance);
        return price;
      }
    }
    else {
      if (distance <= 2) {
        return 29000
      }
      else
      {
        price+=29000;
        let tempPrice=10000;
        price = price +(tempPrice*distance);
        return price;
      }
    }
  }
}


export default new BookingService()