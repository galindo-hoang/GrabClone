import * as actions from "./BookingCar.actions"
import {location, info2Location, bookingCarForm, createBooking,responseCreateBooking} from "../../@types/bookingcar"
import ProcessBookingService from "../../service/BookingCar/ProcessBookingService";
import MapService from "../../service/Map/MapService";
import {coordinate} from "../../@types/map";
import BookingService from "../../service/BookingCar/BookingService";
import {PATH} from "../../constants/paths";
import {MessageWarningService} from "../../service/Message/MessageService";
import {useHistory} from "react-router-dom";
export const saveAddressBooking = (booking: bookingCarForm) => async dispatch => {
  return dispatch(actions.saveAddressBooking(booking));
}


export const createBookingCar = (payload:createBooking) => async dispatch => {
  MapService.getDistanceAndRoute(payload.pickupLocation as coordinate, payload.dropoffLocation as coordinate).then(res=>{
    const distance = res.data.features[0].properties.distance
    var result = Math.round(distance)/100;
    console.log(result)
    payload.price=BookingService.calcPrice(result,payload.typeCar as string);
  }).then(()=>{
    ProcessBookingService.createBooking(payload)
      .then(async response => {
        return await dispatch(actions.createBookingCar(response.data as responseCreateBooking));
      })
      .catch(async error => {
          const message = MessageWarningService.getInstance("Không có tài xế nào gàn vị trí của bạn");
          return await dispatch(actions.createBookingCarWithoutDriver(payload as createBooking))
        }
        )
    }
  )
}

export const clearBookingCar=()=>async dispatch=>{
  return dispatch(actions.clearBookingCar());
}
