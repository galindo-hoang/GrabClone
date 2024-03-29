import React from "react"
import ReactDOM from "react-dom"
import "src/assets/scss/index.scss"
import App from "./App/App"
import * as serviceWorker from "./serviceWorker"
import { Provider } from "react-redux"
import {PersistGate} from "redux-persist/integration/react";
import {persistor,store} from "./store/persistStore";
import Loading from "./components/Loading/Loading"

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <PersistGate loading={<Loading />} persistor={persistor}>
      <App />
      </PersistGate>
    </Provider>
  </React.StrictMode>,
  document.getElementById("root")
)

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
// serviceWorker.unregister()
