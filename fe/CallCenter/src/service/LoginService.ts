import axios from 'axios';
import {promises} from "dns";

const LOGIN_FORM_API_URL = "http://localhost:8080/login";
const config={
  headers:{
    'Content-Type': 'multipart/form-data',
  },
  crossOrigin:{
    'Access-Control-Allow-Origin':'*',
  },
  mode:{
    'mode':'cors'
  }
}
class LoginService {
   postLoginForm({username, password}: ReqLogin) {
     const loginElement = {username: username, password: password} as ReqLogin;
     console.log(loginElement);
     return axios.post(LOGIN_FORM_API_URL, loginElement, config)
   }
}

export default new LoginService()

