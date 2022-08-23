import React, { lazy, Suspense } from "react"
import { Switch } from "react-router-dom"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import { PATH } from "src/constants/paths"
import Loading from "src/components/Loading/Loading"
import MapGuard from "../guards/MapGuard";
const History = lazy(() => import("src/pages/UserInfo/UserInfo"))

export default function UserInfoRoutes() {
  
  return (
    <Switch>
      <AuthenticatedGuard
        exact path={PATH.USERINFO}
        component={() => (
          <Suspense fallback={<Loading />}>
            <History />
          </Suspense>
        )}
      />
    </Switch>
  )
}
