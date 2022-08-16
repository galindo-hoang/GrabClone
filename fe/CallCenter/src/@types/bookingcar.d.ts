import {coordinate} from "./map";

export interface bookingCarForm{
  fullName?:string
  phoneNumber?:string
  address?:string
  note?:string
}
export interface location{
  destination?:string,
  departure?:string
}

export interface featuresLocation{
  coordinate?:coordinate,
  value?:string
}

export interface info2Location{
  destination?:featuresLocation,
  departure?:featuresLocation
}

export interface recentPhoneNumber{
  phonenumber?:string,
  date?:any
}

export interface timestamp{
  seconds: number,
  nanoseconds: number,
}