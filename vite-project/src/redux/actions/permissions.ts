import { createAsyncThunk } from "@reduxjs/toolkit";
import { _DELETE, _POST, _PUT } from "../../api/config";
import { Permissions } from "../../types/permissions";

export type PayloadPermission = Partial<
  Pick<Permissions, "permissionKey" | "roleName" | "type">
>;

/**
 * Update Permission
 */
export const UpdatePermissionAction = createAsyncThunk(
  "roles/permission/update",
  async ({ data }: { data: PayloadPermission }, { rejectWithValue }) => {
    try {
      const response = await _PUT("/permissionRole", data);
      return response;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Create Permission
 */

export const CreatePermissionAction = createAsyncThunk(
  "roles/permission/create",
  async ({ data ,name}: { data: PayloadPermission, name: string }, { rejectWithValue }) => {
    try {
        data.roleName=name;
      const response = await _POST("/permissionRole", data);
      return response.data as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission
 */

export const DeletePermissionAction = createAsyncThunk(
  "roles/permission/delete",
  async (
    { data }: { data: Omit<PayloadPermission, "type"> },
    { rejectWithValue }
  ) => {
    try {
      const response = await _DELETE(
        `/permissionRole/${data.roleName}?permissionKey=${data.permissionKey}`
      );
      return response;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission by key
 */

export const DeletePermissionByKeyAction = createAsyncThunk(
  "permissions/deletekey",
  async ({ key }: { key: string }, { rejectWithValue }) => {
    try {
      const response = await _DELETE(`/permissions/key/${key}`);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission by key
 */

export const CreatePermissionByMethodAction = createAsyncThunk(
  "permissions/create",
  async ({ data }: { data: any }, { rejectWithValue }) => {
    try {
      const response = await _POST(`/permissions`, data);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

export const DeleteLogAction = createAsyncThunk(
  "permissions/log/delete",
  async ({ id }: { id: number }, { rejectWithValue }) => {
    try {
      const response = await _DELETE(`/logs/${id}`);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);
