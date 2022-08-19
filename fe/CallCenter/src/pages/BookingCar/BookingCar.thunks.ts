import * as actions from "./BookingCar.actions"
import {location, info2Location, bookingCarForm, createBooking,responseCreateBooking} from "../../@types/bookingcar"
import ProcessBookingService from "../../service/BookingCar/ProcessBookingService";
export const saveAddressBooking = (booking: bookingCarForm) => async dispatch => {
  return dispatch(actions.saveAddressBooking(booking));
}


export const createBookingCar = (payload:createBooking) => async dispatch => {
  ProcessBookingService.createBooking(payload)
    .then(reponse=>{
      return dispatch(actions.createBookingCar(reponse.data as responseCreateBooking));
    })
    .catch(error=>console.log(error))
}
export const clearBookingCar=()=>async dispatch=>{
  return dispatch(actions.clearBookingCar());
}
