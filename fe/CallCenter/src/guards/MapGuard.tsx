import {connect, ConnectedProps} from "react-redux";
import {login} from "../pages/Login/Login.thunks";
import {Redirect, Route, RouteComponentProps, RouteProps} from "react-router-dom";
import React from "react";
import {PATH} from "../constants/paths";
import {initialCreateBooking} from "../@types/map";


const mapStateToProps = state => ({
  bookingCar:state.bookingCar
})

const mapDispatchToProps = {}

interface ReduxProps {
  bookingCar:initialCreateBooking
}

interface Props extends ReduxProps, RouteProps {
  component: React.ComponentType<RouteComponentProps>
}

const connector = connect(mapStateToProps, mapDispatchToProps)

const MapGuard=(props:Props)=>{
  const {bookingCar ,component: Component, ...rest}=props;
  const render:JSX.Element=(
    <Route
      {...rest}
      render={(props):JSX.Element => {
        if (bookingCar?.departure===null&&bookingCar?.destination===null) {
          return <Redirect to={PATH.BOOKINGCAR as string} />
        }
        return <Component {...props} />
      }}
    />
  )
  return render;
}

export default connector(MapGuard)