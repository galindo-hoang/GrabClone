import React, { lazy, Suspense } from "react"
import { Switch } from "react-router-dom"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import { PATH } from "src/constants/paths"
import Loading from "src/components/Loading/Loading"
import MessageService from "src/service/Message/MessageService"
import MapGuard from "../guards/MapGuard";
const Map = lazy(() => import("src/pages/Map/Map"))

export default function MapRoutes() {
  return (
    <Switch>
      <AuthenticatedGuard
        exact
        path={PATH.MAP}
        component={() => (
          <MapGuard
            exact
            path={PATH.MAP}
            component={()=>(
              <Suspense fallback={<Loading />}>
                <Map/>
              </Suspense>
            )}
          />
        )}
      />
    </Switch>
  )
}
