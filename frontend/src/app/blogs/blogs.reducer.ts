import { EntityAdapter, EntityState, createEntityAdapter } from "@ngrx/entity"
import { Blog } from "./blogs.model"
import { createReducer, on } from "@ngrx/store"
import { clearBlogs, getBlogByIdRequested, getBlogsByIdSucceeded, getBlogsForUserSucceeded, getBlogsRequested, getBlogsSucceeded, postBlogSucceeded } from "./blogs.actions"
import { state } from "@angular/animations"



export interface State extends EntityState<Blog>{
    fetched:boolean
    pages?:number
}

const adapter:EntityAdapter<Blog> = createEntityAdapter<Blog>({
    selectId:blog=>blog.id
})

const initialState: State = adapter.getInitialState({
    fetched:false,
})

const BlogReducer = createReducer(
    initialState,
    on(getBlogsRequested, state=> ({ 
        ...state,
    })),
    on(getBlogsForUserSucceeded, (state, {payload}) => ({
        ...adapter.addMany(payload, state),
        fetched:true,
    })),
    on(getBlogsSucceeded, (state, {payload}) => ({
        ...adapter.addMany(payload.blogs, state),
        pages:payload.pages,
        fetched:true,
    })),
    on(getBlogByIdRequested, state=> ({ 
        ...state, 
    })),
    on(getBlogsByIdSucceeded, (state, {payload}) => ({
        ...adapter.addOne(payload, state),
        fetched:true,
    })),
    on(postBlogSucceeded, (state, {payload}) => ({
        ...adapter.addOne(payload, state),
        fetched:true
    })),
    on(clearBlogs, ()=>initialState)
)

export function getReducer(state,action){
    return BlogReducer(state,action);
}

export const { selectAll, selectEntities, selectIds, selectTotal } = adapter.getSelectors();


