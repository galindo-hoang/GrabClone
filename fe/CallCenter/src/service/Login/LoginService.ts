import axios from 'axios';
import {ReqLogin} from "../../@types/login";
import { instance } from '../Interceptor/ApiService';

const POST_LOGIN_FORM_API_URL = "http://localhost:8080/login";
const GET_REFRESH_TOKEN_API_URL = "http://localhost:8080/refresh-token";
const GET_USER_LIST_API_URL="http://localhost:8080/api/users";
const config2= {
  headers: {
    'Content-Type': 'multipart/form-data',
  },
}


class LoginService {
  postLoginForm({username, password}: ReqLogin) {
    const loginElement = {username, password} as ReqLogin;
    return instance.post(POST_LOGIN_FORM_API_URL, loginElement)
  }

  getRefreshToken(refreshToken: String) {
    const config={
      headers:{
        Authorization: `Bearer ${refreshToken}`
      }
    }
    return instance.get(GET_REFRESH_TOKEN_API_URL,config)
  }
  getListUser(accessToken:String){
    console.log("hahahah")
    const config={
      headers:{
        Authorization: `Bearer ${accessToken}`
      }
    }
    return instance.get(GET_USER_LIST_API_URL,config);
  }
}

export default new LoginService()

