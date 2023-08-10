import { Injectable } from "@angular/core";
import { UserService } from "./user.service";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { getUserByIdRequested, getUserByIdSucceeded, getUsersFailed, getUsersRequested, getUsersSucceeded } from "./user.actions";
import { catchError, map, of, switchMap } from "rxjs";
import { getBlogByIdFailed } from "../blogs/blogs.actions";

@Injectable()
export class UserEffects{

    getUserById$ = createEffect(()=>
        this.actions$.pipe(
            ofType(getUserByIdRequested),
            switchMap(action => this.service.getUserById(action.payload)
            .pipe(
                map(payload => getUserByIdSucceeded({payload:payload})),
                catchError(e => of(getBlogByIdFailed({error:e})))
            ))
        )
    )

    getUsers$ = createEffect(()=>
        this.actions$.pipe(
            ofType(getUsersRequested),
            switchMap(action => this.service.getUserPage(action.payload.page, action.payload.size)
            .pipe(
                map(payload => getUsersSucceeded({payload:payload})),
                catchError(e => of(getUsersFailed({error:e})))
            ))
        )
    )
 
    constructor(
        private readonly actions$:Actions,
        private readonly service:UserService
    ){}
}