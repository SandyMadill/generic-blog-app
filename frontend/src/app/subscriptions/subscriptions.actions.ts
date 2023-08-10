import { createAction, props } from "@ngrx/store"
import { User } from "../user/user.model"

export const subscribeToUserRequested = createAction(
    '[Subscriptions] Subscribe To User Requested',
    props<{payload:number}>()
)

export const subscribeToUserSucceeded = createAction(
    '[Subscriptions] Subscribe To User Succeeded',
    props<{payload:User}>()
)

export const subscribeToUserFailed = createAction(
    '[Subscriptions] Subscribe To User Failed',
    props<{error:Error}>()
)

export const unsubscribeToUserRequested = createAction(
    '[Subscriptions] Unsubscribe To User Requested',
    props<{payload:number}>()
)

export const unsubscribeToUserSucceeded = createAction(
    '[Subscriptions] Unsubscribe To User Succeeded',
    props<{payload:User}>()
)

export const unsubscribeToUserFailed = createAction(
    '[Subscriptions] Unsubscribe To User Failed',
    props<{error:Error}>()
)

export const getSubscriptionsRequested = createAction(
    '[Subscriptions] Get Subscriptions Requested'
)

export const getSubscriptionsSucceeded = createAction(
    '[Subscriptions] Get Subscriptions Succeeded',
    props<{payload:User[]}>()
)

export const getSubscriptionsFailed = createAction(
    '[Subscriptions] Get Subscriptions Failed',
    props<{error:Error}>()
)