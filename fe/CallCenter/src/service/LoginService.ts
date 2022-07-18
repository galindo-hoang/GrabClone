import axios from 'axios';
import {ReqLogin} from "../@types/login";

const LOGIN_FORM_API_URL = "http://localhost:8080/login";
const GET_REFRESH_TOKEN_API_URL = "http://localhost:8080/refresh-token";

const config = {
  headers: {
    'Content-Type': 'multipart/form-data',
  },
  crossOrigin: {
    'Access-Control-Allow-Origin': '*',
  },
  mode: {
    'mode': 'cors'
  }
}

class LoginService {
  postLoginForm({username, password}: ReqLogin) {
    const loginElement = {username, password} as ReqLogin;
    return axios.post(LOGIN_FORM_API_URL, loginElement, config)
  }

  getRefreshToken(refreshToken: String) {
    const refreshTokenFormat= {Authorization:`Bearer ${refreshToken}`} as object;
    return axios.get(GET_REFRESH_TOKEN_API_URL,refreshTokenFormat)
  }
}

export default new LoginService()

