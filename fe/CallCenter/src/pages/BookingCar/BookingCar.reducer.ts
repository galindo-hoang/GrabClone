import * as types from "./BookingCar.constants"
import produce from "immer"
import {createBooking, location, responseCreateBooking} from "../../@types/bookingcar"
import { PATH } from "src/constants/paths";
import {CREATE_BOOKING_CAR} from "./BookingCar.constants";
import {useHistory} from "react-router-dom";
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
export const bookingCarReducer=(state=initialState,action)=>
  produce(state, draft => {
    switch (action.type) {
      case types.BOOKING_CAR_REQUEST:
        draft.departure = action.payload.address.departure;
        draft.destination= action.payload.address.destination;
        break
      case types.CREATE_BOOKING_CAR:
        draft.bookingForm=action.payload;
        break;
      case types.CREATE_BOOKING_CAR_WITHOUT_DRIVER:
        draft.bookingForm.dropoffLocation=action.payload.dropoffLocation;
        draft.bookingForm.pickupLocation=action.payload.pickupLocation;
        draft.bookingForm.typeCar=action.payload.typeCar;
        draft.bookingForm.paymentMethod=action.payload.paymentMethod;
        draft.bookingForm.phonenumber=action.payload.phonenumber;
        draft.bookingForm.updatedAt="";
        draft.bookingForm.passengerUsername=action.payload.username;
        draft.bookingForm.createdAt="";
        draft.bookingForm.price=action.payload.price;
        draft.bookingForm.state="No Driver";
        draft.bookingForm.id=null;
        break
      case types.CLEAR_BOOKING_CAR:
        draft.destination=null;
        draft.departure=null
        Object.keys(draft.bookingForm).forEach(key => {
          if(key.includes("id")) {
            draft.bookingForm[key] = null;
          }
          else {
            draft.bookingForm[key] = undefined;
          }
        });
        break
      default:
        return state;
    }
  })

