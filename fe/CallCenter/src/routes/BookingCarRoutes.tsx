import React, { lazy, Suspense } from "react"
import { Switch } from "react-router-dom"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import { PATH } from "src/constants/paths"
import Loading from "src/components/Loading/Loading"
const CreateCall = lazy(() => import("src/pages/BookingCar/BookingCar"))

export default function BookingCarRoutes() {
  return (
    <Switch>
      <AuthenticatedGuard
        exact
        path={PATH.BOOKINGCAR}
        component={() => (
          <Suspense fallback={<Loading />}>
            <CreateCall />
          </Suspense>
        )}
      />
    </Switch>
  )
}
