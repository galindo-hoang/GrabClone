import * as types from "./Map.constants"
import produce from "immer"

const initialState= {
  loading: false,
  clientInformation: null as Object | null
}

export const loginReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case types.LOGIN_REQUESTED:
        draft.loading = true
        draft.clientInformation= action.payload;
        break
      case types.LOGIN_SUCCESS:
        draft.loading = false
        draft.clientInformation= action.payload;
        break
      case types.LOGIN_FAILED:
        draft.loading = false
        draft.clientInformation= action.payload;
        break
      default:
        return state
    }
  })
