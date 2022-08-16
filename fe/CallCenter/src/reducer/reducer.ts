import { combineReducers } from "redux"
import { AppReducer } from "src/App/App.reducer"
import { loginReducer } from "src/pages/Login/Login.reducer"
import {homeReducer} from "../pages/Home/Home.reducer";
import { ProductListReducer } from "src/pages/Product/ProductList/ProductList.reducer"
import { productItemReducer } from "src/pages/Product/ProductItem/ProductItem.reducer"
import { bookingCarReducer } from "src/pages/BookingCar/BookingCar.reducer";

const rootReducer = combineReducers({
  app: AppReducer,
  home:homeReducer,
  login: loginReducer,
  bookingCar:bookingCarReducer,
  productList: ProductListReducer,
  productItem: productItemReducer
})

export default rootReducer
