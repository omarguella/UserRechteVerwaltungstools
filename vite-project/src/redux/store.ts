import { configureStore } from "@reduxjs/toolkit";
import reducers from "./reducers";
import { useDispatch } from "react-redux";
import storage from "redux-persist/lib/storage";
import { persistReducer, persistStore } from "redux-persist";
const persistConfig = {
  key: "root", // The key prefix for the persisted state
  storage, // The storage mechanism to use
  // Additional configuration options if needed
  whiteList: ["auth"],
  blackList: ["users", "roles"],
};
const persistedReducer = persistReducer(persistConfig, reducers);

export const store = configureStore({
  reducer: persistedReducer,
});
export const persistor = persistStore(store);
export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
