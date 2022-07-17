import * as types from "./Home.constants"
import produce from "immer"

const initialState = {
  loading: false
}

export const homeReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case types.HOME_REQUESTED:
        draft.loading=true
       /* draft.clientInformation= action.payload.data;*/
        break
      default:
        return state
    }
  })
