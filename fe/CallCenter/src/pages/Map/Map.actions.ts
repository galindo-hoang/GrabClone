import * as types from "./Map.constants"

export const loginRequested = payload => ({
  type: types.LOGIN_REQUESTED,
  payload
})

export const loginSuccess = payload => ({
  type: types.LOGIN_SUCCESS,
  payload
})

export const loginFailed = payload => ({
  type: types.LOGIN_FAILED,
  payload
})
