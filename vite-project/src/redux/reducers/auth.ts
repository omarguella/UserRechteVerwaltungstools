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
      .addCase(LoginAction.pending, state => {
        // Add user to the state array
        state.loading = true;
      })
      .addCase(RegisterAction.pending, state => {
        // Add user to the state array
        state.loading = true;
      })
      .addCase(LoginAction.fulfilled, (state, action: { payload: TTokens }) => {
        state.loading = false;
        // Add user to the state array
        state.isConnected = true;
        state.tokens = action.payload;
      })
      .addCase(LoginAction.rejected, state => {
        state.loading = false;
      })
      .addCase(RegisterAction.rejected, state => {
        // Add user to the state array
        state.loading = false;
      })
      .addCase(
        GetAccessAction.fulfilled,
        (state, action: { payload: TTokens }) => {
          state.tokens = action.payload;
        }
      )
      .addCase(LogoutAction.fulfilled, (state, action) => {
        state.isConnected = false;
        state.tokens = null;
      });
  },
});

export const AuthReducer = AuthSlice.reducer;
