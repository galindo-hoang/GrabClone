import axios from 'axios';
import {ReqLogin} from "../../@types/login";
import { instance } from '../Interceptor/ApiService';

const POST_LOGIN_FORM_API_URL = "http://localhost:8085/login";
const GET_REFRESH_TOKEN_API_URL = "http://localhost:8085/refresh-token";
const GET_USER_LIST_API_URL="http://localhost:8085/api/users";


class LoginService {
  postLoginForm({username, password}: ReqLogin) {
    const loginElement = {username, password} as ReqLogin;
    const config= {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
    return instance.post(POST_LOGIN_FORM_API_URL, loginElement,config)
  }

  getRefreshToken(refreshToken: String) {
    const config={
      headers:{
        Authorization: `Bearer ${refreshToken}`,
        'Content-Type': 'multipart/form-data',
      }
    }
    return instance.get(GET_REFRESH_TOKEN_API_URL,config)
  }
  getListUser(accessToken:String){
    const config={
      headers:{
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'multipart/form-data',
      }
    }
    return instance.get(GET_USER_LIST_API_URL,config);
  }
}

export default new LoginService()

