import axios from 'axios';
import LoginService from '../Login/LoginService';
import {useHistory} from "react-router-dom";
import {PATH} from "../../constants/paths";


export const instance =axios.create({
  headers: {
  },
})
instance.interceptors.response.use(async response =>{
 if(response.status===200){
   return response;
 }
 if(response.status===401){
   console.log(401)
 }
 if(response.status===403){
   console.log(403)
 }
}, async error => {
  if(error?.response?.status===401){
    const refreshToken=localStorage.getItem('refreshToken');
    await LoginService.getRefreshToken(refreshToken||"").then(res=>{
      console.log("access"+res)
      localStorage.setItem("accessToken", res?.data?.accessToken);
    })
  }
  if(error?.response?.status===403){
    const history = useHistory();
    history.push(PATH.LOGIN);
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }
})




instance.interceptors.request.use(request=>{
  return request;
})