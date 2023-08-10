import { createAction, props } from "@ngrx/store"
import { User } from "src/app/user/user.model";

export const logInRequested = createAction(
    '[Auth] Log In Requested',
    props<{payload:{username:string, password:string}}>()
)
export const logInSucceeded = createAction(
    '[Auth] Log In Succeeded',
    props<{payload:string}>(),
);

export const logInFailed = createAction(
    '[Auth] Log In Failed',
    props<{error:Error}>(),
);

export const registerUserRequested = createAction(
    '[Auth] Register User Requested',
    props<{payload:{username:string,displayName:string,password:string}}>()
)

export const registerUserSucceeded = createAction(
    '[Auth] Register User Succeeded',
    props<{payload:{username:string,displayName:string,password:string,role:string,active:boolean}}>()
)

export const registerUserFailed = createAction(
    '[Auth] Register User Failed',
    props<{error:Error}>()
)

export const getLoggedInUserRequested = createAction(
    '[Auth] Get Logged In User Requested'
)

export const getLoggedInUserSucceeded = createAction(
    '[Auth] Get Logged In User Succeeded',
    props<{payload:User}>(),
)

export const getLoggedInUserFailed = createAction(
    '[Auth] Get Logged In User Failed',
    props<{error:Error}>(),
)

export const logOutRequested = createAction(
    '[Auth] Log Out Requested'
)