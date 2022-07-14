import { loginApi } from "src/apis/login.api"
import * as actions from "./Login.actions"
import loginService from "../../service/LoginService";




export const login = (payload: ReqLogin) => async dispatch => {
  dispatch(actions.loginRequested());
  return await loginService.postLoginForm(payload)
    .then(res => {
      console.log(res.data)
      localStorage.setItem("token", res.data.accessToken)
      return dispatch(actions.loginSuccess(res))
    })
    .catch(err => {return Promise.reject(dispatch(actions.loginFailed(err)))})
}
