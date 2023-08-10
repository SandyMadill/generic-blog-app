import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects'
import { EMPTY, of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators'
import { AuthService } from './auth.service';
import { getLoggedInUserFailed, getLoggedInUserRequested, getLoggedInUserSucceeded, logInFailed, logInRequested, logInSucceeded, registerUserFailed, registerUserRequested, registerUserSucceeded } from './auth.actions';
import { Router } from '@angular/router';

@Injectable()
export class AuthEffects {

    logIn$ = createEffect( () =>
      this.actions$.pipe(
        ofType(logInRequested),
        switchMap(action => this.authService.logIn(action.payload.username, action.payload.password)
        .pipe(
            map(payload => (logInSucceeded({payload:payload}))),
            catchError(e => of(logInFailed({error:e})))
        ))
      )
    )

    getLoggedInUser$ = createEffect( () =>
        this.actions$.pipe(
            ofType(getLoggedInUserRequested),
            switchMap(() => this.authService.getLoggedInUser()
            .pipe(
                map(payload => (getLoggedInUserSucceeded({payload:payload}))),
                catchError(e => of(getLoggedInUserFailed({error:e})))
            ))
        )
    )

    registerUser$ = createEffect(() => 
        this.actions$.pipe(
            ofType(registerUserRequested),
            switchMap(action => this.authService.register(action.payload.username, 
                action.payload.displayName, 
                action.payload.password
            ).pipe(
                map(payload => (registerUserSucceeded({payload:payload}))),
                catchError(e => of(registerUserFailed({error:e})))
            ))
        )
    )


    constructor(
        private actions$: Actions,
        private authService: AuthService,
        private router: Router
    ){}
}