import axios from 'axios';
import LoginService from '../Login/LoginService';
import {useHistory} from "react-router-dom";
import {PATH} from "../../constants/paths";
import {MessageWarningService} from "../Message/MessageService";

export const instance =axios.create({
  headers:{
  }
})
instance.interceptors.response.use(async response =>{
  console.log(response)
 if(response.status===200){
   return response;
 }
 if(response.status===401){
   console.log(401)
 }
 if(response.status===403){
   console.log(403)
 }
  if(response.status===400){
  }
}, async error => {
  //AccessToken InValid
  if(error?.response?.data==="Invalid token"){
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }
  //AccessToken Het Han
  if(error?.response?.status===401){
    const refreshToken=localStorage.getItem('refreshToken');
    await LoginService.getRefreshToken(refreshToken||"").then(res=>{
      MessageWarningService.getInstance("Đã gia hạn thành công access token");
      localStorage.setItem("accessToken", res?.data?.accessToken);
    })
  }
  //RefreshToken Het Han
  if(error?.response?.status===403){
    const history = useHistory();
    history.push(PATH.LOGIN);
    MessageWarningService.getInstance("refresh token het han")
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }
})




instance.interceptors.request.use(request=>{
  return request;
})