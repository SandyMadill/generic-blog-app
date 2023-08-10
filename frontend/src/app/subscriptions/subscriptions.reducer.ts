import { EntityAdapter, EntityState, createEntityAdapter } from "@ngrx/entity";
import { User } from "../user/user.model";
import { createReducer, on } from "@ngrx/store";
import { getSubscriptionsSucceeded, subscribeToUserSucceeded, unsubscribeToUserSucceeded } from "./subscriptions.actions";

export interface State extends EntityState<User>{
    fetched:boolean,
}

const adapter: EntityAdapter<User> = createEntityAdapter<User>({
    selectId:user => user.id
})

const initialState:State = adapter.getInitialState({
    fetched:true,
})

const SubscriptionsReducer = createReducer(
    initialState,
    on(subscribeToUserSucceeded,(state,{payload}) => ({
        initialState,
        ...adapter.addOne(payload,state)
    })),
    on(unsubscribeToUserSucceeded,(state,{payload}) => ({
        initialState,
        ...adapter.removeOne(payload.id,state)
    })),
    on(getSubscriptionsSucceeded, (state, {payload}) => ({
        ...adapter.addMany(payload,state),
    }))
)

export function getReducer(state,action){
    return SubscriptionsReducer(state,action);
}

export const { selectAll, selectEntities, selectIds, selectTotal } = adapter.getSelectors();