import React, { lazy, Suspense } from "react"
import { Switch } from "react-router-dom"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import { PATH } from "src/constants/paths"
import Loading from "src/components/Loading/Loading"
const History = lazy(() => import("src/pages/History/History"))

export default function HistoryRoutes() {
  
  return (
    <Switch>
      <AuthenticatedGuard
        exact
        path={PATH.HISTORY}
        component={() => (
          <Suspense fallback={<Loading />}>
            <History />
          </Suspense>
        )}
      />
    </Switch>
  )
}
