import { combineReducers } from "redux";
import { AuthReducer } from "./auth";
import { UsersReducer } from "./user";

const reducers = combineReducers({
  auth: AuthReducer,
  users: UsersReducer,
});
export type RootState = ReturnType<typeof reducers>;
export default reducers;
