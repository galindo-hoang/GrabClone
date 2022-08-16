import * as actions from "./BookingCar.actions"
import {location,info2Location} from "../../@types/bookingcar"
export const bookingCar = (location: info2Location) => async dispatch => {
  localStorage.setItem("location", JSON.stringify(location))
  return dispatch(actions.bookingCarRequested(location));
}
