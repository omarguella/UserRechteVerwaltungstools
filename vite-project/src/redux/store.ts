import { configureStore } from "@reduxjs/toolkit";
import reducers from "./reducers";
import { useDispatch } from "react-redux";

export const store = configureStore({
  reducer: reducers,
});

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
