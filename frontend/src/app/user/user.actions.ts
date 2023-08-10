import { createAction, props } from "@ngrx/store";
import { User } from "./user.model";


export const getUsersRequested = createAction(
    '[Users] Get Users Requested',
    props<{payload:{page:number,size:number}}>()
)

export const getUsersSucceeded = createAction(
    '[Users] Get Users Succeeded',
    props<{payload:{users:User[], pages:number}}>()
)

export const getUsersFailed = createAction(
    '[Users] Get UsersFailed',
    props<{error:Error}>() 
)

export const getUserByIdRequested = createAction(
    '[Users] Get User By Id Requested',
    props<{payload:number}>()
)

export const getUserByIdSucceeded = createAction(
    '[Users] Get User By Id Succeeded',
    props<{payload:User}>()
)

export const getUserByIdFailed = createAction(
    '[Users] Get User By ID Failed',
    props<{error:Error}>() 
)