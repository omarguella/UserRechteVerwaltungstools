import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import UseGetAuth from "../../hooks/getAuth";
import { TLogin } from "../../types/auth";
import { DeleteCookies } from "../../lib/cookies";
import { _POST } from "../../api/config";

const BASE_URL = import.meta.env.VITE_URL;

// Login
export const LoginAction = createAsyncThunk(
  "auth/login",
  async (data: TLogin, { rejectWithValue }) => {
    try {
      const response = await axios.post(`${BASE_URL}/auth/login`, data);
      return response.data;
    } catch (error) {
      return rejectWithValue("Opps error ocurred try again");
    }
  }
);

// Registration
export const RegisterAction = createAsyncThunk(
  "auth/registration",
  async (data: any, { rejectWithValue }) => {
    const { role, ...rest } = data;
    try {
      const response = await axios.post(
        `${BASE_URL}/auth/registration/${role}`,
        rest
      );
      return response.data;
    } catch (error) {
      return rejectWithValue("Opps error ocurred try again");
    }
  }
);

// Login
export const GetAccessAction = createAsyncThunk("auth/access", async () => {
  const { tokens } = await UseGetAuth();
  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${tokens?.accessToken}`,
    refreshToken: tokens?.refreshToken,
  };
  try {
    const response = await axios.get(`${BASE_URL}/auth/getAccessToken`, {
      headers,
    });
    return response.data;
  } catch (error) {
    return error;
  }
});

export const LogoutAction = createAsyncThunk("auth/logout", async () => {
  DeleteCookies("current-role");
  return null;
});

export const LogoutAllAction = createAsyncThunk(
  "auth/logout/all",
  async ({ val }, { rejectWithValue }) => {
    try {
      const response = await _POST("/auth/logoutAll", val);
      return response.data;
    } catch (error: any) {
      return rejectWithValue("Something wrong try again");
    }
  }
);
