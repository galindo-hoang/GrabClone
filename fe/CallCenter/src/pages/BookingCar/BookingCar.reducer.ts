import * as types from "./BookingCar.constants"
import produce from "immer"
import {localtion} from "../../@types/bookingcar"
const initialState= {
  departure:null,
  destination:null,
}

export const bookingCarReducer=(state=initialState,action)=>
  produce(state, draft => {
    switch (action.type) {
      case types.BOOKING_CAR_REQUEST:
        draft.departure = action.payload.departure;
        draft.destination= action.payload.destination;
        break
      default:
        return state;
    }
  })

