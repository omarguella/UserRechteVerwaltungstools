import { createSlice } from "@reduxjs/toolkit";
import { TTokens } from "../../types/auth";
import {
  GetAccessAction,
  LoginAction,
  LogoutAction,
  RegisterAction,
} from "../actions/auth";

interface Auth {
  isConnected: boolean;
  tokens: TTokens | null;
  loading: boolean;
}

const initialState: Auth = {
  isConnected: false,
  tokens: null,
  loading: false,
};

export const AuthSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase("auth/login/pending" || "auth/registration/pending", state => {
        // Add user to the state array
        state.loading = true;
      })

      .addCase("auth/registration/fulfilled", state => {
        // Add user to the state array
        state.loading = false;
      })
      .addCase("auth/login/fulfilled", (state, action: any) => {
        state.loading = false;
        // Add user to the state array
        state.isConnected = true;
        state.tokens = action.payload;
      })
      .addCase("auth/login/rejected", state => {
        state.loading = false;
      })
      .addCase("auth/registration/rejected", state => {
        // Add user to the state array
        state.loading = false;
      })
      .addCase("auth/access/fulfilled", (state, action: any) => {
        state.tokens = action.payload;
      })
      .addCase("auth/logout/fulfilled", (state, action) => {
        state.isConnected = false;
        state.tokens = null;
      });
  },
});

export const AuthReducer = AuthSlice.reducer;
