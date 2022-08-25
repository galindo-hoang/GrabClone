import axios from "axios"
import {coordinate} from "../../@types/map";
import {instance} from "../Interceptor/ApiService";
import {requestTop5Location} from "../../@types/bookingcar";


const GET_TOP_5_LOCATION_DEPARTURE_RECENT="http://localhost:8085/api/v1/booking/topDepartures"
const GET_TOP_5_LOCATION_DESTINATION_RECENT="http://localhost:8085/api/v1/booking/topDestination"
class BookingService {
  autoComplete(address: String) {
    return axios.get(`https://api.geoapify.com/v1/geocode/autocomplete?text=${address}&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }

  convertCoordinateToAddress(coordinate: coordinate) {
    return axios.get(`https://api.geoapify.com/v1/geocode/reverse?lat=${coordinate.latitude}&lon=${coordinate.longitude}&lang=fr&apiKey=ce49dd1f7cb14bce8674f53bb21ee1c7`)
  }
  top5LocationDepartureRecent(phoneNumber:string){
    return instance.post(GET_TOP_5_LOCATION_DEPARTURE_RECENT,{limit:5,phoneNumber:phoneNumber} as requestTop5Location,{
      headers:{
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      }
    });
  }
  top5LocationDestinationRecent(phoneNumber:string){
    return instance.post(GET_TOP_5_LOCATION_DESTINATION_RECENT,{limit:5,phoneNumber:phoneNumber} as requestTop5Location,{
      headers:{
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      }
    });
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
        price = price +(tempPrice*(distance-2));
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
        price = price +(tempPrice*(distance-2));
        return price;
      }
    }
  }
}


export default new BookingService()