import {combineReducers, Reducer} from "redux"
import { AppReducer } from "src/App/App.reducer"
import { loginReducer } from "src/pages/Login/Login.reducer"
import {homeReducer} from "../pages/Home/Home.reducer";
import { ProductListReducer } from "src/pages/Product/ProductList/ProductList.reducer"
import { productItemReducer } from "src/pages/Product/ProductItem/ProductItem.reducer"
import { bookingCarReducer } from "src/pages/BookingCar/BookingCar.reducer";
import UserInfo from "../pages/UserInfo/UserInfo";
import {mapReducer} from "../pages/Map/Map.reducer";

const rootReducer = combineReducers({
  app: AppReducer,
  home:homeReducer,
  login: loginReducer,
  map:mapReducer,
  bookingCar:bookingCarReducer as Reducer,
  productList: ProductListReducer,
  productItem: productItemReducer
})

export default rootReducer
