import * as actions from "./Login.actions"
import loginService from "../../service/Login/LoginService";
import {ReqLogin} from "../../@types/login";

export const login = (payload: ReqLogin) => async dispatch => {
  dispatch(actions.loginRequested(null));
  let isAdmin:Boolean=false;
  return await loginService.postLoginForm(payload)
    .then(res => {
      res.data.user.authorities?.forEach(u=>{
        if(u.authority?.includes("ROLE_ADMIN")){
          isAdmin=true;
        }
      });
      if(isAdmin){
        localStorage.setItem("accessToken", res.data.accessToken);
        localStorage.setItem("refreshToken", res.data.refreshToken);
        return dispatch(actions.loginSuccess(res))
      }
      else{
        return dispatch(actions.loginFailed("Bạn không có quyền đăng nhập"))
      }
    })
    .catch(err => {return Promise.reject(dispatch(actions.loginFailed("Tài khoản hoặc mật khẩu bị sai")))})
}
