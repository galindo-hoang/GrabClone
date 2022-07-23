import LoginService from "src/service/Login/LoginService";
import * as actions from "../Login/Login.actions";


export const home = () => async dispact=> {
  let token=localStorage.getItem("token")||"he";
  return await LoginService.getListUser(token).then(res=>{
    dispact(actions.loginSuccess)
  });
}

