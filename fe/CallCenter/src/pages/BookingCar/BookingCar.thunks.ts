import * as actions from "./BookingCar.actions"
import {location, info2Location, bookingCarForm, createBooking} from "../../@types/bookingcar"
import ProcessBookingService from "../../service/BookingCar/ProcessBookingService";
export const saveAddressBooking = (booking: bookingCarForm) => async dispatch => {
  return dispatch(actions.saveAddressBooking(booking));
}


export const createBookingCar = (payload:createBooking) => async dispatch => {
  ProcessBookingService.createBooking(payload)
    .then(reponse=>{console.log(reponse)})
    .catch(error=>console.log(error))
  return dispatch(actions.createBookingCar(payload));
}
