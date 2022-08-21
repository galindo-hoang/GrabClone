import * as actions from "./App.actions"

export const receivedPayload = (booking: object) => async dispatch => {
  return dispatch(actions.receivedFCM(booking));
}

export const clearFCM=()=>async dispatch=>{
  return dispatch(actions.clearFCM())
}
