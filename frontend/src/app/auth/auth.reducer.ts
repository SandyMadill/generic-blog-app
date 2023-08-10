import { Action, createReducer, on } from "@ngrx/store";
import * as authActions from "./auth.actions"
import { User } from "src/app/user/user.model";

export interface State{
    token?: string,
    userData?:User
    loggedIn:boolean
}

const initialState: State = {
    loggedIn:false
}

const authReducer = createReducer(
    initialState,
    on(authActions.logInSucceeded, (state, { payload }) => ({
        ...state,
        token: payload,
        loggedIn:true
    })),
    on(authActions.getLoggedInUserSucceeded, (state, { payload }) => ({
        ...state,
        userData: payload
    })),
    on(authActions.logOutRequested, () => initialState)
)

export function reducer(state: State, actions: Action) {
    return authReducer(state, actions);
}