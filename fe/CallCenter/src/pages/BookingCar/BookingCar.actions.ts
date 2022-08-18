import * as types from "./BookingCar.constants"
import {location, info2Location, bookingCarForm, createBooking, responseCreateBooking} from "../../@types/bookingcar"
export const saveAddressBooking = (payload:bookingCarForm) => ({
  type: types.BOOKING_CAR_REQUEST,
  payload:payload
})

export const createBookingCar = (payload:responseCreateBooking) => ({
  type: types.CREATE_BOOKING_CAR,
  payload:payload
});
