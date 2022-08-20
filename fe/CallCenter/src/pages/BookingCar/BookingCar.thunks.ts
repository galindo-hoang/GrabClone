import * as actions from "./BookingCar.actions"
import {location, info2Location, bookingCarForm, createBooking,responseCreateBooking} from "../../@types/bookingcar"
import ProcessBookingService from "../../service/BookingCar/ProcessBookingService";
import MapService from "../../service/Map/MapService";
import {coordinate} from "../../@types/map";
import BookingService from "../../service/BookingCar/BookingService";
export const saveAddressBooking = (booking: bookingCarForm) => async dispatch => {
  return dispatch(actions.saveAddressBooking(booking));
}


export const createBookingCar = (payload:createBooking) => async dispatch => {
  MapService.getDistanceCarMethod(payload.pickupLocation as coordinate, payload.dropoffLocation as coordinate).then(res=>{
    const distance = res.data.routes[0].distance / 1000;
    var result = Math.round(distance*100)/100;
    payload.price=BookingService.calcPrice(result,payload.typeCar as string);
  }).then(()=>{
    ProcessBookingService.createBooking(payload)
      .then(reponse=>{
        return dispatch(actions.createBookingCar(reponse.data as responseCreateBooking));
      })
      .catch(error=>console.log(error))
    }
  )
}
export const clearBookingCar=()=>async dispatch=>{
  return dispatch(actions.clearBookingCar());
}
