import * as types from "./App.constants"
import { LOGIN_SUCCESS } from "src/pages/Login/Login.constants"
import produce from "immer"

const initialState = {
  isAuthenticated: false,
  closeSideNav: false,
  payloadFCM:null
}

export const AppReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case types.LOGOUT:
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        draft.isAuthenticated = false
        break
      case types.RECEIVED_FCM:
        console.log(action.payload)
        draft.payloadFCM=action.payload
        break;
      case LOGIN_SUCCESS:
        draft.isAuthenticated = true
        break
      case types.CLOSE_SIDE_NAV:
        draft.closeSideNav = !state.closeSideNav
        break
      case types.CLEAR_FCM:

        break
      default:
        return state
    }
  })
