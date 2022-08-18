import axios from "axios";
import {createBooking} from "../../@types/bookingcar";
import {instance} from "../Interceptor/ApiService";

const CREATE_BOOKING_URL="http://localhost:8086/api/v1/booking/create_booking";
const FINISH_BOOKING_URL="http://localhost:8086/api/v1/booking/finish_ride";

class ProcessBookingService {
  createBooking= async (payload:createBooking)=>{
    return await instance.post(CREATE_BOOKING_URL,payload)
  }
  finishBooking=async()=>{
   return await instance.post(FINISH_BOOKING_URL,{userId:123} as object)
  }
}


export default new ProcessBookingService()