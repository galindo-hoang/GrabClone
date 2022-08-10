import * as actions from "./BookingCar.actions"
import {localtion} from "../../@types/bookingcar"
export const bookingCar = (localtion: localtion) => async dispatch => {
  return dispatch(actions.bookingCarRequested(localtion));
}