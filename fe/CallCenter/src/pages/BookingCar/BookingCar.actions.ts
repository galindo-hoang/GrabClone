import * as types from "./BookingCar.constants"
import {localtion} from "../../@types/bookingcar"
export const bookingCarRequested = (payload:localtion) => ({
  type: types.BOOKING_CAR_REQUEST,
  payload:payload
})