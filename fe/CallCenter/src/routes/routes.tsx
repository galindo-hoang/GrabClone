import React, {useState} from "react"
import { BrowserRouter } from "react-router-dom"
import ProductRoutes from "./ProductRoutes"
import LoginRoutes from "./LoginRoutes"
import HomeRoutes from "./HomeRoutes"
import BookingCarRoutes from "./BookingCarRoutes";
import HistoryRoutes from "./HistoryRoutes";
import MapRoutes from "./MapRoutes";

export default function Routes() {
  return (
    <BrowserRouter>
      <LoginRoutes />
      <HistoryRoutes/>
      <HomeRoutes />
      <BookingCarRoutes/>
      <ProductRoutes />
      <MapRoutes/>
    </BrowserRouter>
  )
}
