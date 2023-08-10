import { createFeatureSelector, createSelector } from "@ngrx/store";
import * as blogsReducer from "./blogs.reducer"

export const selectBlogsState = createFeatureSelector<blogsReducer.State>("blogs")

export const selectAllBlogs = createSelector(
    selectBlogsState,
    blogsReducer.selectAll
)

export const selectBlogEntites = createSelector(
    selectBlogsState,
    blogsReducer.selectEntities
)

export const selectFetched = createSelector(
    selectBlogsState,
    state => state.fetched
)

export const selectPages = createSelector(
    selectBlogsState,
    state => state.pages
)