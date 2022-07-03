import React from "react"
import { BrowserRouter } from "react-router-dom"
import ProductRoutes from "./ProductRoutes"
import LoginRoutes from "./LoginRoutes"
import HomeRoutes from "./HomeRoutes"
import BookingCarRoutes from "./BookingCarRoutes";
import HistoryRoutes from "./HistoryRoutes";

export default function Routes() {
  return (
    <BrowserRouter>
      <HistoryRoutes/>
      <HomeRoutes />
      <BookingCarRoutes/>
      <ProductRoutes />
      <LoginRoutes />
    </BrowserRouter>
  )
}
