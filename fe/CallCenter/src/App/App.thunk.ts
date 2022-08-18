import * as actions from "./App.actions"

export const receivedPayload = (booking: object) => async dispatch => {
  return dispatch(actions.receivedFCM(booking));
}
