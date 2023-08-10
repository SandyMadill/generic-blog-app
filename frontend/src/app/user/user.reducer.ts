import { EntityAdapter, EntityState, createEntityAdapter } from "@ngrx/entity";
import { User } from "./user.model";
import { createReducer, on } from "@ngrx/store";
import { getUserByIdSucceeded, getUsersSucceeded } from "./user.actions";

export interface State extends EntityState<User>{
    fetched:boolean,
    pages:number
}

const adapter: EntityAdapter<User> = createEntityAdapter<User>({
    selectId:user => user.id
})

const initialState:State = adapter.getInitialState({
    fetched:true,
    pages:0
})

const UserReducer = createReducer(
    initialState,
    on(getUserByIdSucceeded,(state,{payload}) => ({
        ...adapter.addOne(payload,state)
    })),
    on(getUsersSucceeded, (state, {payload}) => ({
        ...adapter.addMany(payload.users,state),
        pages:payload.pages
    }))
)

export function getReducer(state,action){
    return UserReducer(state,action);
}

export const { selectAll, selectEntities, selectIds, selectTotal } = adapter.getSelectors();