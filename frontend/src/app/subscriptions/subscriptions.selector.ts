import { createFeatureSelector, createSelector } from "@ngrx/store";
import * as subscriptionsReducer from "./subscriptions.reducer"

export const selectSubscriptionsState = createFeatureSelector<subscriptionsReducer.State>("subscriptions")

export const selectAllSubscriptions = createSelector(
    selectSubscriptionsState,
    subscriptionsReducer.selectAll
)

export const selectSubscriptionsEntites = createSelector(
    selectSubscriptionsState,
    subscriptionsReducer.selectEntities
)

export const selectFetched = createSelector(
    selectSubscriptionsState,
    state => state.fetched
)