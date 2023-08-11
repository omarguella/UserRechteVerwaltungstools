import { TTokens } from "./../types/auth.d";
import axios from "axios";
import { useAppSelector } from "../hooks/redux";
import UseGetAuth from "../hooks/getAuth";
import { GetAccessAction } from "../redux/actions/auth";
import { store } from "../redux/store";

export const api = axios.create({
  baseURL: import.meta.env.VITE_URL,
  timeout: 6000,
});

// Define the interceptor
api.interceptors.request.use(async config => {
  const { tokens } = await UseGetAuth();
  try {
    // Make the other API call to get the desired result
    await store.dispatch(GetAccessAction());
    // Access the result from the response and set it in the header
    config.headers["Authorization"] = `Bearer ${tokens?.accessToken}`;
    config.headers["refreshToken"] = tokens?.refreshToken;
    config.auth;

    return config;
  } catch (error) {
    console.error("Interceptor error:", error);
    return Promise.reject(error);
  }
});

/**
 *
 * @param {string} endpoint
 * @returns {Response}
 */
export const _GET = async (endpoint: string) => {
  const response = await api.get(endpoint);
  return response;
};

/**
 *
 * @param endpoint
 * @param payload
 * @returns {Response}
 */
export const _POST = async (endpoint: string, payload: any) => {
  const response = await api.post(endpoint, payload);
  return response;
};

/**
 *
 * @param endpoint
 * @param payload
 * @returns {Response}
 */
export const _PUT = async (endpoint: string, payload: any) => {
  const response = await api.put(endpoint, payload);
  return response;
};

/**
 *
 * @param endpoint
 * @returns {Response}
 */
export const _DELETE = async (endpoint: string) => {
  const response: Response = await api.delete(endpoint);
  return response;
};
