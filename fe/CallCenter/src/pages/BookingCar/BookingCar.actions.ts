import * as types from "./BookingCar.constants"
import {location, info2Location, bookingCarForm, createBooking, responseCreateBooking} from "../../@types/bookingcar"
import {CREATE_BOOKING_CAR, CREATE_BOOKING_CAR_WITHOUT_DRIVER} from "./BookingCar.constants";
export const saveAddressBooking = (payload:bookingCarForm) => ({
  type: types.BOOKING_CAR_REQUEST,
  payload:payload
})

export const createBookingCar = (payload:responseCreateBooking) => {
  console.log(payload)
  return {
  type: types.CREATE_BOOKING_CAR,
  payload:payload
}};

export const clearBookingCar=()=>({
  type:types.CLEAR_BOOKING_CAR
})


export const createBookingCarWithoutDriver=(payload:createBooking)=>({
  type:types.CREATE_BOOKING_CAR_WITHOUT_DRIVER,
  payload:payload
})
