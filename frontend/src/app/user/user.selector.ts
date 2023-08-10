import { createFeatureSelector, createSelector } from "@ngrx/store";
import * as userReducer from "./user.reducer"

export const selectUserState = createFeatureSelector<userReducer.State>("user")

export const selectAllUsers = createSelector(
    selectUserState,
    userReducer.selectAll
)

export const selectUserEntites = createSelector(
    selectUserState,
    userReducer.selectEntities
)

export const selectFetched = createSelector(
    selectUserState,
    state => state.fetched
)

export const selectPages = createSelector(
    selectUserState,
    state => state.pages
)