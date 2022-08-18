import {createBooking, featuresLocation} from "./bookingcar";

export interface coordinate {
  longitude?: number,
  latitude?: number
}

export interface drawerBooking{
  
}

export interface initialCreateBooking {
  departure: featuresLocation,
  destination: featuresLocation,
  bookingForm:createBooking
}


