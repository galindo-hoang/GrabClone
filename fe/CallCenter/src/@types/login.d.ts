export interface ReqLogin {
  username: string
  password: string
}
interface ResLoginApi extends Res {
  data: {
    access_token: string
  }
}

interface tokenApi{
  refreshToken: string
  accessToken:string
}

export interface authority {
  authority?: string
}

export interface userInfo {
  password?: string;
  username?: string;
  authorities?: authority[];
  accountNonExpired?: boolean;
  accountNonLocked?: boolean;
  credentialsNonExpired?: boolean;
  enabled?: boolean;
}

export interface loginResult {
  accessToken: string;
  user?: userInfo;
  refreshToken?: string;
}


interface ResLogin extends ActionRedux {}
