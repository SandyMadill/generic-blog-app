import { createFeatureSelector, createSelector } from "@ngrx/store";
import * as authReducer from "./auth.reducer"


export const selectAuthState = createFeatureSelector<authReducer.State>('auth');

export const selectAuthToken = createSelector(
    selectAuthState,
    (state: authReducer.State) => state.token
)

export const selectAuthUser = createSelector(
    selectAuthState,
    (state: authReducer.State) => state.userData
)

export const selectAuthLoggedIn = createSelector(
    selectAuthState,
    (state: authReducer.State) => state.loggedIn
)