import {coordinate} from "./map";


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

export interface bookingCarForm {
  phoneNumber?: string
  address?: info2Location
  typeCar?: string
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

export interface responseCreateBooking {
  createdAt?: string,
  pickupLocation?: coordinate,
  dropoffLocation?:coordinate,
  id:?number,
  phonenumber?:string,
  state?:string,
  updatedAt?:string
  typeCar?: string,
  paymentMethod?: string,
  price?: number,
  passengerUsername?: string,
}

export interface responseUpdatedLocationDriver {
  ride: {
    driverLocation: {
      username?: string,
      location?: coordinate
    },
    rideId?:string
  },
}
export interface responseAcceptedRider{
  bookingId?:number,
}

export interface responseFinishedRide{
  startTime?:string,
  endTime?:string,
  rideId?:string,
}

export interface typeArrayBooking{
  departure:featuresLocation
  destination:featuresLocation,
  bookingForm: responseCreateBooking
}
