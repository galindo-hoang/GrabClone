import React from "react"
import { connect } from "react-redux"
import {Redirect, Route, RouteComponentProps, RouteProps } from "react-router-dom"
import { PATH } from "src/constants/paths"

interface ReduxProps {
  isAuthenticated: boolean
}
interface Props extends ReduxProps, RouteProps {
  component: React.ComponentType<RouteComponentProps>
}

const AuthenticatedGuard=(props: Props):JSX.Element=>{
  const { isAuthenticated, component: Component, ...rest } = props
  const render:JSX.Element=(
    <Route
      {...rest}
      render={(props):JSX.Element => {
        if (!isAuthenticated || !localStorage.getItem("accessToken") || !localStorage.getItem("refreshToken")) {
          return <Redirect to={PATH.LOGIN as string} />
        }
        return <Component {...props} />
      }}
    />
  )
  return render
}

const mapStateToProps = state => ({
  isAuthenticated: state.app.isAuthenticated
})

const mapDispatchToProps = {}

export default connect(mapStateToProps, mapDispatchToProps)(AuthenticatedGuard)
