import * as types from "./BookingCar.constants"
import produce from "immer"
import {createBooking, location} from "../../@types/bookingcar"
const initialState= {
  departure:null,
  destination:null,
  // bookingForm:{
  //   username:undefined,
  //   price:undefined,
  //   paymentMethod:undefined,
  //   dropoffLongitude:undefined,
  //   dropoffLatitude:undefined,
  //   typeCar:undefined,
  //   pickupLongitude:undefined,
  //   pickupLatitude:undefined,
  //   phonenumber:undefined
  // } as createBooking
}

export const bookingCarReducer=(state=initialState,action)=>
  produce(state, draft => {
    switch (action.type) {
      case types.BOOKING_CAR_REQUEST:
        draft.departure = action.payload.address.departure;
        draft.destination= action.payload.address.destination;
        break
      case types.CREATE_BOOKING_CAR:
        // draft.bookingForm=action.payload
        break
      default:
        return state;
    }
  })

