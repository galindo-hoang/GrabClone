import * as actions from "../Map/Map.actions";


export const map = async ()  =>dispatch =>{
}


export const Geocoder=async () =>dispatch=>{
}

export const reFreshPageFunction= (isRefreshPage:boolean)=>async dispatch=>{
  return dispatch(actions.refreshPage(isRefreshPage))
}

