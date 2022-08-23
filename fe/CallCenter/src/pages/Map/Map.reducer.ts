import * as types from "./Map.constants"
import produce from "immer"

const initialState= {
  refreshPageValue: false,
}

export const mapReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case types.REFRESH_PAGE:
        draft.refreshPageValue = action.payload
        break
      default:
        return state
    }
  })
