import * as types from "./BookingCar.constants"
import {location, info2Location, bookingCarForm, createBooking} from "../../@types/bookingcar"
export const saveBookingCar = (payload:bookingCarForm) => ({
  type: types.BOOKING_CAR_REQUEST,
  payload:payload
})

export const createBookingCar = (payload:createBooking) => ({
  type: types.CREATE_BOOKING_CAR,
  payload:payload
})