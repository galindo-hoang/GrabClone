import {applyMiddleware, compose, createStore} from 'redux';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import rootReducer from "../reducer/reducer";
import thunk from "redux-thunk";
const composeEnhancers =
  typeof window === "object" &&
  process.env.NODE_ENV === "development" &&
  (window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ? (window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({})
    : compose

const persistConfig = {
  key: 'root',
  storage: storage,
};

const pReducer = persistReducer(persistConfig, rootReducer);
const enhancer = composeEnhancers(applyMiddleware(thunk))
export const store = createStore(pReducer,enhancer);
export const persistor = persistStore(store);