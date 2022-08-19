import * as types from "./BookingCar.constants"
import produce from "immer"
import {createBooking, location, responseCreateBooking, typeArrayBooking} from "../../@types/bookingcar"
const initialState= {
  departure:null,
  destination:null,
  bookingForm:{
    price:undefined,
    typeCar:undefined,
    paymentMethod:undefined,
    dropoffLocation:undefined,
    pickupLocation:undefined,
    state:undefined,
    phonenumber:undefined,
    passengerUsername:undefined,
    id:null,
    createdAt:undefined,
    updatedAt:undefined
  } as responseCreateBooking
}
const initalState:typeArrayBooking[]=[]

export const bookingCarReducer=(state=initialState,action)=>
  produce(state, draft => {
    switch (action.type) {
      case types.BOOKING_CAR_REQUEST:
        draft.departure = action.payload.address.departure;
        draft.destination= action.payload.address.destination;
        break
      case types.CREATE_BOOKING_CAR:
        draft.bookingForm=action.payload;
        break
      default:
        return state;
    }
  })

