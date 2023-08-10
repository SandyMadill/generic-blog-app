import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { switchMap, map, catchError, of } from "rxjs";
import { getUsersRequested, getUsersSucceeded, getUsersFailed } from "../user/user.actions";
import { UserService } from "../user/user.service";
import { SubscriptionsService } from "./subscriptions.service";
import { getSubscriptionsFailed, getSubscriptionsRequested, getSubscriptionsSucceeded, subscribeToUserFailed, subscribeToUserRequested, subscribeToUserSucceeded, unsubscribeToUserFailed, unsubscribeToUserRequested, unsubscribeToUserSucceeded } from "./subscriptions.actions";

@Injectable()
export class SubscriptionsEffects{
    subscribeToUser$ = createEffect(()=>
        this.actions$.pipe(
            ofType(subscribeToUserRequested),
            switchMap(action => this.service.subscribeToUser(action.payload)
            .pipe(
                map(payload => subscribeToUserSucceeded({payload:payload})),
                catchError(e => of(subscribeToUserFailed({error:e})))
            ))
        )
    )

    unsubscribeToUser$ = createEffect(()=>
        this.actions$.pipe(
            ofType(unsubscribeToUserRequested),
            switchMap(action => this.service.unsubscribeToUser(action.payload)
            .pipe(
                map(payload => unsubscribeToUserSucceeded({payload:payload})),
                catchError(e => of(unsubscribeToUserFailed({error:e})))
            ))
        )
    )

    getSubscriptions$ = createEffect(()=>
        this.actions$.pipe(
            ofType(getSubscriptionsRequested),
            switchMap(() => this.service.getSubscriptions()
            .pipe(
                map(payload => getSubscriptionsSucceeded({payload:payload})),
                catchError(e => of(getSubscriptionsFailed({error:e})))
            ))
        )
    )
 
    constructor(
        private readonly actions$:Actions,
        private readonly service:SubscriptionsService
    ){}
}