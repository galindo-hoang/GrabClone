import axios from "axios";
import {createBooking} from "../../@types/bookingcar";
import {instance} from "../Interceptor/ApiService";

const CREATE_BOOKING_URL="http://localhost:8085/api/v1/booking/create_booking";
const FINISH_BOOKING_URL="http://localhost:8085/api/v1/booking/finish_ride";

class ProcessBookingService {
  createBooking= async (payload:createBooking)=>{
    console.log(payload)
    return await instance.post(CREATE_BOOKING_URL,payload,{
      headers:{
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      }
    })
  }

  finishBooking=async()=>{
   return await instance.post(FINISH_BOOKING_URL,{username:localStorage.getItem("userName")} as object,{
     headers:{
       Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
     }
   })
  }
}


export default new ProcessBookingService()