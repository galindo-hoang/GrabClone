import React, {useState} from "react"
import { BrowserRouter } from "react-router-dom"
import ProductRoutes from "./ProductRoutes"
import LoginRoutes from "./LoginRoutes"
import HomeRoutes from "./HomeRoutes"
import BookingCarRoutes from "./BookingCarRoutes";
import UserInfoRoutes from "./UserInfoRoutes";
import MapRoutes from "./MapRoutes";

export default function Routes() {
  return (
    <BrowserRouter>
      <LoginRoutes />
      <UserInfoRoutes/>
      <HomeRoutes />
      <BookingCarRoutes/>
      <ProductRoutes />
      <MapRoutes/>
    </BrowserRouter>
  )
}
