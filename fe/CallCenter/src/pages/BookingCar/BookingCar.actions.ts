import * as types from "./BookingCar.constants"
import {location,info2Location} from "../../@types/bookingcar"
export const bookingCarRequested = (payload:info2Location) => ({
  type: types.BOOKING_CAR_REQUEST,
  payload:payload
})