import * as types from "./App.constants"
import {bookingCarForm} from "../@types/bookingcar";

export const logout = () => ({
  type: types.LOGOUT
})

export const toggleSideNav = () => ({
  type: types.CLOSE_SIDE_NAV
})
export const receivedFCM=(payload:object)=>({
  type:types.RECEIVED_FCM,
  payload:payload
})
