import { createAsyncThunk } from "@reduxjs/toolkit";
import { _DELETE, _GET, _POST, _PUT } from "../../api/config";
import { Role } from "../../types/roles";

/**
 * Fetch Role By Name
 */
export const FindRoleByNameAction = createAsyncThunk(
  "roles/find",
  async ({ name }: { name: string }, { rejectWithValue }) => {
    try {
      const response = await _GET(`/roles/name/${name}`);
      return response.data as Role[];
    } catch (error) {
      return rejectWithValue("Something wrong try again please");
    }
  }
);

/**
 * Update Role By Name
 */
export const UpdateRoleByNameAction = createAsyncThunk(
  "roles/update",
  async ({ data }: { data: Role }, { rejectWithValue }) => {
    try {
      const { id, ...rest } = data;
      const response = await _PUT(`/roles/name/${id}`, rest);
      return response.data as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again please");
    }
  }
);

/**
 * Add Role
 */
export const CreateRoleAction = createAsyncThunk(
  "roles/create",
  async ({ data }: { data: Omit<Role, "id"> }, { rejectWithValue }) => {
    try {
      const response = await _POST(`/roles`, data);
      return response.data as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again please");
    }
  }
);

/**
 * Add Role
 */
export const DeleteRoleAction = createAsyncThunk(
  "roles/delete",
  async (
    { name, role }: { name: string; role: string },
    { rejectWithValue }
  ) => {
    try {
      const response = await _DELETE(`/roles/name/${name}?movedToRole=${role}`);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again please");
    }
  }
);
