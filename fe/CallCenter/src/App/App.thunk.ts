import * as actions from "./App.actions"

export const receivedPayload = (booking: object) => async dispatch => {
  console.log(booking)
  return dispatch(actions.receivedFCM(booking));
}
