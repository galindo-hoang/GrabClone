import {connect, ConnectedProps} from "react-redux";
import {login} from "../pages/Login/Login.thunks";
import {Redirect, Route, RouteComponentProps, RouteProps} from "react-router-dom";
import React from "react";
import {PATH} from "../constants/paths";


const mapStateToProps = state => ({
  departure:state.bookingCar?.departure,
  destination:state.bookingCar?.destination
})

const mapDispatchToProps = {}

interface ReduxProps {
  departure:any,
  destination:any
}

interface Props extends ReduxProps, RouteProps {
  component: React.ComponentType<RouteComponentProps>
}

const connector = connect(mapStateToProps, mapDispatchToProps)

const MapGuard=(props:Props)=>{
  const {departure,destination,component: Component, ...rest}=props;
  const render:JSX.Element=(
    <Route
      {...rest}
      render={(props):JSX.Element => {
        if (!localStorage.getItem("location")) {
          return <Redirect to={PATH.BOOKINGCAR as string} />
        }
        return <Component {...props} />
      }}
    />
  )
  return render;
}

export default connector(MapGuard)