import { combineReducers } from "redux";
import { AuthReducer } from "./auth";
import { UsersReducer } from "./user";
import { RolesReducer } from "./roles";

const reducers = combineReducers({
  auth: AuthReducer,
  users: UsersReducer,
  roles: RolesReducer,
});
export type RootState = ReturnType<typeof reducers>;
export default reducers;
