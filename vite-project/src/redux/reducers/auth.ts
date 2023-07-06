import { createSlice } from "@reduxjs/toolkit";
import { User } from "../../types/user";
import { LoginAction, RegisterAction } from "../actions/auth";

interface Auth {
  isConnected: boolean;
  user: User | null;
  loading: boolean;
}

const initialState: Auth = {
  isConnected: false,
  user: null,
  loading: false,
};

export const AuthSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(LoginAction.pending, (state, action) => {
        // Add user to the state array
        state.loading = true;
      })
      .addCase(RegisterAction.pending, (state, action) => {
        // Add user to the state array
        state.loading = true;
      })
      .addCase(LoginAction.fulfilled, (state, action) => {
        state.loading = false;
        // Add user to the state array
        state.isConnected = true;
        state.user = action.payload;
      })
      .addCase(LoginAction.rejected, (state, action) => {
        state.loading = false;
      })
      .addCase(RegisterAction.rejected, (state, action) => {
        // Add user to the state array
        state.loading = false;
      });
  },
});

export const AuthReducer = AuthSlice.reducer;
