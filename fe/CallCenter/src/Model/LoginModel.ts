

export interface loginForm {
    username?: string;
    password?: string;
  }

export interface authority {
    authority?: string
  }

export  interface userInfo {
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
