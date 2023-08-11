import { createAsyncThunk } from "@reduxjs/toolkit";
import { _DELETE, _GET, _POST, _PUT } from "../../api/config";
import { User } from "../../types/user";
import { TRegistration } from "../../types/auth";

/**
 * Fetch One User By Id
 */
export const GetUserAction = createAsyncThunk(
  "users/findOne",
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await _GET(`/users/id/${id}`);
      return response.data as User;
    } catch (error) {
      return rejectWithValue(`Something wrong try again`);
    }
  }
);

/**
 * Fetch All users
 */
export const GetUsersAction = createAsyncThunk("users/findAll", async () => {
  try {
    const response = await _GET(`/users`);
    return response.data as User;
  } catch (error) {
    return error;
  }
});

/**
 * Update email
 */
export const UpdateEmailAction = createAsyncThunk(
  "users/email",
  async ({ id, email }: { id: number; email: string }, { rejectWithValue }) => {
    try {
      const response = await _PUT(`/users/email/${id}`, email);
      return response.data;
    } catch (error) {
      return rejectWithValue(`Something wrong try again`);
    }
  }
);

/**
 * Update password
 */
export const UpdatePasswordAction = createAsyncThunk(
  "users/password",
  async (
    { id, payload }: { id: number; payload: any },
    { rejectWithValue }
  ) => {
    try {
      const response = await _PUT(`/users/password/${id}`, payload);
      return response.data;
    } catch (error) {
      return rejectWithValue(
        `Maybe your old password is not correct or Something wrong! try again`
      );
    }
  }
);

/**
 * Update profile
 */
export const UpdateProfileAction = createAsyncThunk(
  "users/profile",
  async ({ data, id }: { data: User; id: number }, { rejectWithValue }) => {
    try {
      data.roles =
        data.roles.length === 1
          ? data.roles.map(role => role?.label)
          : data.roles;
      const response = await _PUT(`/users/profile/${id}`, data);
      return response.data;
    } catch (error) {
      return rejectWithValue(`Something wrong try again`);
    }
  }
);

/**
 * Create private user
 */
export const CreatePrivateUserAction = createAsyncThunk(
  "users/create",
  async ({ data }: { data: TRegistration }, { rejectWithValue }) => {
    try {
      const { roles, ...rest } = data;
      const response = await _POST(`/users/signup/${roles}`, rest);
      return response.data;
    } catch (error) {
      return rejectWithValue(`Something wrong try again`);
    }
  }
);

/**
 * Delete private user
 */
export const DeleteUserAction = createAsyncThunk(
  "users/delete",
  async ({ id }: { id: number }, { rejectWithValue }) => {
    try {
      await _DELETE(`/users/id/${id}`);
    } catch (error) {
      return rejectWithValue(`Something wrong try again`);
    }
  }
);
