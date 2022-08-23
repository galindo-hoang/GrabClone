import * as types from "./Map.constants"
import {responseCreateBooking} from "../../@types/bookingcar";


export const refreshPage=(payload:boolean) =>({
  type:types.REFRESH_PAGE,
  payload:payload
})
