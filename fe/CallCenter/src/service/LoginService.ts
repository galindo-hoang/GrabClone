import axios from 'axios';
import {promises} from "dns";

const LOGIN_FORM_API_URL = "http://localhost:8080/login";

class LoginService {
  async postLoginForm({username, password}: ReqLogin) {
    const loginElement = {username: username, password: password} as ReqLogin;
    console.log(loginElement);
    return await axios.post(LOGIN_FORM_API_URL, loginElement, {
      headers: {

       /*'Access-Control-Allow-Origin':"*",*/
        'Content-Type': 'multipart/form-data',

      },
    })
  }
}

export default new LoginService()

