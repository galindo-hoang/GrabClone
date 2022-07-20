import React, { lazy, Suspense } from "react"
import { Switch } from "react-router-dom"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import { PATH } from "src/constants/paths"
import Loading from "src/components/Loading/Loading"
const Home = lazy(() => import("src/pages/Map/Map"))

export default function MapRoutes() {
  return (
    <Switch>
      <AuthenticatedGuard
        exact
        path={PATH.MAP}
        component={() => (
          <Suspense fallback={<Loading />}>
            <Home />
          </Suspense>
        )}
      />
    </Switch>
  )
}