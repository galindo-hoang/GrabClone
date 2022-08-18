import {coordinate} from "./map";

export interface bookingCarForm {
  phoneNumber?: string
  address?: info2Location
  typeCar?: string
}

export interface location {
  destination?: string,
  departure?: string
}

export interface featuresLocation {
  coordinate?: coordinate,
  value?: string
}

export interface info2Location {
  destination?: featuresLocation,
  departure?: featuresLocation
}


export interface recentPhoneNumber {
  phonenumber?: string,
  date?: any
}

export interface timestamp {
  seconds: number,
  nanoseconds: number,
}


export interface createBooking {
  phonenumber?: string,
  pickupLocation?: coordinate,
  dropoffLocation?:coordinate,
  typeCar?: string,
  paymentMethod?: string,
  price?: number,
  username?: string,
}
